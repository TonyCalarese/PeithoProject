package com.example.peithoproject;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    //Charting
    private TextView mInfo;
    private ScrollView mScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mInfo = (TextView) findViewById(R.id.informationTextView);
        mInfo.setText(R.string.about_peitho); //Set about Peitho
        mScroller = (ScrollView) findViewById(R.id.scroller);
    }


}
