package com.example.meetapp.Models;

import com.google.android.gms.maps.model.LatLng;

public class Person {

    private String name;
    private LatLng coordinates;
    private String UUID;
    private String lobbyUUID;
    private boolean isWaypoint;

    public Person(String name, String UUID, String lobbyUUID, boolean isWaypoint) {
        this.name = name;
        this.UUID = UUID;
        this.lobbyUUID = lobbyUUID;
        this.isWaypoint = isWaypoint;
    }

    //region getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getlobbyUUID() {
        return lobbyUUID;
    }

    public void setlobbyUUID(String lobbyId) {
        this.lobbyUUID = lobbyId;
    }

    public boolean isWaypoint() {
        return isWaypoint;
    }

    public void setWaypoint(boolean waypoint) {
        isWaypoint = waypoint;
    }

    //endregion
}
