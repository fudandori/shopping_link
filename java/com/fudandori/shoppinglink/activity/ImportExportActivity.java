package com.fudandori.shoppinglink.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fudandori.shoppinglink.Assist;
import com.fudandori.shoppinglink.DB;
import com.fudandori.shoppinglink.R;

public class ImportExportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import_export);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) Assist.initToolbar(getSupportActionBar());
        final TextView key = findViewById(R.id.code);
        key.setText(DB.getKey());

        final EditText e = findViewById(R.id.import_edit);
        final Button b = findViewById(R.id.import_button);
        b.setEnabled(false);
        b.setOnClickListener(x -> {
            String newKey = e.getText().toString();
            if (Assist.writeKey(newKey, getApplicationContext())) DB.setRoot(newKey);
            DB.getCartListener().reset();
            finish();
        });
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                b.setEnabled(s.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
