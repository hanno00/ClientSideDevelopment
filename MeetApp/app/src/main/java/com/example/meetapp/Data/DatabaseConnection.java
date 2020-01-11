package com.example.meetapp.Data;

import androidx.annotation.NonNull;

import com.example.meetapp.Models.Lobby;
import com.example.meetapp.Models.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseConnection {

    private DatabaseListener databaseListener;

    private ArrayList<Lobby> lobbies;
    private ArrayList<Person> persons;

    private DatabaseReference dbRefLobbies;
    private DatabaseReference dbRefPersons;

    public DatabaseConnection(DatabaseListener databaseListener){
        this.databaseListener = databaseListener;

        this.lobbies = new ArrayList<>();
        this.persons = new ArrayList<>();

        this.dbRefLobbies = FirebaseDatabase.getInstance().getReference("lobbies");
        this.dbRefPersons = FirebaseDatabase.getInstance().getReference("persons");

        attachListeners();
    }

    public void updatePerson(Person person){
        dbRefPersons.child(person.getUUID()).setValue(person);
    }

    public ArrayList<Person> getPersonsByLobbyUUID(String lobbyUUID){
        ArrayList<Person> persons = new ArrayList<>();
        for (Person person : this.persons) {
            if (person.getlobbyUUID().equals(lobbyUUID)) {
                persons.add(person);
            }
        }
        return persons;
    }

    public boolean checkIfLobbyExists(String name, String password){
        for (Lobby lobby : lobbies) {
            if (lobby.getPassword().equals(password) && lobby.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Lobby getLobby(String name, String password) {
        for (Lobby lobby : lobbies) {
            if (lobby.getPassword().equals(password) && lobby.getName().equals(name)) {
                return lobby;
            }
        }
        return null;
    }

    public ArrayList<Person> getPersons(){
        return this.persons;
    }

    public ArrayList<Lobby> getLobbies(){
        return this.lobbies;
    }

    //region listener
    private void attachListeners(){
        dbRefLobbies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lobbies.clear();
                for (DataSnapshot node : dataSnapshot.getChildren()) {
                    Lobby lobby = node.getValue(Lobby.class);
                    lobbies.add(lobby);
                }
                databaseListener.onDatabaseChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbRefPersons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                persons.clear();
                for (DataSnapshot node : dataSnapshot.getChildren()) {
                    Person person = node.getValue(Person.class);
                    persons.add(person);
                }
                databaseListener.onDatabaseChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //endregion
}
