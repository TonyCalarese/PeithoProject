package com.example.peithoproject.recyclerassets;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.peithoproject.R;

//Recycler
public class DataHolder extends RecyclerView.ViewHolder{
    private TextView mEmoView;

    private int mPosition;

    public DataHolder(LayoutInflater inflater, ViewGroup container) {
        super(inflater.inflate(R.layout.tile, container, false));
        mEmoView = (TextView) itemView.findViewById(R.id.DataTile);
    }

    public void bindPosition(String emo, int p) {
        mPosition = p;
        mEmoView.setText(emo);
    }

    public void appendData() {
        //Add Model Class Syncornous get function here
        //
    }
}


