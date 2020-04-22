package com.example.peithoproject.com.google.firebase.ml;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.peithoproject.PeithoInterface;
import com.example.peithoproject.R;
import com.example.peithoproject.recyclerassets.UserEmotionData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class Peitho extends Fragment implements TextureView.SurfaceTextureListener, PeithoInterface {
    //Camera
    //Source of reference for Camera API: https://www.youtube.com/watch?v=u5PDdg1G4Q4
    //Code For TextureView Reference: https://developer.android.com/reference/android/view/TextureView
    public TextureView mImageTextureView; // declare the texture view for the camera
    public Camera mCamera; //Declare the camera

    //UserEmo Data
    private UserEmotionData mUserEmoData; //Data representation

    //Handler Elements
    boolean mStarted = false; //Boolean if the loop/ play button was pressed

    //Charting
    public LineChart mChart; //Declaring the line chart
    ArrayList<Entry> mHappinessData = new ArrayList<>(); //Charting data for Happiness


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Gets options menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recording_screen, container, false);

        //PackageManager packageManager = getActivity().getPackageManager();

        //User Emo Data
        mUserEmoData = new UserEmotionData();

        //Texture View
        mImageTextureView = (TextureView) v.findViewById((R.id.imageSurfaceView));
        mImageTextureView.setSurfaceTextureListener(Peitho.this);

        //Charting
        mChart = (LineChart) v.findViewById(R.id.LineChart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);

        updateChart();
        return v;
    }


    //Code For TextureView Reference: https://developer.android.com/reference/android/view/TextureView
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            //The camera does not exist or the permission is off
            System.out.println("ERROR ERRROR NO CAMERA OR PERMISSIONS ERROR");
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
    //End of Reference: https://developer.android.com/reference/android/view/TextureView


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //Menu Functions
    //Source on menus: https://developer.android.com/guide/topics/ui/menus#java
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recording_features_menu_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start_menu_icon:
                //Start the runner of getting the emotions
                if(mStarted == false){
                    item.setIcon(R.drawable.stop_icon); //Change the Handler
                    mVideoHandler.post(mRefreshImageTexture); //Start the Hnadler
                }
                else{
                    item.setIcon(R.drawable.play_icon); //Change the Icon
                    mVideoHandler.removeCallbacks(mRefreshImageTexture); //End the handler
                }
                mStarted = !mStarted; //Turn it on or off
                return true;

            case R.id.clear_screen:
                //Clearing the current data in the screen
                mHappinessData.clear(); //Clear the array List
                mUserEmoData = new UserEmotionData(); // Reset the User Emo Data
                updateChart(); //Clear the chart
                return true;

            case R.id.save_menu_item:
                //Need to work on saving
                try {
                    saveSpeech("TestData");
                    Log.d("SUCCESS TO SAVE", "SUCCESS TO SAVE");
                }catch(Exception e) {
                    Log.d("FAILURE TO SAVE", "FAILURE TO SAVE");
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Functions for Handler
    // Define the code block to be executed
    //Code Reference: https://stackoverflow.com/questions/37995564/what-is-the-way-to-make-an-infinite-loop-in-a-thread-android
    private Runnable mRefreshImageTexture = new Runnable() {
        @Override
        public void run() {
           scanForFaces();
           //updateChart(new int[]{20});
           mVideoHandler.postDelayed(mRefreshImageTexture, mStandardRefreshRate); }
    };

   
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void scanForFaces()  {
        try {
            FD.scanHappiness(this, mImageTextureView.getBitmap(), mUserEmoData);
            updateChart();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void updateChart() {
        //int size = mHappinessData.size();
        for(int happyScale: mUserEmoData.getAllEmotions()) {
            mHappinessData.add(new Entry(mHappinessData.size(), happyScale));
        }
            LineDataSet HappinessDataSet = new LineDataSet(mHappinessData, "Happiness");
            //Setting up the chart
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(HappinessDataSet);
            LineData data = new LineData(dataSets);

            mChart.setData(data);
            mChart.invalidate();
        }

    public void saveSpeech(String name) throws IOException {
            PrintWriter writer = new PrintWriter(name + ".txt", "UTF-8");
            FileOutputStream fos = new FileOutputStream(name + ".txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mUserEmoData);
            oos.close();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getContext().openFileOutput("names.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(name);
            outputStreamWriter.close();
        }

}// end of Fragment



