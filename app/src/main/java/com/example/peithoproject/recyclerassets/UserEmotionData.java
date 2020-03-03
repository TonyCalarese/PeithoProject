package com.example.peithoproject.recyclerassets;

import java.util.List;

public class UserEmotionData {
    private List<String> mEmotions ;

    public UserEmotionData() {
        mEmotions.add("Neutral");
    }


    public List<String> getAllEmotions() {
        return mEmotions;
    }


}
