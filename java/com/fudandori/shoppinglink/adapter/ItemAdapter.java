package com.fudandori.shoppinglink.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.R;
import com.fudandori.shoppinglink.dto.Item;
import com.fudandori.shoppinglink.dto.ItemFilter;
import com.fudandori.shoppinglink.listener.ItemClickListener;
import com.fudandori.shoppinglink.viewholder.ItemViewHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemAdapter<T extends Item> extends RecyclerView.Adapter<ItemViewHolder> {

    protected List<T> dataset;
    protected List<T> list;
    private ItemClickListener<T> longListener;

    public ItemAdapter(ItemClickListener<T> listener) {
        dataset = new ArrayList<>();
        list = new ArrayList<>();
        longListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row, parent, false);
        ItemViewHolder holder = new ItemViewHolder(v);

        if (longListener != null) {
            v.setOnLongClickListener(x -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    longListener.click(list.get(pos));
                }
                return true;
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int pos) {
        final Item i = list.get(pos);
        final String name = i.getName();
        final String cat = i.getCategory();
        final String brand = i.getBrand();
        final float price = i.getPrice();
        holder.setItem(name, cat, brand, price);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter(ItemFilter f) {
        List<T> l = new ArrayList<>(dataset);

        if (f.isName()) {
            List<T> temp = new ArrayList<>();
            for (T i : l) {
                if (i.getName().toLowerCase().contains(f.getNameValue().toLowerCase())) {
                    temp.add(i);
                }
            }

            l = temp;
        }

        if (f.isBrand()) {
            List<T> temp = new ArrayList<>();
            for (T i : l) {
                if (i.getBrand().toLowerCase().contains(f.getBrandValue().toLowerCase())) {
                    temp.add(i);
                }
            }

            l = temp;
        }

        if (!f.getCat().equals("Todas")) {
            List<T> temp = new ArrayList<>();
            for (T i : l) {
                if (i.getCategory().equals(f.getCat())) {
                    temp.add(i);
                }
            }

            l = temp;
        }

        list = l;
        notifyDataSetChanged();
    }

    protected int find(String key, List<T> list) {
        int result = -1;
        boolean found = false;

        for (int i = 0; !found && i < list.size(); i++) {
            final String cKey = list.get(i).getKey();
            found = key.equals(cKey);
            if (found) result = i;
        }

        return result;
    }

    public void add(String key, T item) {
        if (item != null) {
            item.setId(key);
            dataset.add(0, item);
            list.add(0, item);
            notifyItemInserted(0);
        }
    }

    public void update(String key, T value) {
        final int pos = find(key, dataset);
        if (pos != -1) dataset.get(pos).set(value);

        final int fPos = find(key, list);
        if (fPos != -1) {
            list.get(fPos).set(value);
            notifyItemChanged(fPos);
        }
    }

    public void delete(String key) {
        final int pos = find(key, dataset);
        if (pos != -1) dataset.remove(pos);

        final int fPos = find(key, list);
        if (fPos != -1) {
            list.remove(fPos);
            notifyItemRemoved(fPos);
        }
    }

    public void delete(int pos) {
        final String key = list.get(pos).getKey();
        delete(key);
    }

    public List<String> getCategories() {
        final Set<String> set = new HashSet<>();
        for (Item i : dataset) {
            final String cat = i.getCategory();
            if (cat != null && cat.length() > 0) {
                set.add(i.getCategory());
            }
        }

        return new ArrayList<>(set);
    }

    public T get(int pos) {
        return list.get(pos);
    }

    public void reset() {
        dataset = new ArrayList<>();
        list = new ArrayList<>();
        notifyDataSetChanged();
    }
}
