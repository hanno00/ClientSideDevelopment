package com.example.hue;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hue.Data.Bridge;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BridgeActivity extends AppCompatActivity {

    private boolean emulatorIsOnline;
    private boolean HUEIsOnline;
    private RequestQueue queue;

    private TextView tvEmulator;
    private TextView tvHUE;

    final private String emulatorToken = "newdeveloper";
    final private String HUEToken = "2kRHeQYCLXt2cnrABObLUG3sC3xSmnL5etpHtEZI";
    final private String emulatorUrl = "http://192.168.1.24:80/api/" + emulatorToken;
    final private String HUEUrl = "http://192.168.1.179/api/" + HUEToken;

    private Bridge emulator = new Bridge(emulatorUrl, "Emulator", 0);
    private Bridge HUE = new Bridge(HUEUrl, "HUE", 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

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
                    emulatorUrl,
                    null,
                    response -> {
                        tvEmulator.setText("Emulator\nStatus: Available");
                    },
                    error -> {
                        tvEmulator.setText("Emulator\nStatus: Not Available");
                        Log.d("Connection Error EMULAT", error.toString());
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
                    HUEUrl,
                    null,
                    response -> {
                        tvHUE.setText("HUE\nStatus: Available");
                    },
                    error -> {
                        tvHUE.setText("HUE\nStatus: Not Available");
                        Log.d("Connection Error HUE", error.toString());
                    }
            );
            queue.add(request);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }
    //endregion ping

    public void connectToEmulator(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("BRIDGE", emulator);
        view.getContext().startActivity(intent);
    }

    public void connectToHUE(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("BRIDGE", HUE);
        view.getContext().startActivity(intent);
    }
}
