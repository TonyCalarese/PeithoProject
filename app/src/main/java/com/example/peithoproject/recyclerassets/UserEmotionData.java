package com.example.peithoproject.recyclerassets;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserEmotionData implements Serializable {

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
    public synchronized String getFileData(){
        //Source of refernce: https://devqa.io/java/convert-list-to-array-in-java/
        Integer[] emotions = new Integer[mEmotions.size()];
        emotions = mEmotions.toArray(emotions); //Put all the emotions into a string array
        //end of refernce
        return Arrays.toString(emotions);
    }

    //setters
    public synchronized void add(double emotion){
        int emo = (int) (emotion); //Convert to int and append
        Log.d("Happiness from UEMO: ", String.valueOf(emo));
        mEmotions.add(emo);
    }
}
