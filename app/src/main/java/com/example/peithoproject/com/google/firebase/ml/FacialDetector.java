package com.example.peithoproject.com.google.firebase.ml;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.peithoproject.PeithoInterface;
import com.example.peithoproject.recyclerassets.UserEmotionData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;


//Sources of Reference for Async tasks: https://stackoverflow.com/questions/51411110/are-the-firebase-ml-kit-functions-asynchronous-so-that-i-could-run-multiple-dete
//https://www.upwork.com/hiring/mobile/why-you-should-use-asynctask-in-android-development/
//Finn Look here: Whole class is restructured
public class FacialDetector implements PeithoInterface {

    //FireBaseCode for High speed
    FirebaseVisionFaceDetectorOptions mRealTimeOpts = new FirebaseVisionFaceDetectorOptions.Builder()
            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
            .build();

    //Variant of the code for HighAccuracy
    FirebaseVisionFaceDetectorOptions mHighAccuracyOpts =
            new FirebaseVisionFaceDetectorOptions.Builder()
                    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                    .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                    .build();

    FirebaseVisionFaceDetector mDetector = FirebaseVision.getInstance()
            .getVisionFaceDetector(mHighAccuracyOpts);

    //FireBase Declareations //Check interface for the full settings
    FirebaseVisionImage mFireImage;

    //Setters and Functions
    public void setFireImage(Bitmap image){
        mFireImage = FirebaseVisionImage.fromBitmap(image);
    }

    //Tony's version
    public void scanHappiness(final Peitho peitho, Bitmap image, final UserEmotionData data) throws ExecutionException, InterruptedException {
        //Start of Async Task
        setFireImage(image); //Set the image and create the task

        Task<List<FirebaseVisionFace>> result = mDetector.detectInImage(mFireImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> faces) {
                //source for landmarks: https://medium.com/androidiots/firebase-ml-kit-101-face-detection-5057190e58c0
                //https://firebase.google.com/docs/ml-kit/detect-faces

                double HappinessScalePlaceHolder = 0.0; //Going to be the placeholder for the average

                for (FirebaseVisionFace face : faces) {
                    //Happiness is on scale of 0.0 to 1.0 --> Equivalent to a 0-100%

                    double HScale = Math.abs(face.getSmilingProbability()) * 100.0; //Convert to double
                    Log.d("Happiness is: ", String.valueOf(HScale));
                    Log.d("Total Faces ", String.valueOf(faces.size()));
                    HappinessScalePlaceHolder += HScale;
                }
                HappinessScalePlaceHolder = HappinessScalePlaceHolder / faces.size(); //Average it out
                data.add(HappinessScalePlaceHolder); //add it to the data

                //feed the UI Thread the data
                peitho.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        peitho.updateChart();
                    } //Thread the data
                });
            }
        })
                .addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(SCANNER_LOG_TAG, "FUNCTION FAILURE");
                    }
                });


    } //end Scan Faces Function
}
