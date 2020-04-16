package com.example.peithoproject.recyclerassets;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserEmotionData {

    public List<Integer> mEmotions;

    public UserEmotionData() {
        mEmotions = new ArrayList<Integer>();
    }

    //Getters
    public synchronized List<Integer> getAllEmotions() { return mEmotions; }
    public synchronized int getEmotionDataSize() { return mEmotions.size();}
    public synchronized int getIndexEmotion(int position) {return mEmotions.get(position);}
    public synchronized int getEmo() {
        return mEmotions.get(mEmotions.size());
    }

    //setters
    public synchronized void add(double emotion){
        int emo = (int) (emotion * 100);
        Log.d("Happiness from UEMO: ", String.valueOf(emo));
        mEmotions.add(emo);
    }
}
