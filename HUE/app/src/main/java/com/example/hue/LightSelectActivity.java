package com.example.hue;

import android.database.Observable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hue.Data.Bridge;
import com.example.hue.Data.Light;
import com.example.hue.RecyclerViewStuff.RecyclerViewAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class LightSelectActivity extends AppCompatActivity {

    private RequestQueue queue;
    private final String token = "jaGi5XSBOhu75gGVWHJqnG9AyXEEAfVJ7Ei-XfMd";
    private final String url = "http://192.168.1.179/api/" + token + "/lights";
    private final String fullURL = "http://192.168.1.179/api/2kRHeQYCLXt2cnrABObLUG3sC3xSmnL5etpHtEZI/lights";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ArrayList<String> testArray = new ArrayList<>(Arrays.asList("Light4","Light6","Light12","Light16","Light24","Light30"));
    private ArrayList<Light> lightArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_select);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        Bridge bridge = (Bridge) getIntent().getSerializableExtra("BRIDGE");
        System.out.println(bridge);
        lightArray = BridgeActivity.lightArrays.get(getIntent().getIntExtra("INDEX",0));

        this.queue = Volley.newRequestQueue(this);

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

        requestJson();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDataSet();
            }
        });

    }

    private void refreshDataSet() {
        requestJson();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
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
                    fullURL,
                    null,
                    response -> {
                        lightArray.clear();
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
