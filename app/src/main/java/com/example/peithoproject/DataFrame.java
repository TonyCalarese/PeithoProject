package com.example.peithoproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataFrame extends Peitho {
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


    public class DataFrameAdapter extends RecyclerView.Adapter<DataHolder> {

        public List<String> mEmotions;
        public DataFrameAdapter() {
            this.mEmotions = mEmotions;
        }
        @Override
        public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new DataHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DataHolder holder, int position) {
            holder.bindPosition(position);
        }

        @Override
        public int getItemCount() {
            return mEmotions.size();
        }

        public void appendEmotion(String emotion){
            mEmotions.add(emotion);
        }
    }

}