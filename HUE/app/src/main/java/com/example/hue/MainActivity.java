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
import com.example.hue.DataType.Triple;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.hue.Data.Bridge;
import com.example.hue.Data.Light;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.ErrorListener;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;

    private String url;

    public boolean tempResult = false;
    public TextView textView;
    public ArrayList<Light> lights = new ArrayList<>();
    public List<Triple<SeekBar, TextView, Integer>> rgb = new ArrayList<>();
    public int[] rgbInt = new int[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bridge bridge = (Bridge) getIntent().getSerializableExtra("BRIDGE");
        url = bridge.getUrl();

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

        }
        requestJson(url);
//
//        float[] henk = new float[3];
//        Color.colorToHSV(computeHue2(0,143,143),henk);
//        double kleur = ((double)henk[0]/(double)360) * (double)65536;
//        Log.d("Kleur3", "H:" + kleur + " S:" + henk[1] + " L:" + henk[2]);



    }

    public void onclick(View view) {
//
//        float[] hsl = convertRGBtoHSB(rgbInt);
//
//        int hue = (int)((double)hsl[0]/360.0 * 65536.0);
//        int saturation = (int)(hsl[1]*255.0);
//        int brightness = (int)(hsl[2]*255.0);

        // Dit hierboven is omgezet naar 1 methode

        float[] hsl = ColorUtilities.convertRGBtoHSB(rgbInt);

        int[] vals = ColorUtilities.convertHSBtoSeperateValues(hsl);

        for (Light light : lights) {
            light.setOn(true);
            light.setHue(vals[0]);
            light.setSaturation(vals[1]);
            light.setBrightness(vals[2]);
            sendAction(light);
        }
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setBackgroundColor(Color.HSVToColor(hsl));
    }

    public boolean pingUrl(String url){
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

        return tempResult;
    }

    public void requestJson(String url) {
        try {
            final JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url + "/lights",
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

        HashMap data = new HashMap();
        data.put("on", l.isOn());
        data.put("bri", l.getBrightness());
        data.put("hue",l.getHue());
        data.put("sat", l.getSaturation());

        JSONArray array = new JSONArray();
        array.put(data);
        Log.i("request",url + "/" + l.getKey() + "/state");
        System.out.println(url);
        try {
            final CustomJsonArrayRequest request = new CustomJsonArrayRequest(
                    Request.Method.PUT,
                    url + "/lights/" + l.getKey() + "/state",
                    new JSONObject(data),
                    response -> {
                        Log.d("VOLLEY_REQ", response.toString());
                    },
                    null
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

    public class CustomJsonArrayRequest extends JsonRequest<JSONArray> {

        /**
         * Creates a new request.
         * @param method the HTTP method to use
         * @param url URL to fetch the JSON from
         * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
         *   indicates no parameters will be posted along with request.
         * @param listener Listener to receive the JSON response
         * @param errorListener Error listener, or null to ignore errors.
         */
        public CustomJsonArrayRequest(int method, String url, JSONObject jsonRequest,
                                      Response.Listener<JSONArray> listener, ErrorListener errorListener) {
            super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                    (Response.ErrorListener) errorListener);
        }

        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONArray(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }


}
