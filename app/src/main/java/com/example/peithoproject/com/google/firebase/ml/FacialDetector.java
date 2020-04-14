package com.example.peithoproject.com.google.firebase.ml;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.peithoproject.Peitho;
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
    //FireBaseCode
    FirebaseVisionFaceDetectorOptions mRealTimeOpts = new FirebaseVisionFaceDetectorOptions.Builder()
            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
            .build();
    FirebaseVisionFaceDetector mDetector = FirebaseVision.getInstance()
            .getVisionFaceDetector(mRealTimeOpts);

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
                //Starting here: These lines can be removed when fully functional, unless we want to log properly
                switch(faces.size()){
                    case 0:
                        Log.d(SCANNER_LOG_TAG, NO_FACE_LOG);
                        data.add(0);
                        break;
                    case 1:
                        Log.d(SCANNER_LOG_TAG, ONE_FACE_LOG);
                        break;
                    default:
                        Log.d(SCANNER_LOG_TAG, MULTI_FACE_LOG);
                        break;

                } //End of Auditable Code
                for (FirebaseVisionFace face : faces) {
                    //happiness = Arrays.copyOf(happiness, happiness.length + 1);
                    //Happiness is on scale of 0.0 to 1.0 --> Equivalent to a 0-100%
                    Log.d("Happiness is: ", String.valueOf( Math.abs(face.getSmilingProbability())));
                    double HScale = Math.abs(face.getSmilingProbability()); //Convert to double

                    Log.d("Happiness is: ", String.valueOf(HScale));

                    Log.d("Happiness is: ", String.valueOf((int) HScale));
                    //happiness[happiness.length - 1] = (int) HScale; //get all the happiness

                    data.add(HScale);
                }

                peitho.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        peitho.updateChart();
                    }
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
