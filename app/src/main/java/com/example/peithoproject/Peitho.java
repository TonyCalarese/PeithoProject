package com.example.peithoproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

//Camera Imports

//Source of reference for Camera API: https://www.youtube.com/watch?v=u5PDdg1G4Q4
public class Peitho extends Fragment implements TextureView.SurfaceTextureListener, PeithoInterface {

    //public ImageView mPhotoView;
    //public TextView mEmotionTextResults;
    //Camera
    public TextureView mImageTextureView;
    public Camera mCamera;

    //RecyclerView
    public RecyclerView mDataFrame;
    public DataFrame.DataFrameAdapter mAdapter;

    //Handler Elements
    boolean mStarted = false;

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

        //Texture View
        mImageTextureView = (TextureView) v.findViewById((R.id.imageSurfaceView));
        mImageTextureView.setSurfaceTextureListener(Peitho.this);


        //mPhotoView = (ImageView) v.findViewById(R.id.imagePreview); //Photo View, will go away after some tesing is done
        //mEmotionTextResults = (TextView) v.findViewById(R.id.analysisView); //TextView for Results

        mDataFrame = (RecyclerView) v.findViewById(R.id.dataRecyclerView);
        mDataFrame.setAdapter(mAdapter);
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
        // Invoked every time there's a new Camera preview frame
        //scanFace();

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

    //Source for rotating with all the proper data https://medium.com/hootsuite-engineering/handling-orientation-changes-on-android-41a6b62cb43f

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
                    mVideoHandler.post(mRefreshImageTexture);
                    item.setIcon(R.drawable.stop_icon);
                }
                else{
                    mVideoHandler.removeCallbacks(mRefreshImageTexture);
                    item.setIcon(R.drawable.play_icon);
                }
                mStarted = !mStarted;
                return true;
            case R.id.single_shot_menu_icon:
                Toast.makeText(getActivity(), "DETECTING FACES", Toast.LENGTH_SHORT).show();
                scanForFaces();
                return true;
            case R.id.download:
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
           mVideoHandler.postDelayed(mRefreshImageTexture, mStandardRefreshRate); }
    };

    //Finn Look Here: Added the error handler as requested by the compiler
    public void scanForFaces()  {
        try {
            FD.scanFaces(mImageTextureView.getBitmap());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

   //     mPhotoView.setImageBitmap(FD.getImage());
        //        mEmotionTextResults.setText(FD.getEmotion());
    }




}// end of Fragment



