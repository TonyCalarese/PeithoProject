package com.example.peithoproject.recyclerassets;

import java.util.ArrayList;
import java.util.List;

public class UserEmotionData {
    private List<String> mEmotions;;

    public UserEmotionData() {
        mEmotions = new ArrayList<>();
        mEmotions.add("Neutral");
    }

    //Getters
    public synchronized List<String> getAllEmotions() {
        return mEmotions;
    }
    public synchronized int getEmotionDataSize() { return mEmotions.size();}

    //setters
    public synchronized void add(String emotion){ mEmotions.add(emotion);}
    public synchronized String getEmo() {
        return mEmotions.get(mEmotions.size() - 1);
    }

}
