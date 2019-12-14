package com.example.peithoproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;


import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

//Tensorflow Lite Imports
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.List;


//Camera Imports
import android.hardware.Camera;

import static android.app.Activity.RESULT_OK;

//Source of reference: https://www.youtube.com/watch?v=u5PDdg1G4Q4
public class peitho extends Fragment implements TextureView.SurfaceTextureListener{
    private static final String MAIN_MENU_STATIC = "TEST";
    private static final int REQUEST_PHOTO = 101;
    private Button mPhotoButton;

    //Tensor Flow
    private ImageProcessor mImageProcessor = new ImageProcessor.Builder().add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)).build();
    private TensorImage tImage = new TensorImage(DataType.UINT8);
    private TensorBuffer mProbabilityBuffer = TensorBuffer.createFixedSize(new int[]{1, 1001}, DataType.UINT8);
    private TensorProcessor mProbabilityProcessor = new TensorProcessor.Builder().add(new NormalizeOp(0, 255)).build();
    private TensorBuffer mDeQuantBuffer = mProbabilityProcessor.process(mProbabilityBuffer);

    private final String AXIS_LABELS = "label_emotions.txt";
    private List<String> axisLabels = null;


    //Camera
    private ImageView mPhotoView;
    private TextView mEmotionTextResults;
    private TextureView mImageTextureView;
    private Camera mCamera;
    //Emotion detection main Class
    public emo_identifier Emo = new emo_identifier();


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


        PackageManager packageManager = getActivity().getPackageManager();


        mImageTextureView = (TextureView) v.findViewById((R.id.imageSurfaceView));
        mImageTextureView.setSurfaceTextureListener(peitho.this);
        //mImageTextureView.setVisibility(View.INVISIBLE);

        mPhotoView = (ImageView) v.findViewById(R.id.cameraPreview);
        mPhotoButton = (Button) v.findViewById(R.id.cameraButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFace();
            }
        });
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

    public void scanFace(){
        //Intent scanFace = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Original Code


        //boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        //mPhotoButton.setEnabled(canTakePhoto);
        /*
        if(scanFace.resolveActivity(getActivity().getPackageManager())!= null)
        {
            startActivityForResult(scanFace, REQUEST_PHOTO);

        }
         */


        mPhotoView.setImageBitmap(mImageTextureView.getBitmap());
    } //end of scan face function




}

// Tensorflow Adapted from https://github.com/EliotAndres/tensorflow-2-run-on-mobile-devices-ios-android-browser
// and https://github.com/tensorflow/examples/blob/master/lite/examples/image_classification/android/EXPLORE_THE_CODE.md
// and https://github.com/tensorflow/tensorflow/blob/master/tensorflow/lite/experimental/support/java/README.md

