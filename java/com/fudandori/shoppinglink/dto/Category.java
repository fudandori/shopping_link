package com.fudandori.shoppinglink.dto;

import androidx.annotation.NonNull;

import com.fudandori.shoppinglink.interfaces.Identified;

public class Category implements Identified {
    private String key;
    private String name;

    public Category(String id, String name) {
        this.key = id;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
