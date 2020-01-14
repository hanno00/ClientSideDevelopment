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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.meetapp.Activities.GroupActivity;
import com.example.meetapp.Models.Lobby;
import com.example.meetapp.Models.Person;
import com.example.meetapp.R;


import java.util.List;


public class MyDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity activity;
    private Button createGroup, joinGroup;
    private TextView t;
    private EditText nameField, passwordField;

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

        nameField = findViewById(R.id.dialogName);
        passwordField = findViewById(R.id.dialogPassword);

        createGroup = findViewById(R.id.createGroupButton);
        joinGroup = findViewById(R.id.joinGroupButton);
        t = findViewById(R.id.textView3);

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add new lobby to firebase database
                String name = nameField.getText().toString();
                String password = passwordField.getText().toString();
                


                Intent intent = new Intent(view.getContext(), GroupActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        joinGroup.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
