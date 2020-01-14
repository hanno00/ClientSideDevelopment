package com.example.meetapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.meetapp.Data.DatabaseConnection;
import com.example.meetapp.Data.DatabaseListener;
import com.example.meetapp.Models.Lobby;
import com.example.meetapp.Models.Person;
import com.example.meetapp.RecyclerViewStuff.RecyclerViewAdapter;
import com.example.meetapp.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.meetapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity implements DatabaseListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Person> persons = new ArrayList<>();

    private Lobby lobby;
    private Person me;

    private DatabaseConnection databaseConnection;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        databaseConnection = new DatabaseConnection(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("DATA", 0); // 0 - for private mode
        me = gson.fromJson(pref.getString("PERSON",""), Person.class);
        lobby = gson.fromJson(pref.getString("LOBBY",""), Lobby.class);

        TextView textView = findViewById(R.id.textViewLobby);
        textView.setText(lobby.getUuid());

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
    }

    @Override
    public void onDatabaseChanged() {
        persons = databaseConnection.getPersonsByLobbyUUID(lobby.getUuid());
        adapter = new RecyclerViewAdapter(persons);

        recyclerView.setAdapter(adapter);
    }
}
