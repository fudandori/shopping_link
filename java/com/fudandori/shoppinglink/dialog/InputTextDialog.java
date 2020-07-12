package com.fudandori.shoppinglink.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.fudandori.shoppinglink.R;
import com.google.android.material.textfield.TextInputLayout;

public class InputTextDialog extends AppCompatDialogFragment {

    private InputTextDialogListener listener;
    private String text;
    private String label;
    private boolean number = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        final TextInputLayout t = view.findViewById(R.id.dialog_inputlayout);
        final EditText e = view.findViewById(R.id.dialog_edit_text);
        if (label == null) label = getActivity().getString(R.string.cat_name);
        e.setText(text);
        t.setHint(label);
        if (number) e.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder
                .setView(view)
                .setTitle("Edit")
                .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                })
                .setPositiveButton("OK", (DialogInterface dialog, int which) -> {
                    if (listener != null) listener.getText(e.getText().toString());
                });

        return builder.create();
    }

    public void setText(String t) {
        text = t;
    }

    public void setLabel(String l) {
        label = l;
    }

    public void number() {
        number = true;
    }

    public void setListener(InputTextDialogListener listener) {
        this.listener = listener;
    }

    public interface InputTextDialogListener {
        void getText(String text);
    }
}
