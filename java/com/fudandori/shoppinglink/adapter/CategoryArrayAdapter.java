package com.fudandori.shoppinglink.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fudandori.shoppinglink.dto.Category;

import java.util.List;

public class CategoryArrayAdapter extends ArrayAdapter<Category> {


    public CategoryArrayAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(getItem(position).getName());
        return label;
    }
}
