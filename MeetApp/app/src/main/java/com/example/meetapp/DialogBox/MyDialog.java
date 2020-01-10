package com.example.meetapp.DialogBox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.meetapp.Activities.GroupActivity;
import com.example.meetapp.R;


import java.util.List;


public class MyDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity activity;
    private Button b1, b2;
    private TextView t;

    public MyDialog() {
        super(null);
    }

    public MyDialog(Activity activity) {

        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_dialog);

        b1 = findViewById(R.id.createGroupButton);
        b2 = findViewById(R.id.joinGroupButton);
        t = findViewById(R.id.textView3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add new lobby to firebase database

                Intent intent = new Intent(view.getContext(), GroupActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO: enter the uuid of the desired lobby and join it.

                Intent intent = new Intent(view.getContext(), GroupActivity.class);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
