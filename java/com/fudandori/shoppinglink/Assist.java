package com.fudandori.shoppinglink;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fudandori.shoppinglink.listener.SlideListener;

import java.io.FileOutputStream;
import java.io.IOException;

public class Assist {

    public static final String CONFIG_FILE = "config";

    private Assist() {
    }

    public static ItemTouchHelper getTouchHelper(SlideListener l) {
        ItemTouchHelper.SimpleCallback impl = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int pos = viewHolder.getAdapterPosition();
                l.action(pos);
            }
        };
        return new ItemTouchHelper(impl);
    }

    public static boolean writeKey(@NonNull String key, Context context) {
        boolean success = true;
        try (FileOutputStream fos = context.openFileOutput(CONFIG_FILE, Context.MODE_PRIVATE)) {
            fos.write(key.getBytes());
        } catch (IOException ex) {
            success = false;
        }
        return success;
    }

    public static void initToolbar(ActionBar bar) {
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
    }
}
