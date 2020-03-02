package com.example.peithoproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataFrame extends Peitho {
    //-------------------------------------------------------------------------------------------------------


    //Recycler
    private class DataHolder extends RecyclerView.ViewHolder{
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
        }
    }


    public class DataFrameAdapter extends RecyclerView.Adapter<DataHolder> {

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
            return 10;
        }
    }

}