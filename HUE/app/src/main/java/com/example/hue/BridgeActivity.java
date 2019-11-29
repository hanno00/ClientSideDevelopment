package com.example.hue;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hue.Data.Bridge;
import com.example.hue.Data.Light;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class BridgeActivity extends AppCompatActivity {

    private RequestQueue queue;

    private TextView tvEmulator;
    private TextView tvHUE;

    private boolean emulatorIsOnline;
    private boolean HUEIsOnline;

    public static ArrayList<ArrayList<Light>> lightArrays = new ArrayList<>();
    private ArrayList<Light> emulatorLights = new ArrayList<>();
    private ArrayList<Light> HUELights = new ArrayList<>();


    final private String emulatorUrl = "http://192.168.1.187:80/api/newdeveloper";
    final private String HUEUrl = "http://192.168.1.179/api/2kRHeQYCLXt2cnrABObLUG3sC3xSmnL5etpHtEZI";

    private Bridge emulator = new Bridge(emulatorUrl, "Emulator", 0);
    private Bridge HUE = new Bridge(HUEUrl, "HUE", 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        lightArrays.add(HUELights);
        lightArrays.add(emulatorLights);

        this.emulatorIsOnline = false;
        this.HUEIsOnline = false;
        this.queue = Volley.newRequestQueue(this);

        pingEmulator();
        pingHUE();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tvEmulator = findViewById(R.id.textViewEmulator);
        tvHUE = findViewById(R.id.textViewBridge);
    }

    //region ping
    public void pingEmulator(){
        try {
            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    emulator.getUrl() + "/lights",
                    null,
                    response -> {

                        tvEmulator.setText("Emulator\nStatus: Available");
                        Log.d("VOLLEY_REQ", response.toString());
                        String tekst = "";
                        Iterator<String> iter = response.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                                Light light = new Light((JSONObject) response.get(key), key);
                                emulatorLights.add(light);
                                tekst += "\n" + light.toString();
                            } catch (JSONException e) {
                                Log.d("VOLLEY_ERR1", e.toString());
                            }
                        }
                    },
                    error ->{
                        Log.d("VOLLEY_ERRREquest", error.toString());
                        tvEmulator.setText("Emulator\nStatus: Not Available");
                    }
            );

            queue.add(request);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }

    public void pingHUE(){
        try {
            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    HUE.getUrl() + "/lights",
                    null,
                    response -> {

                        tvHUE.setText("HUE\nStatus: Available");
                        Log.d("VOLLEY_REQ", response.toString());
                        String tekst = "";
                        Iterator<String> iter = response.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                                Light light = new Light((JSONObject) response.get(key), key);
                                HUELights.add(light);
                                tekst += "\n" + light.toString();
                            } catch (JSONException e) {
                                Log.d("VOLLEY_ERR1", e.toString());
                            }
                        }
                    },
                    error ->{
                        Log.d("VOLLEY_ERRREquest", error.toString());
                        tvHUE.setText("HUE\nStatus: Not Available");
                    }
            );

            queue.add(request);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }
    //endregion ping

    public void connectToEmulator(View view) {
        Intent intent = new Intent(view.getContext(), LightSelectActivity.class);
        intent.putExtra("BRIDGE", emulator);
        intent.putExtra("INDEX",1);
        view.getContext().startActivity(intent);
    }

    public void connectToHUE(View view) {
        Intent intent = new Intent(view.getContext(), LightSelectActivity.class);
        intent.putExtra("BRIDGE", HUE);
        intent.putExtra("INDEX",0);
        view.getContext().startActivity(intent);
    }
}
