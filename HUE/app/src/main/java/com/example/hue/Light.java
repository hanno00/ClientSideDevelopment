package com.example.hue;

import org.json.JSONException;
import org.json.JSONObject;

public class Light {

    JSONObject state;

    private boolean on;
    private int brightness;
    private int hue;
    private int saturation;
    private String effect;
    private boolean reachable;
    private String type;
    private String name;
    private String modelid;
    private String key;

    public Light(JSONObject jo, String keyID) throws JSONException {
        this.key = keyID;
        this.state = jo.getJSONObject("state");
        this.on = state.getBoolean("on");
        this.brightness = state.getInt("bri");
        this.hue = state.getInt("hue");
        this.saturation = state.getInt("sat");
        this.effect = state.getString("effect");
        this.reachable = state.getBoolean("reachable");
        this.type = jo.getString("type");
        this.name = jo.getString("name");
        this.modelid = jo.getString("modelid");

    }

    @Override
    public String toString() {
        return "Name: " + this.name +
                "\nOn: " + this.on +
                "\nHue: " + this.hue +
                "\nBrightness: " + this.brightness +
                "\nSaturation: " + this.saturation;
    }

    //region Getters and Setters

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JSONObject getState() {
        return state;
    }

    public void setState(JSONObject state) {
        this.state = state;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getHue() {
        return hue;
    }

    public void setHue(int hue) {
        this.hue = hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }
    //endregion
}