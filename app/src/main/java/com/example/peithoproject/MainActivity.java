package com.example.peithoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.peithoproject.recyclerassets.ChartsActivity;

//Source of reference: https://www.youtube.com/watch?v=u5PDdg1G4Q4

public class MainActivity extends AppCompatActivity {
    private Button mPeithoButton;
    private Button mChartsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mPeithoButton = (Button) findViewById(R.id.peitho_segue_button);
        mPeithoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                segueToPeitho();
            }
        });

        mChartsButton = (Button) findViewById(R.id.chart_segue_button);
        mChartsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                segueToCharts();
            }
        });

    }

    private void segueToPeitho() {
        Intent intent = new Intent(this, PeithoActivity.class);
        startActivity(intent);
    }

    private void segueToCharts() {
        Intent intent = new Intent(this, ChartsActivity.class);
        startActivity(intent);
    }
    private void segueToInfo() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }



}


