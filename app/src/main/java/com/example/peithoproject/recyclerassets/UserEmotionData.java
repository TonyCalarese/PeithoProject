package com.example.peithoproject.recyclerassets;

import java.util.ArrayList;
import java.util.List;

public class UserEmotionData {

    public List<String> mEmotions;;

    public UserEmotionData() {
        mEmotions = new ArrayList<String>();
        mEmotions.add("Neutral");
        mEmotions.add("Neutral");
        mEmotions.add("Happiness");
        mEmotions.add("Fear");
        mEmotions.add("Fear");
        mEmotions.add("Surprise");
        mEmotions.add("Sadness");
        mEmotions.add("Anger");
        mEmotions.add("Disgust");

    }

    //Getters
    public synchronized List<String> getAllEmotions() { return mEmotions; }
    public synchronized int getEmotionDataSize() { return mEmotions.size();}
    public synchronized String getIndexEmotion(int position) {return mEmotions.get(position);}

    //setters
    public synchronized void add(String emotion){ mEmotions.add(emotion);}
    public synchronized String getEmo() {
        return mEmotions.get(mEmotions.size() - 1);
    }

}
