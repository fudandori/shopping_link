package com.fudandori.shoppinglink.dto;

import androidx.annotation.NonNull;

import com.fudandori.shoppinglink.interfaces.Identified;

public class Item implements Identified {
    protected String id;
    protected String name;
    protected String category;
    protected String brand;
    protected float price;

    public Item() {}

    public Item(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Item(String name, String category, String brand, float price) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
    }

    public Item(String id, String name, String category, String brand, float price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
    }

    public Item(String id, Item item) {
        this.id = id;
        this.name = item.getName();
        this.category = item.getCategory();
        this.brand = item.getBrand();
        this.price = item.getPrice();
    }

    public Item(Item item) {
        this.name = item.getName();
        this.category = item.getCategory();
        this.brand = item.getBrand();
        this.price = item.getPrice();
    }

    public String getKey() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void set(Item value) {
        name = value.getName();
        brand = value.getBrand();
        category = value.getCategory();
        price = value.getPrice();
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
