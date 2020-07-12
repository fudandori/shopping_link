package com.fudandori.shoppinglink.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.fudandori.shoppinglink.R;
import com.fudandori.shoppinglink.dto.Category;
import com.fudandori.shoppinglink.dto.ItemFilter;
import com.fudandori.shoppinglink.listener.ItemFilterDialogListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class FilterDialog extends AppCompatDialogFragment {

    private static final Category ALL = new Category(null, "Todas");

    private boolean filterName;
    private boolean filterBrand;
    private String name;
    private String brand;
    private ItemFilterDialogListener listener;
    private List<String> list;
    private String cat;

    public FilterDialog(ItemFilter filter, List<String> list, ItemFilterDialogListener listener) {
        this.filterName = filter.isName();
        this.filterBrand = filter.isBrand();
        this.name = filter.getNameValue();
        this.brand = filter.getBrandValue();
        this.cat = filter.getCat();
        this.list = list;
        list.add(0, "Todas");
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filters, null);

        SwitchMaterial nameSwitch = view.findViewById(R.id.switch_name);
        SwitchMaterial brandSwitch = view.findViewById(R.id.switch_brand);

        nameSwitch.setChecked(filterName);
        brandSwitch.setChecked(filterBrand);

        TextInputEditText nameEdit = view.findViewById(R.id.filter_name_input);
        nameEdit.setEnabled(filterName);
        nameEdit.setText(name);
        nameEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        nameSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            nameEdit.setEnabled(isChecked);
            if (isChecked) {
                nameEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(nameEdit, InputMethodManager.SHOW_IMPLICIT);
            } else {
                nameEdit.clearFocus();
            }
            nameEdit.setText("");
        });

        TextInputEditText brandEdit = view.findViewById(R.id.filter_brand_input);
        brandEdit.setEnabled(filterBrand);
        brandEdit.setText(brand);
        nameEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        brandSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            brandEdit.setEnabled(isChecked);
            if (isChecked) {
                brandEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(brandEdit, InputMethodManager.SHOW_IMPLICIT);
            } else {
                brandEdit.clearFocus();
            }
            brandEdit.setText("");
        });

        Spinner s = view.findViewById(R.id.spinner_cat);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        spinnerArrayAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(spinnerArrayAdapter);

        if (cat != null && cat.length() > 0) {
            boolean found = false;
            for (int i = 0; i < list.size() && !found; i++) {
                final String actual = list.get(i);
                found = actual.equalsIgnoreCase(cat);
                if (found) s.setSelection(i);
            }
        }

        builder
                .setView(view)
                .setTitle("Test")

                .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                    // Do nothing
                })
                .setPositiveButton("OK", (DialogInterface dialog, int which) -> {
                    ItemFilter f = new ItemFilter();
                    f.setName(nameSwitch.isChecked());
                    f.setBrand(brandSwitch.isChecked());
                    f.setNameValue(nameEdit.getText().toString());
                    f.setBrandValue(brandEdit.getText().toString());
                    final int pos = s.getSelectedItemPosition();
                    f.setCat(list.get(pos));
                    listener.getFilter(f);
                });

        return builder.create();
    }
}
