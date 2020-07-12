package com.fudandori.shoppinglink.dto;

public class ShopItem extends Item {

    private int quantity = 0;
    private boolean strike = false;

    public ShopItem() {
    }

    public ShopItem(String key, Item item) {
        super(key, item);
    }

    public ShopItem(String name, String category) {
        super(name, category);
    }

    public ShopItem(String id, ShopItem s) {
        this.id = id;
        name = s.getName();
        category = s.getCategory();
        brand = s.getCategory();
        price = s.getPrice();
        quantity = s.getQuantity();
        strike = s.isStrike();
    }

    public ShopItem(Item item) {
        super(item);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStrike() {
        return strike;
    }

    public void setStrike(boolean strike) {
        this.strike = strike;
    }

    public void set(ShopItem value) {
        name = value.getName();
        brand = value.getBrand();
        category = value.getCategory();
        price = value.getPrice();
        quantity = value.getQuantity();
        strike = value.isStrike();
    }
}
