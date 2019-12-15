package com.example.peithoproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;


import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

//Tensorflow Lite Imports

import java.io.IOException;


//Camera Imports
import android.hardware.Camera;

import static android.app.Activity.RESULT_OK;

//Source of reference: https://www.youtube.com/watch?v=u5PDdg1G4Q4
public class Peitho extends Fragment implements TextureView.SurfaceTextureListener{
    private static final String MAIN_MENU_STATIC = "TEST";
    private static final int REQUEST_PHOTO = 101;
    private Button mStartButton, mSaveButton, mStopButton;


    //Camera
    private ImageView mPhotoView;
    private TextView mEmotionTextResults;
    private TextureView mImageTextureView;
    private Camera mCamera;

    //Handler Elements
    private boolean mStarted = false;
    private Handler mVideoHandler = new Handler();
    private int mRefreshRate = 1000; //in milliseconds

    //Emotion detection main Class
    public EmoIdentifier Emo = new EmoIdentifier();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.peitho_main_screen_landscape, container, false); //Default to landscape

        //Soruce on rotating: https://www.youtube.com/watch?v=7MMs_lBQVcc
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            v = inflater.inflate(R.layout.peitho_main_screen_verticle, container, false); //Use Verticle is in verticle form
        }
        //End of Rotating Source

        PackageManager packageManager = getActivity().getPackageManager();

        //Texture View
        mImageTextureView = (TextureView) v.findViewById((R.id.imageSurfaceView));
        mImageTextureView.setSurfaceTextureListener(Peitho.this);
        //mImageTextureView.setVisibility(View.INVISIBLE);

        //Photo View, will go away after some tesing is done
        mPhotoView = (ImageView) v.findViewById(R.id.imagePreview);

        //Buttons
        mStartButton = (Button) v.findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mStarted == false){
                    mVideoHandler.post(mRefreshImageTexture);
                    mStartButton.setText(R.string.stop);
                    mStarted=true;
                }
                else{
                    mVideoHandler.removeCallbacks(mRefreshImageTexture);
                    mStartButton.setText(R.string.start);
                    mStarted = false;
                }
            }
        });

        mSaveButton = (Button) v.findViewById(R.id.saveButton);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert Saving the code here
            }
        });
        //TextView for Results
        mEmotionTextResults = (TextView) v.findViewById(R.id.analysisView);
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

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

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
            mPhotoView.setImageBitmap(image);

        }


    }





    // Define the code block to be executed
    //Code Reference: https://stackoverflow.com/questions/37995564/what-is-the-way-to-make-an-infinite-loop-in-a-thread-android
    private Runnable mRefreshImageTexture = new Runnable() {
        @Override
        public void run() {
            scanFaces();
            mVideoHandler.postDelayed(mRefreshImageTexture, mRefreshRate); }
    };

    //Putting the image into the Image View for now before moving onto the facial detection
    public void scanFaces(){
        mPhotoView.setImageBitmap(mImageTextureView.getBitmap());
    } //end of scan face function




}


