package com.example.hue.Data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hue.Data.Light;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Preset {

    private ArrayList<Light> lights;
    private Light preset;
    private String url;
    private RequestQueue queue;

    public Preset(Light preset, ArrayList<Light> lights, String url, Context context) {
        this.lights = lights;
        this.preset = preset;
        this.url = url;
        this.queue = Volley.newRequestQueue(context);
    }

    public void sendAction() {
        HashMap data = new HashMap();
        data.put("on", preset.isOn());
        data.put("bri", preset.getBrightness());
        data.put("hue",preset.getHue());
        data.put("sat", preset.getSaturation());

        JSONArray array = new JSONArray();
        array.put(data);
        Log.i("request",url + "/" + preset.getKey() + "/state");

        try {
            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url + "/" + preset.getKey() + "/state",
                    new JSONObject(data),
                    response -> {
                        Log.d("VOLLEY_REQ", response.toString());
                    },
                    error -> Log.d("VOLLEY_ERR2", error.toString())
            );
            queue.add(request);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }
}
