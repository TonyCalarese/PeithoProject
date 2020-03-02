package com.example.peithoproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataFrame extends Peitho {
    //-------------------------------------------------------------------------------------------------------


    //Recycler
    private class DataHolder extends RecyclerView.ViewHolder{

        private int mPosition;
        public DataHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.tile, container, false));
        }
        public void bindPosition(int p) {
            mPosition = p;
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