package com.example.meetapp.Activities;

import android.content.Intent;
import android.os.Bundle;

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

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.meetapp.R;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Button b;
    private ArrayList<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        persons = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            persons.add(new Person("Person " + i, java.util.UUID.randomUUID().toString(), java.util.UUID.randomUUID().toString(), false));
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewAdapter(persons);

        recyclerView.setAdapter(adapter);

        b = findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

}
