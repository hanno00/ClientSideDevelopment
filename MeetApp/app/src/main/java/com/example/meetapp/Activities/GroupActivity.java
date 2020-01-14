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

    private String lobbyUUID;
    private DatabaseConnection databaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        databaseConnection = new DatabaseConnection(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("DATA", 0); // 0 - for private mode
//        me = databaseConnection.getPersonByUUID(pref.getString("PERSON",""));


        lobbyUUID = pref.getString("LOBBY","");

        Lobby l = databaseConnection.getLobbyByUUID(lobbyUUID);

        TextView textView = findViewById(R.id.textViewLobby);
        textView.setText(lobbyUUID);

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

                SharedPreferences pref = getApplicationContext().getSharedPreferences("DATA", 0); // 0 - for private mode
                Person p = databaseConnection.getPersonByUUID(pref.getString("PERSON", ""));
                p.setlobbyUUID("");
                databaseConnection.updatePerson(p);

                new MyDialog((Activity) view.getContext()).show();
            }
        });
    }

    @Override
    public void onDatabasePersonChanged() {
        persons = databaseConnection.getPersonsByLobbyUUID(lobbyUUID);
        adapter = new RecyclerViewAdapter(persons);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDatabaseLobbyChanged() {

    }
}
