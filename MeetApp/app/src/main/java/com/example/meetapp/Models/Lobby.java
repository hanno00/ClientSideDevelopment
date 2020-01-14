package com.example.meetapp.Models;

import java.io.Serializable;

public class Lobby implements Serializable {

    private String name;
    private String password;
    private String uuid;

    public Lobby(){}

    public Lobby(String name, String password, String uuid) {
        this.name = name;
        this.password = password;
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Lobby{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    //region getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    //endregion
}
