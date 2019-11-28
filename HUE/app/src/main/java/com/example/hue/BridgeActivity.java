package com.example.hue;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BridgeActivity extends AppCompatActivity {

    private RequestQueue queue;

    final private String emulatorToken = "newdeveloper";
    final private String HUEToken = "2kRHeQYCLXt2cnrABObLUG3sC3xSmnL5etpHtEZI";
    final private String emulatorUrl = "http://145.49.12.80:80/api/" + emulatorToken;
    final private String HUEUrl = "http://192.168.1.179/api/" + HUEToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        this.queue = Volley.newRequestQueue(this);

        TextView tvEmulator = findViewById(R.id.textViewEmulator);
        TextView tvHUE = findViewById(R.id.textViewBridge);

        if (VolleyManager.pingUrl(emulatorUrl)) {
            tvEmulator.setText("Emulator\nStatus: Available");
        } else tvEmulator.setText("Emulator\nStatus: Not available");

        if (VolleyManager.pingUrl(HUEUrl)) {
            tvHUE.setText("Bridge\nStatus: Available");
        } else tvHUE.setText("Bridge\nStatus: Not available");


    }

}
