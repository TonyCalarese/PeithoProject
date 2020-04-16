package com.example.peithoproject.com.google.firebase.ml;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;


//Source of reference for Camera API: https://www.youtube.com/watch?v=u5PDdg1G4Q4
public class Peitho extends Fragment implements TextureView.SurfaceTextureListener, PeithoInterface {
    //Camera
    public TextureView mImageTextureView;
    public Camera mCamera;

    //UserEmo Data
    private UserEmotionData mUserEmoData;

    //Handler Elements
    boolean mStarted = false;


    //Data Classes
    UserEmotionData UserEmoData = new UserEmotionData();

    //Charting
    public LineChart mChart;
    ArrayList<Entry> mHappinessData = new ArrayList<>(); //Charting data for Happiness


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.peitho_main_screen, container, false);

        if (savedInstanceState != null) {

        }

        PackageManager packageManager = getActivity().getPackageManager();

        //User Emo Data
        mUserEmoData = new UserEmotionData();

        //Texture View
        mImageTextureView = (TextureView) v.findViewById((R.id.imageSurfaceView));
        mImageTextureView.setSurfaceTextureListener(Peitho.this);

        //DataFrame



        //Charting
        mChart = (LineChart) v.findViewById(R.id.LineChart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setGridBackgroundColor(R.color.white);
        mChart.setBackgroundColor(R.color.white);

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
            // Something bad happened
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");

        }


    }

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
                if(mStarted == false){
                    item.setIcon(R.drawable.stop_icon);
                    mVideoHandler.post(mRefreshImageTexture);
                }
                else{
                    item.setIcon(R.drawable.play_icon);
                    mVideoHandler.removeCallbacks(mRefreshImageTexture);
                }
                mStarted = !mStarted;
                return true;
            case R.id.single_shot_menu_icon:
                Toast.makeText(getActivity(), "DETECTING FACES", Toast.LENGTH_SHORT).show();
                scanForFaces();
                return true;
            case R.id.save_menu_item:
                //Need to work on saving
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
            FileOutputStream fos = new FileOutputStream(name + ".txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mUserEmoData);
            oos.close();
        }

}// end of Fragment



