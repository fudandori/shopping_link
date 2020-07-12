package com.fudandori.shoppinglink.listener;

import com.fudandori.shoppinglink.dto.Item;

public interface ItemClickListener<T extends Item> {
    void click(T item);
}
