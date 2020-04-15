package com.example.peithoproject.recyclerassets;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Speech {
    private HashMap<Long, Float> speechData = new HashMap<>();
    private long startTime = 0L;
    private long timeHolder = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Handler timerHandler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.P)
    public Speech() {
        startTime = SystemClock.uptimeMillis();
        timerHandler.postDelayed(updateTimerThread, 0);
    }

    public void addData(float happiness) {
        speechData.put(timeHolder, happiness);
    }

    public boolean saveSpeech(Context context, String filename) {
        try {
            File file = new File(context.getDir("data", MODE_PRIVATE), filename);
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(speechData);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Pair<Long, Float> findMinimum() {
        float minHappiness = Collections.min(speechData.values());
        float value = 0f;
        long key = 0L;
        for(Map.Entry entry:speechData.entrySet()) {
            value = (float) entry.getValue();
            if(Float.compare(minHappiness, value) == 0) {
                key = (long) entry.getKey();
            }
        }
        return new androidx.core.util.Pair<>(key, minHappiness);
    }

    public Pair<Long, Float> findMaximum() {
        float maxHappiness = Collections.max(speechData.values());
        float value = 0f;
        long key = 0L;
        for(Map.Entry entry:speechData.entrySet()) {
            value = (float) entry.getValue();
            if(Float.compare(maxHappiness, value) == 0) {
                key = (long) entry.getKey();
            }
        }
        return new androidx.core.util.Pair<>(key, maxHappiness);
    }

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            timeHolder = secs;
            timerHandler.postDelayed(this, 0);
        }
    };
}
