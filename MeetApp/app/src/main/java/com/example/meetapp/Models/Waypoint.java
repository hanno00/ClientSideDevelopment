package com.example.meetapp.Models;

import java.io.Serializable;

public class Waypoint implements Serializable {
    private String name;
    private double latitude;
    private double longitude;
    private String lobbyUUID;
    private String UUID;

    public Waypoint (){}

    public Waypoint(String name, double latitude, double longitude, String lobbyUUID) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lobbyUUID = lobbyUUID;
        this.UUID = java.util.UUID.randomUUID().toString();
    }

    //region getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLobbyUUID() {
        return lobbyUUID;
    }

    public void setLobbyUUID(String lobbyUUID) {
        this.lobbyUUID = lobbyUUID;
    }

    public String getUUID() {

        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    //endregion
}
