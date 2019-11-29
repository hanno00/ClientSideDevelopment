package com.example.hue;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hue.RecyclerViewStuff.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class LightSelectActivity extends AppCompatActivity {

    private RequestQueue queue;
    private final String token = "2kRHeQYCLXt2cnrABObLUG3sC3xSmnL5etpHtEZI";
    private final String url = "http://192.168.1.179/api/" + token + "/lights";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> testArray = new ArrayList<>(Arrays.asList("Light4","Light6","Light12","Light16","Light24","Light30"));
    private ArrayList<Light> lightArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_select);

        this.queue = Volley.newRequestQueue(this);
        //requestJson();
        //lightArray = generateRandomLights();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        mAdapter = new RecyclerViewAdapter(lightArray);
        recyclerView.setAdapter(mAdapter);

        new Thread(() -> {
            requestJson();
            mAdapter.notifyDataSetChanged();
        }).start();
    }

    private ArrayList<Light> generateRandomLights() {
        ArrayList<Light> lightsArr = new ArrayList<>();
        Random r = new Random();
        for (int q = 0; q <= 10; q++) {
            lightsArr.add(new Light(r.nextInt(360), r.nextInt(65535), r.nextInt(360), "lol", "Light " + q, true));
        }
        return lightsArr;
    }


    public void requestJson() {
        try {
            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        Log.d("VOLLEY_REQ", response.toString());
                        String tekst = "";
                        Iterator<String> iter = response.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                                Light light = new Light((JSONObject) response.get(key), key);
                                lightArray.add(light);
                                tekst += "\n" + light.toString();
                            } catch (JSONException e) {
                                Log.d("VOLLEY_ERR1", e.toString());
                            }
                        }
                        //textView.setText(tekst);
                    },
                    error -> Log.d("VOLLEY_ERRREquest", error.toString())
            );

            queue.add(request);

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }
}
