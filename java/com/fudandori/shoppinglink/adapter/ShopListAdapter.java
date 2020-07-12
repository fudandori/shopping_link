package com.fudandori.shoppinglink.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.DB;
import com.fudandori.shoppinglink.dto.ShopItem;
import com.fudandori.shoppinglink.listener.ItemClickListener;
import com.fudandori.shoppinglink.listener.RefreshListener;
import com.fudandori.shoppinglink.viewholder.ItemViewHolder;

public class ShopListAdapter extends ItemAdapter<ShopItem> {

    private ItemClickListener<ShopItem> listener;
    private RefreshListener refresh;
    private boolean recolor = true;

    public ShopListAdapter(ItemClickListener<ShopItem> click, ItemClickListener<ShopItem> longClick, RefreshListener refresh) {
        super(longClick);
        listener = click;
        this.refresh = refresh;
    }

    public ShopListAdapter(ItemClickListener<ShopItem> longClick) {
        super(longClick);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewHolder holder = super.onCreateViewHolder(parent, viewType);

        if (listener != null) {
            holder.getView().setOnClickListener(x -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.click(list.get(pos));
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ShopItem item = list.get(position);

        int color = 0xff8bc34a;
        if (recolor && item.isStrike()) color = 0xff90a4ae;
        holder.set(item.getName(), item.getCategory(), item.getBrand(), item.getPrice(), item.getQuantity(), color);
    }

    @Override
    public void add(String key, ShopItem item) {
        if (item != null) {
            item.setId(key);
            if (item.isStrike()) {
                dataset.add(item);
                list.add(item);
                notifyItemInserted(list.size() - 1);
            } else {
                dataset.add(0, item);
                list.add(0, item);
                notifyItemInserted(0);
            }
        }
        recount();
    }

    @Override
    public void update(String key, ShopItem value) {
        if (value != null) {
            super.delete(key);
            ShopItem s = new ShopItem(key, value);
            if (value.isStrike()) {
                dataset.add(s);
                list.add(s);
                notifyItemInserted(list.size() - 1);
            } else {
                dataset.add(0, s);
                list.add(0, s);
                notifyItemInserted(0);
            }

            recount();
        }
    }


    @Override
    public void delete(String key) {
        super.delete(key);
        recount();
    }

    private void recount() {
        if (refresh != null) {
            float total = 0f;
            float bought = 0f;
            for (ShopItem item : list) {
                final float value = item.getPrice() * item.getQuantity();
                total += value;
                if (item.isStrike()) bought += value;
            }

            refresh.recount(total, bought);
        }
    }

    public void disableRecolor() {
        recolor = false;
    }

    public void upload() {
        DB.CART.removeValue();

        for (ShopItem i : dataset) {
            DB.CART
                    .push()
                    .setValue(i);
        }
    }

    public void remove(int pos) {
        final String id = list.get(pos).getKey();
        DB.CART
                .child(id)
                .removeValue();
    }

    public void updateQuantity(String key, int q) {
        final int pos = find(key, dataset);
        if (pos != -1) dataset.get(pos).setQuantity(q);

        final int fPos = find(key, list);
        if (fPos != -1) {
            list.get(fPos).setQuantity(q);
            notifyItemChanged(fPos);
        }
    }
}
