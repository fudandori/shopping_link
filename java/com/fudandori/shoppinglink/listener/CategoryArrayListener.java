package com.fudandori.shoppinglink.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fudandori.shoppinglink.adapter.CategoryArrayAdapter;
import com.fudandori.shoppinglink.dto.Category;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class CategoryArrayListener implements ChildEventListener {

    private CategoryArrayAdapter adapter;

    public CategoryArrayListener(CategoryArrayAdapter adapter) {
        this.adapter = adapter;
    }

    private int find(String key) {
        int result = -1;
        boolean found = false;

        for (int i = 0; !found && i < adapter.getCount(); i++) {
            final String cKey = adapter.getItem(i).getKey();
            found = key.equals(cKey);
            if (found) result = i;
        }

        return result;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        String value = dataSnapshot.getValue(String.class);
        String key = dataSnapshot.getKey();
        adapter.add(new Category(key, value));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        final String key = dataSnapshot.getKey();
        final int position = find(key);

        if (position != -1) {
            final String value = dataSnapshot.getValue(String.class);
            adapter.getItem(position).setName(value);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        final String key = dataSnapshot.getKey();
        final int position = find(key);

        if (position != -1) {
            adapter.remove(adapter.getItem(position));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {}
}
