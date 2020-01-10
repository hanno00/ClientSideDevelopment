package com.example.meetapp.RecyclerViewStuff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.meetapp.Models.Person;
import com.example.meetapp.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Person> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tView;
        public View view;


        public MyViewHolder(View v){
            super(v);
            this.view = v;
            this.tView = v.findViewById(R.id.nameView);
        }
    }

    public RecyclerViewAdapter(ArrayList<Person> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater l = LayoutInflater.from(parent.getContext());
        View v = l.inflate(R.layout.rec_view_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        StringBuilder sb = new StringBuilder();

        sb.append("Name: ").append(mDataset.get(position).getName());
        sb.append(" PersonUUID: ").append(mDataset.get(position).getUUID());
        sb.append(" LobbyUUID: ").append(mDataset.get(position).getlobbyUUID());


        holder.tView.setText(sb.toString());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
