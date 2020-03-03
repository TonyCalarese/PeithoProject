package com.example.peithoproject.recyclerassets;

import java.util.List;

public class UserEmotionData {
    private List<String> mEmotions ;

    public UserEmotionData() {
        mEmotions.add("Neutral");
    }

    //Getters
    public List<String> getAllEmotions() {
        return mEmotions;
    }
    public int getEmotionDataSize() { return mEmotions.size();}

    //setters
    public void add(String emotion){ mEmotions.add(emotion);}

}
