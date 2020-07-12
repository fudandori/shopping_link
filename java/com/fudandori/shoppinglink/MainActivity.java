package com.fudandori.shoppinglink;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.activity.CategoryActivity;
import com.fudandori.shoppinglink.activity.ImportExportActivity;
import com.fudandori.shoppinglink.activity.ItemActivity;
import com.fudandori.shoppinglink.activity.ShopListActivity;
import com.fudandori.shoppinglink.adapter.ItemAdapter;
import com.fudandori.shoppinglink.adapter.ShopListAdapter;
import com.fudandori.shoppinglink.dialog.FilterDialog;
import com.fudandori.shoppinglink.dto.ItemFilter;
import com.fudandori.shoppinglink.dto.ShopItem;
import com.fudandori.shoppinglink.listener.ItemFilterDialogListener;
import com.fudandori.shoppinglink.listener.ItemListener;
import com.fudandori.shoppinglink.listener.RefreshListener;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.fudandori.shoppinglink.Assist.CONFIG_FILE;

public class MainActivity extends AppCompatActivity {

    private ItemFilter filter = new ItemFilter();
    private ShopListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String key;
        try (FileInputStream f = getApplicationContext().openFileInput(CONFIG_FILE)) {
            key = readKey(f);
        } catch (FileNotFoundException e) {
            key = generateKey();
        } catch (IOException e) {
            key = null;
        }

        if (key != null) {
            DB.setRoot(key);
            setContentView(R.layout.activity_main);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayShowTitleEnabled(false);

            TextView spent = findViewById(R.id.spent);
            TextView totalTV = findViewById(R.id.total);

            RecyclerView recyclerView = findViewById(R.id.rcl);
            recyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            RefreshListener refresh = (float total, float bought) -> {
                if (total != 0f) {
                    spent.setText(Float.toString(bought));
                    totalTV.setText(Float.toString(total));
                }
            };

            mAdapter = new ShopListAdapter(this::clickListener, null, refresh);
            recyclerView.setAdapter(mAdapter);

            ItemTouchHelper itemTouchHelper = Assist.getTouchHelper(mAdapter::remove);
            itemTouchHelper.attachToRecyclerView(recyclerView);

            ItemListener<ShopItem, ItemAdapter<ShopItem>> cListener = new ItemListener<>(mAdapter, ShopItem.class);
            DB.setCartListener(cListener);
        } else {
            toastError();
        }
    }

    private void clickListener(ShopItem i) {
        i.setStrike(!i.isStrike());
        DB.CART
                .child(i.getKey())
                .setValue(i);
    }

    private String readKey(FileInputStream f) throws IOException {
        int c;
        StringBuilder s = new StringBuilder();
        while ((c = f.read()) != -1) s.append((char) c);
        return s.toString();
    }

    private String generateKey() {
        String key = FirebaseDatabase.getInstance().getReference().push().getKey();
        if(key != null) Assist.writeKey(key, getApplicationContext());
        return key;
    }

    private void toastError() {
        Toast.makeText(this, "Error de acceso. Reinicie la aplicación", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Class c = null;

        switch (item.getItemId()) {
            case R.id.category_button:
                c = CategoryActivity.class;
                break;
            case R.id.items_button:
                c = ItemActivity.class;
                break;
            case R.id.list_button:
                c = ShopListActivity.class;
                break;
            case R.id.export_button:
                c = ImportExportActivity.class;
                break;
            case R.id.filter:
                ItemFilterDialogListener listener = x -> {
                    filter = x;
                    mAdapter.filter(x);
                };

                final List<String> list = mAdapter.getCategories();

                final FilterDialog dialog = new FilterDialog(filter, list, listener);
                dialog.show(getSupportFragmentManager(), "filter-dialog");
                break;
            default:
                break;
        }

        if (c != null) {
            Intent intent = new Intent(this, c);
            startActivity(intent);
            filter.reset();
        }

        return super.onOptionsItemSelected(item);
    }
}
