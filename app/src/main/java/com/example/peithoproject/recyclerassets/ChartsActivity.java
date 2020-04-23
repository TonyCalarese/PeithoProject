package com.example.peithoproject.recyclerassets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peithoproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChartsActivity extends AppCompatActivity {


    //RecyclerView
    private RecyclerView mDataFrame;
    private DataFrameAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mDataset = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        mDataFrame = (RecyclerView) findViewById(R.id.saved_speeches_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        mDataFrame.setLayoutManager(mLayoutManager);
        mAdapter = new DataFrameAdapter();
        mDataFrame.setAdapter(mAdapter);


        try {
            loadSavedSpeeches();
            Log.d("Success on loading speeches", "SUCCESS TO LOAD");
        }catch(Exception e) {
            Log.d("FAILURE TO LOAD ", "FAILURE TO LOAD");
        }
    }


    //Adapter
    public class DataFrameAdapter extends RecyclerView.Adapter<DataHolder> {
        public DataFrameAdapter() {
        }

        @Override
        public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            return new DataHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DataHolder holder, int position) {
            holder.bindPosition(mDataset.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

    }

    private void loadSavedSpeeches() throws IOException, ClassNotFoundException {
            InputStream inStream = this.openFileInput("names.txt");

            if(inStream != null) {
                InputStreamReader inStreamReader = new InputStreamReader(inStream);
                BufferedReader bufferedReader = new BufferedReader(inStreamReader);
                String receivedName = "";

                while ( (receivedName = bufferedReader.readLine()) != null) {
                    mDataset.add(receivedName);
                }
            }

            mAdapter.notifyDataSetChanged();
        return;
    }

}
