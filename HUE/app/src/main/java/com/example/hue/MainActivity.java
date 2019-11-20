package com.example.hue;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    final private String url = "http://192.168.1.179/api/2kRHeQYCLXt2cnrABObLUG3sC3xSmnL5etpHtEZI/lights";
    public TextView textView;
    public ArrayList<Light> lights = new ArrayList<>();
    public List<Triple<SeekBar, TextView, Integer>> rgb = new ArrayList<>();
    public int[] rgbInt = new int[3];
    public int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        this.queue = Volley.newRequestQueue(this);

        rgb.add(new Triple<>(findViewById(R.id.seekBar), findViewById(R.id.textView), 0));
        rgb.add(new Triple<>(findViewById(R.id.seekBar2), findViewById(R.id.textView2), 1));
        rgb.add(new Triple<>(findViewById(R.id.seekBar3), findViewById(R.id.textView3), 2));
        for (Triple<SeekBar, TextView, Integer> triple : rgb) {
            triple.getFirst().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    triple.getSecond().setText("Value" + triple.getFirst().getProgress());
                    rgbInt[triple.getThird()] = triple.getFirst().getProgress();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            counter++;
        }
        requestJson();
//
//        float[] henk = new float[3];
//        Color.colorToHSV(computeHue2(0,143,143),henk);
//        double kleur = ((double)henk[0]/(double)360) * (double)65536;
//        Log.d("Kleur3", "H:" + kleur + " S:" + henk[1] + " L:" + henk[2]);



    }

    public void onclick(View view) {

        float[] hsl = convertRGBtoHSB(rgbInt);

        int hue = (int)((double)hsl[0]/360.0 * 65536.0);
        int saturation = (int)(hsl[1]*255.0);
        int brightness = (int)(hsl[2]*255.0);

//        Log.d("Kleur1","" + computeHue(r,g,b));
//        Log.d("Bietje stront", "" + hsl[0]);
//        Log.d("Kleur3", "H:" + hue + " S:" + saturation + " L:" + brightness);

        for (Light light : lights) {
            light.setOn(true);
            light.setHue(hue);
            light.setSaturation(saturation);
            light.setBrightness(brightness);
            sendAction(light);
        }
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setBackgroundColor(Color.HSVToColor(hsl));
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
                                lights.add(light);
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

    public void sendAction(Light l) {

        int r = 255;
        int g = 255;
        int b = 0;

        HashMap data = new HashMap();
        data.put("on", l.isOn());
        data.put("bri", l.getBrightness());
//        data.put("hue", kleur);
        data.put("hue",l.getHue());
        data.put("sat", l.getSaturation());
//        data.put("","");
//        data.put("","");
//        data.put("","");
//        data.put("","");

        JSONArray array = new JSONArray();
        array.put(data);

        try {
            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url + "/" + l.getKey() + "/state",
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

    public float[] convertRGBtoHSB(int[] rgb) {
        float[] hsb = new float[3];
        Color.RGBToHSV(rgb[0],rgb[1],rgb[2],hsb);
        return hsb;
    }

    public class Triple<T, U, V> {

        private final T first;
        private final U second;
        private final V third;

        public Triple(T first, U second, V third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }

        public V getThird() {
            return third;
        }
    }
}
