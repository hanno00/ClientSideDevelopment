package com.example.meetapp.Models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Person implements Serializable {

    private String name;
    private double latitude;
    private double longitude;
    private String UUID;
    private String lobbyUUID;
    private boolean isWaypoint;

    public Person (){}

    public Person(String name, String UUID, boolean isWaypoint, double latitude, double longitude) {
        this.name = name;
        this.UUID = UUID;
        this.lobbyUUID = lobbyUUID;
        this.isWaypoint = isWaypoint;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Person(String name,boolean isWaypoint) {
        this.name = name;
        this.isWaypoint = isWaypoint;
        this.UUID = java.util.UUID.randomUUID().toString();
    }

    //region getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getCoordinates() {
        return new LatLng(latitude,longitude);
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

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", UUID='" + UUID + '\'' +
                ", lobbyUUID='" + lobbyUUID + '\'' +
                ", isWaypoint=" + isWaypoint +
                '}';
    }

    //endregion
}
