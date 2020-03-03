package com.example.peithoproject;

import java.util.List;

public class UserEmotionData {
    private List<String> mEmotions ;

    UserEmotionData() {
        mEmotions.add("Neutral");
    }


    public List<String> getAllEmotions() {
        return mEmotions;
    }


}
