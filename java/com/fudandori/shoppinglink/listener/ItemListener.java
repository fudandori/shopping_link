package com.fudandori.shoppinglink.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fudandori.shoppinglink.adapter.ItemAdapter;
import com.fudandori.shoppinglink.dto.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class ItemListener<T extends Item, V extends ItemAdapter<T>> implements ChildEventListener {

    private V adapter;
    private final Class<T> type;

    public ItemListener(V adapter, Class<T> type) {
        this.adapter = adapter;
        this.type = type;
    }

    public void reset() {
        adapter.reset();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        final String key = dataSnapshot.getKey();
        final T value = dataSnapshot.getValue(type);

            adapter.add(key, value);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        final String key = dataSnapshot.getKey();
        final T value = dataSnapshot.getValue(type);
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
