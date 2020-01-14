package com.example.meetapp.RecyclerViewStuff;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.meetapp.Data.DatabaseConnection;
import com.example.meetapp.Data.DatabaseListener;
import com.example.meetapp.Models.Person;
import com.example.meetapp.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements DatabaseListener {
    private ArrayList<Person> mDataset;
    private DatabaseConnection dbConnection;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tView;
        public View view;
        public Button button;

        public MyViewHolder(View v){
            super(v);
            this.view = v;
            this.tView = v.findViewById(R.id.nameView);
            this.button = v.findViewById(R.id.button3);
        }
    }

    public RecyclerViewAdapter(ArrayList<Person> myDataset) {
        mDataset = myDataset;
        dbConnection = new DatabaseConnection(this);
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        StringBuilder sb = new StringBuilder();

        sb./* append("Name: "). */append(mDataset.get(position).getName());


        SharedPreferences pref = holder.view.getContext().getSharedPreferences("DATA", 0);



        holder.button.setBackgroundResource(R.drawable.ic_delete_black_24dp);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person p = mDataset.get(position);
                dbConnection.removePerson(p);
            }
        });

        if(mDataset.get(position).getUUID().equals(pref.getString("PERSON", "")) ) {
            holder.button.setVisibility(View.INVISIBLE);
        }

        holder.tView.setText(sb.toString());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onDatabasePersonChanged() {

    }

    @Override
    public void onDatabaseLobbyChanged() {

    }

    @Override
    public void onDatabaseWaypointChanged() {
        
    }
}
