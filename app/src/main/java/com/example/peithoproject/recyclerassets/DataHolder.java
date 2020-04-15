package com.example.peithoproject.recyclerassets;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.peithoproject.R;

//Recycler
public class DataHolder extends RecyclerView.ViewHolder{
    private Button mSpeechButton;
    private int mPosition;

    public DataHolder(LayoutInflater inflater, ViewGroup container) {
        super(inflater.inflate(R.layout.saved_speech_item, container, false));
        mSpeechButton = (Button) itemView.findViewById(R.id.speech_button);
    }

    public void bindPosition(String name, int p) {
        mPosition = p;
        mSpeechButton.setText(name);
    }

}


