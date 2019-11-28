package com.example.hue;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

public class VolleyManager {

    private static boolean tempResult;
    private RequestQueue queue;

    public static boolean pingUrl(String url){
        tempResult = false;
        try {
            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        tempResult = true;
                    },
                    error -> {
                        Log.d("Connection Error", error.toString());
                    }
            );
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        queue.add(request);
        return tempResult;
    }
}
