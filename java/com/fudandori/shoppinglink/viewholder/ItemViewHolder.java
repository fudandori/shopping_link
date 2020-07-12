package com.fudandori.shoppinglink.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;
    private TextView categoryTextView;
    private TextView brandTextView;
    private TextView priceTextView;
    private LinearLayout linear;
    private View view;

    public ItemViewHolder(View v) {
        super(v);
        view = v;
        nameTextView = v.findViewById(R.id.text1);
        categoryTextView = v.findViewById(R.id.text2);
        brandTextView = v.findViewById(R.id.brand);
        priceTextView = v.findViewById(R.id.price);
        linear = v.findViewById(R.id.linear);
    }

    public void set(String name, String category, String brand, float price, int quantity, int color) {
        nameTextView.setText(name);
        categoryTextView.setText(category);
        brandTextView.setText(brand);

        String value = price + "€";
        if (quantity > 1) value += " / x" + quantity;
        priceTextView.setText(value);

        linear.setBackgroundColor(color);
    }

    public void setItem(String name, String category, String brand, float price) {
        nameTextView.setText(name);
        categoryTextView.setText(category);
        brandTextView.setText(brand);
        final String priceText = price > 0f
                ? "€" + price
                : "";
        priceTextView.setText(priceText);
    }

    public View getView() {
        return view;
    }
}
