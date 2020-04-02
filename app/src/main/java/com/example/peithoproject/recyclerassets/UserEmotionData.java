package com.example.peithoproject.recyclerassets;

import java.util.ArrayList;
import java.util.List;

public class UserEmotionData {

    public List<Float> mEmotions;;

    public UserEmotionData() {
        mEmotions = new ArrayList<Float>();
    }

    //Getters
    public synchronized List<Float> getAllEmotions() { return mEmotions; }
    public synchronized int getEmotionDataSize() { return mEmotions.size();}
    public synchronized float getIndexEmotion(int position) {return mEmotions.get(position);}

    //setters
    public synchronized void add(float emotion){ mEmotions.add(emotion);}
    public synchronized float getEmo() {
        return mEmotions.get(mEmotions.size() - 1);
    }

}
