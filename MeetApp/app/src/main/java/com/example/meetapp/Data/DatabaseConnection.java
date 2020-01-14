package com.example.meetapp.Data;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.meetapp.Models.Lobby;
import com.example.meetapp.Models.Person;
import com.example.meetapp.Models.Waypoint;
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
    private ArrayList<Waypoint> waypoints;

    private DatabaseReference dbRefLobbies;
    private DatabaseReference dbRefPersons;
    private DatabaseReference dbRefWaypoints;

    public DatabaseConnection(DatabaseListener databaseListener){
        this.databaseListener = databaseListener;

        this.lobbies = new ArrayList<>();
        this.persons = new ArrayList<>();
        this.waypoints = new ArrayList<>();

        this.dbRefLobbies = FirebaseDatabase.getInstance().getReference("lobbies");
        this.dbRefPersons = FirebaseDatabase.getInstance().getReference("persons");
        this.dbRefWaypoints = FirebaseDatabase.getInstance().getReference("waypoints");

        attachListeners();
    }

    public void insertWaypoint(Waypoint waypoint){
        if (checkIfLobbyHasWaypoint(waypoint.getLobbyUUID())) {
            removeWaypoint(waypoint.getLobbyUUID());
        }
        dbRefWaypoints.child(waypoint.getUUID()).setValue(waypoint);
    }

    public boolean checkIfLobbyHasWaypoint(String lobbyUUID){
        for (Waypoint wp : waypoints) {
            if (wp.getLobbyUUID().equals(lobbyUUID)){
                return true;
            }
        }
        return false;
    }

    public Waypoint getWaypointByUUID(String lobbyUUID){
        for (Waypoint wp : waypoints) {
            if (wp.getLobbyUUID().equals(lobbyUUID)) {
                return wp;
            }
        }
        return null;
    }

    public Person getPersonByUUID(String id){
        for (Person p : persons) {
            if (p.getUUID().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Lobby getLobbyByUUID(String id){
        for (Lobby l : lobbies) {
            if (l.getUuid().equals(id)) {
                return l;
            }
        }
        return null;
    }

    public void addLobby(String id, Lobby lobby){
        dbRefLobbies.child(id).setValue(lobby);
    }

    public void updatePerson(Person person){
        dbRefPersons.child(person.getUUID()).setValue(person);
    }

    public void removeWaypoint(String lobbyUUID){
        for (Waypoint wp : waypoints) {
            if (wp.getLobbyUUID().equals(lobbyUUID)){
                dbRefWaypoints.child(wp.getUUID()).removeValue();
            }
        }
    }

    public void removeLobby(String name) {
        for (Lobby l : lobbies) {
            if (l.getName().equals(name)) {
                removeWaypoint(l.getUuid());
                dbRefLobbies.child(l.getUuid()).removeValue();
            }
        }
    }

    public void removeLobbyByUUID(String uuid) {
        for(Lobby l : lobbies) {
            if (l.getUuid().equals(uuid)) {
                removeWaypoint(uuid);
                dbRefLobbies.child(l.getUuid()).removeValue();
            }
        }
    }

    public void removePerson(Person person) {
        dbRefPersons.child(person.getUUID()).removeValue();
    }

    public ArrayList<Person> getPersonsByLobbyUUID(String lobbyUUID){
        ArrayList<Person> persons = new ArrayList<>();
        for (Person person : this.persons) {
            if (person.getlobbyUUID() != null) {
                if (person.getlobbyUUID().equals(lobbyUUID)) {
                    persons.add(person);
                }
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

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<Waypoint> waypoints) {
        this.waypoints = waypoints;
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
                databaseListener.onDatabaseLobbyChanged();
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
                databaseListener.onDatabasePersonChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbRefWaypoints.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                waypoints.clear();
                for (DataSnapshot node : dataSnapshot.getChildren()) {
                    Waypoint waypoint = node.getValue(Waypoint.class);
                    waypoints.add(waypoint);
                }
                databaseListener.onDatabaseWaypointChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //endregion
}
