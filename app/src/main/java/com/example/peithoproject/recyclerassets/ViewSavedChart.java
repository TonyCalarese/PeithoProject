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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
        //More code Reference: http://zetcode.com/java/fileinputstream/
        //Source of Reference: https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java/4716623#4716623
        Log.d("File Name: ", mFile + ".txt");

        File fileDir = getApplicationContext().getFileStreamPath(mFile + ".txt");
        FileReader fisr = new FileReader(fileDir);
        BufferedReader br = new BufferedReader((fisr));

        //code here is verbatim reference: //Source of Reference: https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java/4716623#4716623
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            Log.d("Text of File: ", everything);
            parseString(everything); //Push everything in the file through
        } finally {
            br.close();
        }
        //End of refernce
    }


    public void parseString(String data) {
        //Getting Rid of [, ], spaces and \n
        data = data.replace("[", "");
        data = data.replace("]","");
        data = data.replace("\n", "");
        data = data.replace(" ", "");

        String[] rawData = data.split(",");
        Log.d("Loading Value: ", String.valueOf(rawData));
        for (String str : rawData){
            Log.d("Loading Value: ", str);
            mGatheredEmotions.add(Integer.valueOf(str));
        }
    }
}
