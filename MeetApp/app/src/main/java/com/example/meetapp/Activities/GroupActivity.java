package com.example.meetapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetapp.Data.DatabaseConnection;
import com.example.meetapp.Data.DatabaseListener;
import com.example.meetapp.DialogBox.MyDialog;
import com.example.meetapp.Models.Lobby;
import com.example.meetapp.Models.Person;
import com.example.meetapp.R;
import com.example.meetapp.RecyclerViewStuff.RecyclerViewAdapter;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity implements DatabaseListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView textView;
    private ArrayList<Person> persons;
    private Lobby lobby;
    private SharedPreferences pref;

    private String lobbyUUID;
    private DatabaseConnection databaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        databaseConnection = new DatabaseConnection(this);

        pref = getApplicationContext().getSharedPreferences("DATA", 0);
        lobbyUUID = pref.getString("LOBBY","");

        Lobby l = databaseConnection.getLobbyByUUID(lobbyUUID);

        textView = findViewById(R.id.textViewLobby);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pref = getApplicationContext().getSharedPreferences("DATA", 0); // 0 - for private mode
                Person p = databaseConnection.getPersonByUUID(pref.getString("PERSON", ""));
                p.setlobbyUUID("");
                databaseConnection.updatePerson(p);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("LOBBY","empty");
                editor.commit();

                startActivity(new Intent(view.getContext(),LoginActivity.class));
            }
        });

        findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = getApplicationContext().getSharedPreferences("DATA", 0); // 0 - for private mode
                Person p = databaseConnection.getPersonByUUID(pref.getString("PERSON", ""));
                p.setlobbyUUID("");
                databaseConnection.removePerson(p);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("LOBBY","empty");
                editor.putString("PERSON","empty");
                editor.commit();

                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDatabasePersonChanged() {
        persons = databaseConnection.getPersonsByLobbyUUID(lobbyUUID);
        adapter = new RecyclerViewAdapter(persons);

        recyclerView.setAdapter(adapter);



        if(databaseConnection.getPersonsByLobbyUUID(lobbyUUID).size() == 0) {
            databaseConnection.removeLobbyByUUID(lobbyUUID);
        }



        if(databaseConnection.getPersonByUUID(pref.getString("PERSON", "")).getlobbyUUID().equals("")) {
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("LOBBY", "empty");
            edit.commit();

            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onDatabaseLobbyChanged() {
        if(lobbyUUID != null)
            textView.setText(databaseConnection.getLobbyByUUID(lobbyUUID).getName());
    }

    @Override
    public void onDatabaseWaypointChanged() {

    }
}
