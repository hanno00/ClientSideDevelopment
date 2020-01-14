package com.example.meetapp.DialogBox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.meetapp.Activities.GroupActivity;
import com.example.meetapp.Data.DatabaseConnection;
import com.example.meetapp.Data.DatabaseListener;
import com.example.meetapp.Models.Lobby;
import com.example.meetapp.Models.Person;
import com.example.meetapp.R;


import java.util.List;


public class MyDialog extends Dialog implements android.view.View.OnClickListener, DatabaseListener {

    public Activity activity;
    private Button createGroup, joinGroup;
    private TextView t;
    private EditText nameField, passwordField;
    private DatabaseConnection dbConnection;

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

        dbConnection = new DatabaseConnection(this);

        nameField = findViewById(R.id.dialogName);
        passwordField = findViewById(R.id.dialogPassword);

        createGroup = findViewById(R.id.createGroupButton);
        joinGroup = findViewById(R.id.joinGroupButton);
        t = findViewById(R.id.textView3);

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameField.getText().toString();
                String password = passwordField.getText().toString();
                if (!dbConnection.checkIfLobbyExists(name,password)) {

                    Lobby l = new Lobby(name, password, java.util.UUID.randomUUID().toString());

                    dbConnection.addLobby(l.getUuid(), l);
                    SharedPreferences pref = view.getContext().getSharedPreferences("DATA", 0);
                    SharedPreferences.Editor edit = pref.edit();
                    Person p = dbConnection.getPersonByUUID(pref.getString("PERSON", ""));
                    edit.putString("LOBBY", l.getUuid());
                    edit.commit();

                    p.setlobbyUUID(l.getUuid());
                    dbConnection.updatePerson(p);

                    Intent intent = new Intent(view.getContext(), GroupActivity.class);
                    view.getContext().startActivity(intent);
                } else if (name.equals("") && password.equals("")) {
                    Toast.makeText(view.getContext(), R.string.invalid_name_or_pass, Toast.LENGTH_LONG).show();
                } else {


                    Toast.makeText(view.getContext(), R.string.groupavailability, Toast.LENGTH_LONG).show();
                }
            }
        });

        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameField.getText().toString();
                String password = passwordField.getText().toString();

                if(dbConnection.checkIfLobbyExists(name, password)) {
                    SharedPreferences pref = view.getContext().getSharedPreferences("DATA", 0);
                    SharedPreferences.Editor edit = pref.edit();

                    Person p = dbConnection.getPersonByUUID(pref.getString("PERSON",""));
                    Lobby l = dbConnection.getLobby(name, password);

                    edit.putString("LOBBY", l.getUuid());
                    edit.commit();

                    p.setlobbyUUID(l.getUuid());
                    dbConnection.updatePerson(p);

                    Intent intent = new Intent(view.getContext(), GroupActivity.class);
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), R.string.namepasswordincorrect, Toast.LENGTH_LONG).show();
                }

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

    @Override
    public void onDatabasePersonChanged() {

    }

    @Override
    public void onDatabaseLobbyChanged() {

    }

    @Override
    public void onDatabaseWaypointChanged() {

    }
}
