package com.fudandori.shoppinglink.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.fudandori.shoppinglink.DB;
import com.fudandori.shoppinglink.R;
import com.fudandori.shoppinglink.adapter.CategoryArrayAdapter;
import com.fudandori.shoppinglink.dto.Category;
import com.fudandori.shoppinglink.dto.Item;
import com.fudandori.shoppinglink.listener.ItemDialogListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;

import java.math.BigDecimal;
import java.util.List;

public class ItemDialog extends AppCompatDialogFragment {

    private final String title;
    private Item item;
    private CategoryArrayAdapter adapter;

    public ItemDialog(String title, CategoryArrayAdapter adapter) {
        this.title = title;
        this.adapter = adapter;
        item = new Item("", "", "", 0f);
    }

    public ItemDialog(String title, Item item, CategoryArrayAdapter adapter) {
        this.title = title;
        this.item = item;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_item, null);

        final EditText nameEdit = view.findViewById(R.id.item_edit_name);
        final EditText brandEdit = view.findViewById(R.id.item_edit_brand);
        final EditText priceEdit = view.findViewById(R.id.item_edit_price);
        Spinner spinner = view.findViewById(R.id.item_category_spinner);
        spinner.setAdapter(adapter);
        for (int i = 0; i < adapter.getCount(); i++) {
            if (item.getCategory().equalsIgnoreCase(adapter.getItem(i).getName())) {
                spinner.setSelection(i);
                break;
            }
        }
        nameEdit.setText(item.getName());
        brandEdit.setText(item.getBrand());
        priceEdit.setText(String.valueOf(item.getPrice()));
        builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                })
                .setPositiveButton("OK", (DialogInterface dialog, int which) -> {

                    final String cat = adapter.getItem(spinner.getSelectedItemPosition()).getName();

                    item.setCategory(cat);
                    item.setName(nameEdit.getText().toString());
                    item.setBrand(brandEdit.getText().toString());
                    BigDecimal bd = new BigDecimal((priceEdit.getText().toString()));
                    bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                    item.setPrice(bd.floatValue());

                    final String key = item.getKey();
                    DatabaseReference ref = key != null
                            ? DB.ITEMS.child(key)
                            : DB.ITEMS.push();

                    ref.setValue(item);

                });

        return builder.create();
    }
}
