package com.example.peithoproject;

import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartingFragment extends Peitho {

    //Source of references:
    //https://www.youtube.com/watch?v=pi1tq-bp7uA
    //https://github.com/PhilJay/MPAndroidChart
    //https://github.com/mitchtabian/SimpleBarGraph1
    public static float mNeutralY = 10f, mHappinessY = 12f, mSadnessY = 14f,
    mFearY = 16, mDisgustY = 18f, mAngerY = 20f, mSurpriseY = 22f;

    public static int mNeutralAmount = 0, mHappinessAmount = 0, mSadnessAmount = 0,
            mFearAmount = 0, mDisgustAmount = 0, mAngerAmount = 0, mSurpriseAmount = 0;

    public static BarData refreshChart(List<String> mData) {

        for(String emotion: mData) {
            Log.d("EMOTION", emotion);
            switch (emotion){
                case "Happiness":
                    incHappiness();
                    break;
                case "Sadness":
                    incSadness();
                    break;
                case "Fear":
                    incFear();
                    break;
                case "Disgust":
                    incDisgust();
                    break;
                case "Anger":
                    incAnger();
                    break;
                case "Surprise":
                    incSurprise();
                    break;
                default:
                    incNeutrality();
                    break;
            }
        }




        //This is the Y axis of the bar graph
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(mNeutralY, mNeutralAmount));
        barEntries.add(new BarEntry(mHappinessY, mHappinessAmount));
        barEntries.add(new BarEntry(mSadnessY, mSadnessAmount));
        barEntries.add(new BarEntry(mFearY, mFearAmount));
        barEntries.add(new BarEntry(mDisgustY, mDisgustAmount));
        barEntries.add(new BarEntry(mAngerY, mAngerAmount));
        barEntries.add(new BarEntry(mSurpriseY, mSurpriseAmount));


        //This is the X axis of the bar graph
        ArrayList<String> theEmotions = new ArrayList<>();
        theEmotions.add("Neutral");
        theEmotions.add("Happiness");
        theEmotions.add("Sadness");
        theEmotions.add("Fear");
        theEmotions.add("Disgust");
        theEmotions.add("Anger");
        theEmotions.add("Surprise");

        //Making the chart
        IBarDataSet barDataSet = new BarDataSet(barEntries, "Neutral, Happiness, Sadness, Fear, Disgust, Anger, Surprise");
        BarData mBarData = new BarData(barDataSet);
        return mBarData;
    }

    public static void incHappiness(){ mHappinessAmount++; }
    public static void incSadness(){ mSadnessAmount++; }
    public static void incFear(){ mFearAmount++; }
    public static void incDisgust(){ mDisgustAmount++; }
    public static void incAnger(){ mAngerAmount++; }
    public static void incSurprise(){ mSurpriseAmount++; }
    public static void incNeutrality(){ mNeutralAmount++; }


}
