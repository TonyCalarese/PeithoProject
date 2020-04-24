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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
                Log.d("FAILURE TO LOAD ", e.toString());
            }

        }

        //Charting
        mChart = (LineChart) findViewById(R.id.LineChart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        


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
        //Source of Reference for code to read from file: https://stackoverflow.com/questions/23675204/how-to-read-write-from-txt-file-in-android
        Log.d("File Name: ", mFile + ".txt");
        File fileDir = getApplicationContext().getFileStreamPath(mFile + ".txt");
        InputStream inStream = new FileInputStream(fileDir);

       // ObjectInputStream input = new ObjectInputStream(new FileInputStream(mFile));
        String data = Integer.toString(inStream.read());
        Log.d("Read Data: ", data);
        parseString(data);
        inStream.close();
        //End of refernce
    }
//63, 45 -> 91?
    //[52, 47] --> 91?
    //[46,47] --> 91?
    public void parseString(String data) {
        data.replace("[", "");
        data.replace("]","");
        List<String> holder = new ArrayList<String>(Arrays.asList(data.split(",")));

        for (String str : holder){
            mGatheredEmotions.add(Integer.valueOf(str));
        }
    }
}
