package com.fudandori.shoppinglink.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private TextView text;

    public CategoryViewHolder(@NonNull View v) {
        super(v);

        text = v.findViewById(R.id.text);
    }

    public void set(String t) {
        text.setText(t);
    }
}
