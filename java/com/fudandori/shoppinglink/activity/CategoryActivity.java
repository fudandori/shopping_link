package com.fudandori.shoppinglink.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.Assist;
import com.fudandori.shoppinglink.DB;
import com.fudandori.shoppinglink.R;
import com.fudandori.shoppinglink.adapter.CategoryAdapter;
import com.fudandori.shoppinglink.dialog.InputTextDialog;
import com.fudandori.shoppinglink.dto.Category;
import com.fudandori.shoppinglink.listener.CategoryListener;
import com.fudandori.shoppinglink.listener.SlideListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) Assist.initToolbar(getSupportActionBar());

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter();
        adapter.setLongListener(c -> {
            final String value = c.getName();
            final String key = c.getKey();

            InputTextDialog dialog = new InputTextDialog();
            dialog.setText(value);
            dialog.setListener(s -> DB.CATEGORIES.child(key).setValue(s));
            dialog.show(getSupportFragmentManager(), "dialog");
        });

        SlideListener l = pos -> {
            String key = adapter.getItemKey(pos);
            DB.CATEGORIES
                    .child(key)
                    .removeValue();
        };

        ItemTouchHelper itemTouchHelper = Assist.getTouchHelper(l);
        itemTouchHelper.attachToRecyclerView(recycler);

        recycler.setAdapter(adapter);

        CategoryListener listener = new CategoryListener(adapter);
        DB.CATEGORIES.addChildEventListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cat_menu_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Buscar");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String text = newText.length() > 2 ? newText : null;
                adapter.getFilter().filter(text);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add) {
            InputTextDialog dialog = new InputTextDialog();

            dialog.setText("");
            dialog.setListener(x -> {
                if (x != null && x.length() > 0) {
                    boolean exists = adapter.exists(x);
                    String toastText = exists
                            ? "Ya existe " + x + " en la lista"
                            : x + " añadido";
                    if (!exists) DB.CATEGORIES.push().setValue(x);
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
                }

            });

            dialog.show(getSupportFragmentManager(), "dialog");
        }

        return super.onOptionsItemSelected(item);
    }
}
