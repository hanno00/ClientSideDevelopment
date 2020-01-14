package com.example.meetapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetapp.Data.DatabaseConnection;
import com.example.meetapp.Data.DatabaseListener;
import com.example.meetapp.DialogBox.MyDialog;
import com.example.meetapp.Models.Person;
import com.example.meetapp.R;

public class LoginActivity extends AppCompatActivity implements DatabaseListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private EditText usernameText;
    private Button b;
    private EditText nameField, passwordField;
    private MyDialog myDialog;
    private DatabaseConnection databaseConnection;
    final SharedPreferences pref = getApplicationContext().getSharedPreferences("DATA", 0); // 0 - for private mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(android.R.style.Theme_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameText = findViewById(R.id.nameEditText);

        databaseConnection = new DatabaseConnection(this);

        b = findViewById(R.id.button);
        myDialog = new MyDialog(this);
        nameField = findViewById(R.id.nameEditText);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!pref.getString("PERSON","empty").equals("empty")) {
            //HIER komt tie als je een person hebt

            if (!pref.getString("LOBBY","empty").equals("empty")) {
                //HIER KOMT TIE ALS JE GEEN GROEP HEBT
                Intent intent = new Intent(this,GroupActivity.class);
                startActivity(intent);
            }
            myDialog.show();
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nameField.getText().toString().equals("")) {

                    SharedPreferences.Editor editor = pref.edit();

                    Person person = new Person(usernameText.getText().toString(), false);

                    databaseConnection.updatePerson(person);

                    editor.putString("PERSON", person.getUUID());
                    editor.commit();

                    myDialog.show();
                }
            }
        });
    }

    public void askPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, R.string.locationPermissionNotAccepted, Toast.LENGTH_LONG).show();
            }
        }
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
