package com.example.peithoproject.com.google.firebase.ml;

import java.util.ArrayList;

public class EmoHolder {
    private ArrayList<String> mEmo = new ArrayList<>();

    public EmoHolder(){}

    public synchronized void add(String emo) {
        mEmo.add(emo);
    }

    public synchronized String getEmo() {
        return mEmo.get(mEmo.size() - 1);
    }
}