package com.example.meetapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetapp.Data.DatabaseConnection;
import com.example.meetapp.Data.DatabaseListener;
import com.example.meetapp.Models.Lobby;
import com.example.meetapp.Models.Person;
import com.example.meetapp.R;
import com.example.meetapp.Student;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements DatabaseListener {

    private EditText nameText;
    private EditText classText;
    private EditText class2Text;
    private EditText name2Text;
    private EditText name3Text;
    private Button button;

    private Person person;

    private ArrayList<Lobby> lobbies = new ArrayList<>();
    private SharedPreferences pref;

    private DatabaseConnection databaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseConnection = new DatabaseConnection(this);
        pref = getApplicationContext().getSharedPreferences("DATA", 0); // 0 - for private mode

        nameText = findViewById(R.id.editText1);
        classText = findViewById(R.id.editText2);
        class2Text = findViewById(R.id.editText5);
        name2Text = findViewById(R.id.editText3);
        name3Text = findViewById(R.id.editText4);
        button = findViewById(R.id.buttonAdd);
    }

    public void addBtnClick(View view) {
        addLobby();
    }

    public void addLobby(){
        String name = nameText.getText().toString();
        String password = classText.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {

            String uniqueID = UUID.randomUUID().toString();
            databaseConnection.addLobby(uniqueID,new Lobby(name,password,uniqueID));

            nameText.setText("");
            classText.setText("");

        } else {
            Toast.makeText(this,"Naampje invullen pls", Toast.LENGTH_LONG).show();
        }

    }
    public void removeBtnClick(View view) {
        removeLobby();
    }

    public void removeLobby() {
        databaseConnection.removeLobby(name2Text.getText().toString());
        name2Text.setText("");
        Toast.makeText(this,lobbies.toString(), Toast.LENGTH_LONG).show();
    }

    public void editBtnClick(View view) {
        for (Lobby l : databaseConnection.getLobbies()) {
            if (l.getName().equals(name3Text.getText().toString()) && l.getPassword().equals(class2Text.getText().toString())) {
                Toast.makeText(this,"Ingelogd bij:" + l.toString(), Toast.LENGTH_LONG).show();

                SharedPreferences pref = getApplicationContext().getSharedPreferences("DATA",0);
                SharedPreferences.Editor editor = pref.edit();

                person.setlobbyUUID(l.getUuid());
                databaseConnection.updatePerson(person);

                editor.putString("PERSON", person.getUUID());
                editor.putString("LOBBY", l.getUuid());
                editor.commit();

                Intent intent = new Intent(this, GroupActivity.class);

                startActivity(intent);

            }
        }
        name3Text.setText("");
        class2Text.setText("");
    }

    @Override
    public void onDatabasePersonChanged() {
        person = databaseConnection.getPersonByUUID(pref.getString("PERSON",""));
    }

    @Override
    public void onDatabaseLobbyChanged() {

    }

    @Override
    public void onDatabaseWaypointChanged() {

    }
}
