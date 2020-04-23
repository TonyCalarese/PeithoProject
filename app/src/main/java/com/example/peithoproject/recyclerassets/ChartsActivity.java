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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartsActivity extends AppCompatActivity {


    //RecyclerView
    private RecyclerView mDataFrame;
    private DataFrameAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mDataset = new ArrayList<String>();
    private ArrayList<UserEmotionData> mUserEmo = new ArrayList<UserEmotionData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        try {
            loadSavedSpeeches();
            Log.d("Success on loading speeches", "SUCCESS TO LOAD");
        }catch(Exception e) {
            Log.d("FAILURE TO LOAD ", "FAILURE TO LOAD");
        }

        mDataFrame = (RecyclerView) findViewById(R.id.saved_speeches_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        mDataFrame.setLayoutManager(mLayoutManager);
        mAdapter = new DataFrameAdapter();
        mDataFrame.setAdapter(mAdapter);


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

        //Here is where it bugs
        ObjectInputStream input = new ObjectInputStream(null);
        for(String name : mDataset){
            input = new ObjectInputStream(new FileInputStream(name + ".txt"));
            String data = (String) input.readObject();

            parseString(data);
        }
        input.close();

        return;
    }

    public void parseString(String data) {
        data.replace("[", "");
        data.replace("]","");
        List<String> holder = new ArrayList<String>(Arrays.asList(data.split(",")));
        UserEmotionData emoHolder = new UserEmotionData();

        for (String str : holder){
            emoHolder.add(Double.valueOf(str));
        }

        mUserEmo.add(emoHolder);
    }
}
