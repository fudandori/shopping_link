package com.fudandori.shoppinglink.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.R;
import com.fudandori.shoppinglink.view.StrikeTextView;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private StrikeTextView nameTextView;
    private TextView categoryTextView;
    private TextView brandTextView;
    private TextView priceTextView;
    private ConstraintLayout parent;
    private View view;

    public ItemViewHolder(View v) {
        super(v);
        view = v;
        nameTextView = v.findViewById(R.id.text1);
        categoryTextView = v.findViewById(R.id.text2);
        brandTextView = v.findViewById(R.id.brand);
        priceTextView = v.findViewById(R.id.price);
        parent = v.findViewById(R.id.parent);
    }

    public void set(String name, String category, String brand, float price, int quantity, boolean strike) {
        nameTextView.setText(name);
        categoryTextView.setText(category);
        brandTextView.setText(brand);

        String value = price + "€";
        if (quantity > 1) value += " / x" + quantity;
        priceTextView.setText(value);

            nameTextView.strike(strike);
        parent.setBackgroundColor(strike ? 0x77abcabc : 0x77000000);

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
