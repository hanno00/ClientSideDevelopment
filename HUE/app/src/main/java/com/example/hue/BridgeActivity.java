package com.example.hue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hue.Data.Bridge;
import com.example.hue.Data.Light;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class BridgeActivity extends AppCompatActivity {

    private RequestQueue queue;

    private EditText editName;
    private EditText editIP;
    private EditText editToken;
    private TextView statusTV;

    public static ArrayList<Light> lights = new ArrayList<>();

    final private String emulatorUrl = "http://192.168.1.187:80/api/newdeveloper";
    final private String HUEUrl = "192.168.1.179/api/2kRHeQYCLXt2cnrABObLUG3sC3xSmnL5etpHtEZI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        this.queue = Volley.newRequestQueue(this);

        this.editName = findViewById(R.id.editName);
        this.editIP = findViewById(R.id.editIP);
        this.editToken = findViewById(R.id.editToken);
        this.statusTV = findViewById(R.id.statusTV);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("BRIDGE_PREF", 0); // 0 - for private mode
        editName.setText(pref.getString("NAME_KEY","No name found"));
        editIP.setText(pref.getString("IP_KEY","No IP found"));
        editToken.setText(pref.getString("TOKEN_KEY","No Token found"));

    }

    //region ping
    public void pingBridge(String url, String token){
        try {
            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url + "/" + token + "/lights",
                    null,
                    response -> {
                        statusTV.setText("Available");
                        Log.d("VOLLEY_REQ", response.toString());
                        String tekst = "";
                        Iterator<String> iter = response.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                                Light light = new Light((JSONObject) response.get(key), key);
                                lights.add(light);
                                tekst += "\n" + light.toString();
                            } catch (JSONException e) {
                                Log.d("VOLLEY_ERR1", e.toString());
                            }
                        }
                    },
                    error ->{
                        Log.d("VOLLEY_ERRREquest", error.toString());
                        statusTV.setText("Not Available");
                    }
            );
            queue.add(request);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }

    //endregion ping

    public void pingOnClick(View view) {
        String url = editIP.getText().toString() + "/api";
        String token = editToken.getText().toString();

        pingBridge(url,token);
    }

    public void connectOnClick(View view) {
        String url = editIP.getText().toString();
        String name = editName.getText().toString();

        Intent intent = new Intent(view.getContext(), LightSelectActivity.class);
        intent.putExtra("BRIDGE", new Bridge(url,name));
        view.getContext().startActivity(intent);
    }

    public void onPause() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("BRIDGE_PREF", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("NAME_KEY",editName.getText().toString());
        editor.putString("IP_KEY",editIP.getText().toString());
        editor.putString("TOKEN_KEY",editToken.getText().toString());
        editor.apply();
        super.onPause();
    }

    public void onDestroy() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("BRIDGE_PREF", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("NAME_KEY",editName.getText().toString());
        editor.putString("IP_KEY",editIP.getText().toString());
        editor.putString("TOKEN_KEY",editToken.getText().toString());
        editor.apply();
        super.onDestroy();
    }
}
