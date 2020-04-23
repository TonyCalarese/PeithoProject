package com.example.peithoproject.recyclerassets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.peithoproject.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewSavedChart extends AppCompatActivity {
    //FileName
    private static final String FILE= "test.txt"; //Setting the file name upon loading from intent
    private String mFile;
    //Charting
    public LineChart mChart;
    ArrayList<Entry> mHappinessData = new ArrayList<>(); //Charting data for Happiness
    ArrayList<Integer> mGatheredEmotions = new ArrayList<>();

    public static Intent newIntent(Context packageContext, String file) {
        Intent intent = new Intent(packageContext, ViewSavedChart.class);
        intent.putExtra(FILE, file);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_chart);
        Intent getFileName = getIntent();

        if(getFileName != null)
        {
            mFile = getFileName.getStringExtra(FILE); // Get the File Name from the Intent
            try {
                readEmotions();
            }catch (Exception e){
                Log.d("FAILURE TO LOAD ", mFile + ".txt");
            }

        }

        //Charting
        mChart = (LineChart) findViewById(R.id.LineChart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);

        mGatheredEmotions.add(0); //Initial value for testing

        try {
            readEmotions(); //Read the data
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        updateChart();  //Update the Chart
        
    }

    public synchronized void updateChart() {

        for(int happyScale: mGatheredEmotions) {
            mHappinessData.add(new Entry(mHappinessData.size(), happyScale));
        }
        LineDataSet HappinessDataSet = new LineDataSet(mHappinessData, "Happiness");
        //Setting up the chart
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(HappinessDataSet);
        LineData data = new LineData(dataSets);

        mChart.setData(data);
        mChart.invalidate();
    }


    public void readEmotions() throws IOException, ClassNotFoundException {
        //Read the Contents of the File
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(mFile));
        String data = (String) input.readObject();
        parseString(data);
        input.close();
    }

    public void parseString(String data) {
        data.replace("[", "");
        data.replace("]","");
        List<String> holder = new ArrayList<String>(Arrays.asList(data.split(",")));

        for (String str : holder){
            mGatheredEmotions.add(Integer.valueOf(str));
        }
    }
}
