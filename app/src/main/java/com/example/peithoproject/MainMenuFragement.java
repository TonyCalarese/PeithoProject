package com.example.peithoproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

//Tensorflow Lite Imports
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.model.Model;


import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

//Source of reference: https://www.youtube.com/watch?v=u5PDdg1G4Q4
public class MainMenuFragement extends Fragment {
    private static final String MAIN_MENU_STATIC = "TEST";
    private static final int REQUEST_PHOTO = 101;
    private Button mPhotoButton;
    private ImageView mPhotoView;
    private TextView mEmotionTextResults;

    private ImageProcessor mImageProcessor =
            new ImageProcessor.Builder()
                    .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                    .build();
    private TensorImage tImage = new TensorImage(DataType.UINT8);
    private TensorBuffer mProbabilityBuffer = TensorBuffer.createFixedSize(new int[]{1, 1001}, DataType.UINT8);
    private TensorProcessor mProbabilityProcessor =
            new TensorProcessor.Builder().add(new NormalizeOp(0, 255)).build();
    private TensorBuffer mDeQuantBuffer = mProbabilityProcessor.process(mProbabilityBuffer);

    private final String AXIS_LABELS = "label_emotions.txt";
    private List<String> axisLabels = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.prototype_layout_1, container, false);
        PackageManager packageManager = getActivity().getPackageManager();



        mPhotoView = (ImageView) v.findViewById(R.id.cameraPreview);
        mPhotoButton = (Button) v.findViewById(R.id.cameraButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFace(v);
            }
        });
        mEmotionTextResults = (TextView) v.findViewById(R.id.analysisView);
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            mPhotoView.setImageBitmap(image);

            tImage.load(image);
            tImage = mImageProcessor.process(tImage);

            try{
                MappedByteBuffer tfliteModel
                        = FileUtil.loadMappedFile(getContext(), "mobilenet_v1_1.0_224_quant.tflite");
                Interpreter tflite = new Interpreter(tfliteModel);

                if(tflite != null) {
                    tflite.run(tImage.getBuffer(), mProbabilityBuffer.getBuffer());
                }
            } catch (IOException e) {
                Log.e("TF", "Error Reading Model", e);
            }

            try {
                axisLabels = FileUtil.loadLabels(getContext(), AXIS_LABELS);
            } catch (IOException e) {
                Log.e("TF", "Error Reading Labels", e);
            }

            if (axisLabels != null) {
                TensorLabel labels = new TensorLabel(axisLabels, mDeQuantBuffer);

                Map<String, Float> results = labels.getMapWithFloatValue();

                for(Map.Entry<String, Float> entry : results.entrySet()) {
                    mEmotionTextResults.setText(mEmotionTextResults.getText().toString() + entry.getKey() + ": " + entry.getValue() + "\n");
                }
            }

        }


    }
    //Source: https://developer.android.com/guide/topics/media/camera#java
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public void scanFace(View view){
        Intent scanFace = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        //mPhotoButton.setEnabled(canTakePhoto);

        if(scanFace.resolveActivity(getActivity().getPackageManager())!= null)
        {
            startActivityForResult(scanFace, REQUEST_PHOTO);

        }

    }

}

// Tensorflow Adapted from https://github.com/EliotAndres/tensorflow-2-run-on-mobile-devices-ios-android-browser
// and https://github.com/tensorflow/examples/blob/master/lite/examples/image_classification/android/EXPLORE_THE_CODE.md
// and https://github.com/tensorflow/tensorflow/blob/master/tensorflow/lite/experimental/support/java/README.md

