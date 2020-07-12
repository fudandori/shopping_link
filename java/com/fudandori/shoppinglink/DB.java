package com.fudandori.shoppinglink;

import com.fudandori.shoppinglink.adapter.ItemAdapter;
import com.fudandori.shoppinglink.dto.ShopItem;
import com.fudandori.shoppinglink.listener.ItemListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DB {

    private DB() {
    }

    public static DatabaseReference CATEGORIES;
    public static DatabaseReference ITEMS;
    public static DatabaseReference CART;

    private static ItemListener<ShopItem, ItemAdapter<ShopItem>> cartListener;

    private static String key;

    public static void setRoot(String input) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(input);
        CATEGORIES = root.child("categories");
        ITEMS = root.child("items");
        CART = root.child("cart");

        if (cartListener != null) CART.addChildEventListener(cartListener);
        key = input;
    }

    public static ItemListener<ShopItem, ItemAdapter<ShopItem>> getCartListener() {
        return cartListener;
    }

    public static void setCartListener(ItemListener<ShopItem, ItemAdapter<ShopItem>> cartListener) {
        DB.cartListener = cartListener;
        CART.addChildEventListener(cartListener);
    }

    public static String getKey() {
        return key;
    }
}
