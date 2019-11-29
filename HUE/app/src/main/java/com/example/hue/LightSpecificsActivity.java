package com.example.hue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.hue.Data.Light;
import com.example.hue.DataType.Triple;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.ErrorListener;

public class LightSpecificsActivity extends AppCompatActivity {

    private RequestQueue queue;
    final private String url = "http://192.168.1.179/api/2kRHeQYCLXt2cnrABObLUG3sC3xSmnL5etpHtEZI/lights";
    private SeekBar red, blue, green;
    private TextView redText, blueText, greenText;
    private ImageView lightColor;
    private Map<SeekBar, TextView> bars = new HashMap<>();
    private List<Triple<SeekBar, TextView, Integer>> rgb = new ArrayList<>();
    private int[] rgbInt = new int[3];
    private Button button;
    private Switch aSwitch;
    private boolean lightState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_specifics);

        queue = Volley.newRequestQueue(this);

        Light l = (Light) getIntent().getSerializableExtra("LightObject");


        //convert HSB to RGB

        float[] hsb = new float[]{l.getHue(), l.getSaturation(), l.getBrightness()};
        int[] rgbVals = ColorUtilities.HSBtoRGB(hsb);

        //set seekbars to correct value from parcel
        red = findViewById(R.id.lightSpecHueBar);
        blue = findViewById(R.id.lightSpecSatBar);
        green = findViewById(R.id.lightSpecBriBar);


        red.setProgress(rgbVals[0]);
        green.setProgress(rgbVals[1]);
        blue.setProgress(rgbVals[2]);

        //set texts to seekbar value
        redText = findViewById(R.id.hueText);
        greenText = findViewById(R.id.briText);
        blueText = findViewById(R.id.satText);

        redText.setText("" + red.getProgress());
        greenText.setText("" + green.getProgress());
        blueText.setText("" + blue.getProgress());

        aSwitch = findViewById(R.id.switch1);

        aSwitch.setChecked(l.isOn());
        aSwitch.setTextOff("OFF");
        aSwitch.setTextOn("ON");

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                lightState = b;
            }
        });

        button = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float[] hsl = ColorUtilities.convertRGBtoHSB(rgbInt);
                int[] vals = ColorUtilities.convertHSBtoSeperateValues(hsl);

                l.setHue(vals[0]);
                l.setSaturation(vals[1]);
                l.setBrightness(vals[2]);
                l.setOn(lightState);

                sendAction(l);
            }
        });



        lightColor = findViewById(R.id.lightSpecColorImage);
        lightColor.setBackgroundColor(Color.rgb(rgbVals[0], rgbVals[1], rgbVals[2]));

        rgb.add(new Triple<>(red, redText, 0));
        rgb.add(new Triple<>(blue, blueText, 1));
        rgb.add(new Triple<>(green, greenText, 2));
        for (Triple<SeekBar, TextView, Integer> triple : rgb) {
            triple.getFirst().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    triple.getSecond().setText("" + triple.getFirst().getProgress());
                    rgbInt[triple.getThird()] = triple.getFirst().getProgress();
                    lightColor.setBackgroundColor(Color.rgb(red.getProgress(), blue.getProgress(), green.getProgress()));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    public void sendAction(Light l) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("BRIDGE_PREF", 0); // 0 - for private mode

        HashMap data = new HashMap();
        data.put("on", l.isOn());
        data.put("bri", l.getBrightness());
        data.put("hue",l.getHue());
        data.put("sat", l.getSaturation());

        JSONArray array = new JSONArray();
        array.put(data);

        try {
            final CustomJsonArrayRequest request = new CustomJsonArrayRequest(
                    Request.Method.PUT,
                    pref.getString("IP_KEY","No IP found") + "/api/" + pref.getString("TOKEN_KEY","No IP found") + "/lights/" + l.getKey() + "/state",
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
