package com.fudandori.shoppinglink.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.R;
import com.fudandori.shoppinglink.dto.Category;
import com.fudandori.shoppinglink.listener.CatClickListener;
import com.fudandori.shoppinglink.viewholder.CategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> implements Filterable {

    private List<Category> dataset;
    private List<Category> list;
    private CatClickListener longListener;
    private String cache = null;

    public CategoryAdapter() {
        dataset = new ArrayList<>();
        list = new ArrayList<>();
    }

    public void setLongListener(CatClickListener longListener) {
        this.longListener = longListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_cat, parent, false);
        CategoryViewHolder holder = new CategoryViewHolder(v);

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
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final Category cat = list.get(position);
        holder.set(cat.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return f;
    }

    private Filter f = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Category> l = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                cache = null;
                l = dataset;
            } else {
                cache = constraint.toString();
                for (Category c : dataset) {
                    if (c.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        l.add(c);
                    }
                }
            }

            FilterResults result = new FilterResults();
            result.values = l;
            return result;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (List) results.values;
            notifyDataSetChanged();
        }
    };

    public void add(Category c) {
        dataset.add(c);
        if(cache == null || c.getName().toLowerCase().contains(cache)) {
            list.add(c);
            notifyItemInserted(list.size() - 1);
        }
    }

    public void update(String key, String value) {
        final int pos = find(key, dataset);
        final int fPos = find(key, list);
        if (pos != -1) {
            dataset.get(pos).setName(value);
        }

        if(fPos != -1) {
            list.get(fPos).setName(value);
            notifyItemChanged(fPos);
        }
    }

    public void delete(String key) {
        final int pos = find(key, dataset);
        final int fPos = find(key, list);
        if (pos != -1) {
            dataset.remove(pos);
        }

        if(fPos != -1) {
            list.remove(fPos);
            notifyItemRemoved(fPos);
            notifyItemRangeChanged(fPos, list.size() - 1);
        }
    }

    private int find(String key, List<Category> list) {
        int result = -1;
        boolean found = false;

        for (int i = 0; !found && i < list.size(); i++) {
            final String cKey = list.get(i).getKey();
            found = key.equals(cKey);
            if (found) result = i;
        }

        return result;
    }

    public boolean exists(String input) {
        boolean exists = false;
        for (int i = 0; i < dataset.size() && !exists; i++) {
            exists = dataset.get(i).getName().equalsIgnoreCase(input);
        }

        return exists;
    }

    public String getItemKey(int pos) {
        return list.get(pos).getKey();
    }
}
