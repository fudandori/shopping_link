package com.fudandori.shoppinglink.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.Assist;
import com.fudandori.shoppinglink.DB;
import com.fudandori.shoppinglink.R;
import com.fudandori.shoppinglink.adapter.ItemAdapter;
import com.fudandori.shoppinglink.adapter.ShopListAdapter;
import com.fudandori.shoppinglink.dialog.InputTextDialog;
import com.fudandori.shoppinglink.dto.Item;
import com.fudandori.shoppinglink.dto.ShopItem;
import com.fudandori.shoppinglink.listener.ItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ShopListActivity extends AppCompatActivity {

    ShopListAdapter shopAdapter;
    ItemAdapter<Item> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();
        initItemRecycler();
        initCartRecycler();
        initListeners();
    }

    private void initListeners() {
        ItemListener<Item, ItemAdapter<Item>> itemListener = new ItemListener<>(adapter, Item.class);
        DB.ITEMS.addChildEventListener(itemListener);

        DB.CART.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    final String key = child.getKey();
                    final ShopItem value = child.getValue(ShopItem.class);
                    shopAdapter.add(key, value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShopListActivity.this, "Error leyendo datos", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initItemRecycler() {
        final RecyclerView recycler = findViewById(R.id.recycler_select);
        recycler.setHasFixedSize(true);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        adapter = new ItemAdapter<>(x -> {
            final String key = DB.CART.push().getKey();
            final ShopItem s = new ShopItem(x);
            s.setQuantity(1);
            shopAdapter.add(key, s);
        });
        recycler.setAdapter(adapter);
    }

    private void initCartRecycler() {
        final RecyclerView recyclerPreview = findViewById(R.id.recycler_preview);
        recyclerPreview.setHasFixedSize(true);

        final RecyclerView.LayoutManager layoutManagerPreview = new LinearLayoutManager(this);
        recyclerPreview.setLayoutManager(layoutManagerPreview);

        shopAdapter = new ShopListAdapter(this::longClick);
        shopAdapter.disableRecolor();
        recyclerPreview.setAdapter(shopAdapter);

        ItemTouchHelper itemTouchHelper = Assist.getTouchHelper(x -> shopAdapter.delete(x));
        itemTouchHelper.attachToRecyclerView(recyclerPreview);
    }

    private void initLayout() {
        setContentView(R.layout.activity_item_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) Assist.initToolbar(getSupportActionBar());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_select_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.done_button) {
            shopAdapter.upload();
            super.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void longClick(ShopItem s) {
        InputTextDialog dialog = new InputTextDialog();
        dialog.setLabel("Cantidad");
        dialog.number();
        dialog.setText(String.valueOf(s.getQuantity()));
        dialog.setListener(y -> {
            try {
                final int q = Integer.parseInt(y);
                if (shopAdapter != null)
                    shopAdapter.updateQuantity(s.getKey(), q);
            } catch (Exception ignored) {

            }
        });
        dialog.show(getSupportFragmentManager(), "dialog");
    }
}
