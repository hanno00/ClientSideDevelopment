package com.example.hue.Data;

import java.io.Serializable;

public class Bridge implements Serializable {
    private String url;
    private String name;
    private boolean isAvailable;

    public Bridge(String url, String name) {
        this.url = url;
        this.name = name;
        this.isAvailable = false;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
