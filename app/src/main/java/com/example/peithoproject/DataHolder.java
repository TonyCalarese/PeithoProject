package com.example.peithoproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//Recycler
public class DataHolder extends RecyclerView.ViewHolder{
    private TextView mEmoView;

    private int mPosition;


    public DataHolder(LayoutInflater inflater, ViewGroup container) {
        super(inflater.inflate(R.layout.tile, container, false));
        mEmoView = (TextView) itemView.findViewById(R.id.DataTile);
        mEmoView.setText("No Emotion Detected");
    }
    public void bindPosition(int p) {
        mPosition = p;
    }

    public void appendData() {
        //Add Model Class Syncornous get function here
        //
    }
}


