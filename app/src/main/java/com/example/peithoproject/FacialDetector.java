package com.example.peithoproject;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public class FacialDetector extends Peitho {
    public float happinessProbability = 1.0f;
    FirebaseVisionFaceDetectorOptions realTimeOpts = new FirebaseVisionFaceDetectorOptions.Builder().setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS).build();
    FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(realTimeOpts);
    FirebaseVisionImage fireImage;


    public Bitmap getImage(){
        return fireImage.getBitmap();
    }
    private void adjustHappinessProbability(float prob){
        happinessProbability = (happinessProbability + prob) / 2;
    }
    public String getHappiness() {
        return "Happiness: " +Float.toString(happinessProbability * 100) + " %";
    }




    public void scanFaces(Bitmap image) {
        fireImage = FirebaseVisionImage.fromBitmap(image);
        Task<List<FirebaseVisionFace>> result = detector.detectInImage(fireImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> faces) {
                if(faces.size() <= 0) {
                    //Toast.makeText(getActivity(), "NO FACES DETECTED", Toast.LENGTH_SHORT).show();
                    Log.d(SCANNER_LOG_TAG, "NO FACES DETECTED");
                }
                else if (faces.size() ==  1){
                    //Toast.makeText(getActivity(), "SINGLE FACE DETECTED", Toast.LENGTH_SHORT).show();
                    Log.d(SCANNER_LOG_TAG, "SINGLE FACE DETECTED");
                }
                else{
                    //Toast.makeText(getActivity(), "MULTIPLE FACES DETECTED", Toast.LENGTH_SHORT).show();
                    Log.d(SCANNER_LOG_TAG, "MULTIPLE FACES DETECTED");
                }

                for (FirebaseVisionFace face : faces) {
                    //Rect bounds = face.getBoundingBox(); // Bounds of the Face that was detected
                    //float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    //float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
                     adjustHappinessProbability(face.getSmilingProbability());
                }
            }


        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Function Failure", Toast.LENGTH_SHORT).show();
                            }
                        });

       Log.d(FIREBASE_IMAGE_RESULT_LOG_TAG, result.toString());
    } //end Scan Faces Function

}
