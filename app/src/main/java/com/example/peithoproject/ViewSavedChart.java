package com.example.peithoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ViewSavedChart extends AppCompatActivity {
    //Charting
    public LineChart mChart;
    ArrayList<Entry> mHappinessData = new ArrayList<>(); //Charting data for Happiness
    ArrayList<Integer> mGatheredEmotions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_chart);

        //Charting
        mChart = (LineChart) findViewById(R.id.LineChart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);


        mGatheredEmotions.add(0); //Initial value for testing

        readEmotions(); //Read the data
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


    public void readEmotions() {
        //Reading the charting data from the file
        //Need to
    }
}
