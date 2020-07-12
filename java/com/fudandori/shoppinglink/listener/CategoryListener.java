package com.fudandori.shoppinglink.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fudandori.shoppinglink.adapter.CategoryAdapter;
import com.fudandori.shoppinglink.dto.Category;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class CategoryListener implements ChildEventListener {

    final CategoryAdapter adapter;

    public CategoryListener(CategoryAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        final String value = dataSnapshot.getValue(String.class);
        final String key = dataSnapshot.getKey();
        adapter.add(new Category(key, value));
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        final String key = dataSnapshot.getKey();
        final String value = dataSnapshot.getValue(String.class);
        adapter.update(key, value);

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        final String key = dataSnapshot.getKey();
        adapter.delete(key);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
