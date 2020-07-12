package com.fudandori.shoppinglink.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import com.fudandori.shoppinglink.adapter.CategoryArrayAdapter;
import com.fudandori.shoppinglink.adapter.ItemAdapter;
import com.fudandori.shoppinglink.dialog.FilterDialog;
import com.fudandori.shoppinglink.dialog.ItemDialog;
import com.fudandori.shoppinglink.dto.Category;
import com.fudandori.shoppinglink.dto.Item;
import com.fudandori.shoppinglink.dto.ItemFilter;
import com.fudandori.shoppinglink.listener.CategoryArrayListener;
import com.fudandori.shoppinglink.listener.ItemFilterDialogListener;
import com.fudandori.shoppinglink.listener.ItemListener;
import com.fudandori.shoppinglink.listener.SlideListener;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    ItemAdapter<Item> adapter;
    CategoryArrayAdapter catAdapter;
    ItemFilter filter = new ItemFilter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();
        initSpinner();
        initRecycler();
    }

    private void initSpinner() {
        final int layout = android.R.layout.simple_spinner_item;

        catAdapter = new CategoryArrayAdapter(this, layout, new ArrayList<>());
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final CategoryArrayListener cListener = new CategoryArrayListener(catAdapter);
        DB.CATEGORIES.addChildEventListener(cListener);
    }

    private void initRecycler() {
        adapter = new ItemAdapter<>(item -> {
            ItemDialog dialog = new ItemDialog("Editar", item, catAdapter);
            dialog.show(getSupportFragmentManager(), "dialog");
        });

        final RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        final SlideListener l = x -> {
            final String key = adapter.get(x).getKey();
            DB.ITEMS
                    .child(key)
                    .removeValue();
        };

        final ItemTouchHelper itemTouchHelper = Assist.getTouchHelper(l);
        itemTouchHelper.attachToRecyclerView(recycler);

        ItemListener<Item, ItemAdapter<Item>> itemListener = new ItemListener<>(adapter, Item.class);
        DB.ITEMS.addChildEventListener(itemListener);
    }

    private void initLayout() {
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) Assist.initToolbar(getSupportActionBar());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add) {
            final ItemDialog dialog = new ItemDialog("Crear", catAdapter);
            dialog.show(getSupportFragmentManager(), "item-dialog");
        } else if (item.getItemId() == R.id.filter) {
            ItemFilterDialogListener listener = x -> {
                filter = x;
                adapter.filter(x);
            };

            List<String> list = adapter.getCategories();

            final FilterDialog dialog = new FilterDialog(filter, list, listener);
            dialog.show(getSupportFragmentManager(), "filter-dialog");
        }

        return super.onOptionsItemSelected(item);
    }
}
