package com.example.hue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hue.Data.Light;
import com.example.hue.DataType.Triple;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        blue.setProgress(rgbVals[1]);
        green.setProgress(rgbVals[2]);

        //set texts to seekbar value
        redText = findViewById(R.id.hueText);
        blueText = findViewById(R.id.satText);
        greenText = findViewById(R.id.briText);

        redText.setText("" + red.getProgress());
        blueText.setText("" + blue.getProgress());
        greenText.setText("" + green.getProgress());

        aSwitch = findViewById(R.id.switch1);

        aSwitch.setChecked(l.isOn());
        aSwitch.setTextOff("OFF");
        aSwitch.setTextOn("ON");

        button = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float[] hsl = ColorUtilities.convertRGBtoHSB(rgbInt);
                int[] vals = ColorUtilities.convertHSBtoSeperateValues(hsl);

                l.setHue(vals[0]);
                l.setSaturation(vals[1]);
                l.setBrightness(vals[2]);

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
                    lightColor.setBackgroundColor(Color.rgb(red.getProgress(), green.getProgress(), blue.getProgress()));
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

        HashMap data = new HashMap();
        data.put("on", l.isOn());
        data.put("bri", l.getBrightness());
        data.put("hue",l.getHue());
        data.put("sat", l.getSaturation());

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

}
