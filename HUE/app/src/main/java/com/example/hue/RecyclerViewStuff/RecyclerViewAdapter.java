package com.example.hue.RecyclerViewStuff;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hue.ColorUtilities;
import com.example.hue.Data.Light;
import com.example.hue.Data.Light;
import com.example.hue.Data.Light;
import com.example.hue.LightSpecificsActivity;
import com.example.hue.R;

import java.io.IOException;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Light> mDataset;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView tViewID, tViewName, tViewState;
        public ImageView iViewColor;


        public MyViewHolder(View v){
            super(v);
            this.view = v;
            this.tViewID = v.findViewById(R.id.recTextID);
            this.tViewName = v.findViewById(R.id.recTextName);
            this.tViewState = v.findViewById(R.id.recTextState);
            this.iViewColor = v.findViewById(R.id.recImageView);
        }
    }

    public RecyclerViewAdapter(List<Light> lightList) {
        mDataset = lightList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater l = LayoutInflater.from(viewGroup.getContext());
        View v = l.inflate(R.layout.recycler_view_item, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Light l = mDataset.get(i);
        myViewHolder.tViewName.setText(mDataset.get(i).getName());
        myViewHolder.tViewState.setText("" + mDataset.get(i).isOn());
        myViewHolder.tViewID.setText("Light " + mDataset.get(i).getKey());

        //System.out.println(ColorUtilities.hsvToRgb(l.getHue(), l.getSaturation(), l.getBrightness()));
        myViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new intent to specifics of light
                Intent intent = new Intent(view.getContext(), LightSpecificsActivity.class);
                intent.putExtra("LightObject", mDataset.get(i));
                view.getContext().startActivity(intent);
            }
        });

        int[] rgbVals = ColorUtilities.HSBtoRGB(new float[] {l.getHue(), l.getSaturation(), l.getBrightness()});
        myViewHolder.iViewColor.setBackgroundColor(
                Color.rgb(rgbVals[0], rgbVals[1], rgbVals[2])
        );

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
