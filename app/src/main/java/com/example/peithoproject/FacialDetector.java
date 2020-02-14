package com.example.peithoproject;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;

import java.util.List;

public class FacialDetector extends Peitho {
    public double mHappinessProbability = 100.0;
    public String mEmotion = EMPTY_EMOPTION_STRING;

    //Moved Options and Declaration the the Interface

    FirebaseVisionImage mFireImage; //Set a blank Image

    Bitmap mDetectedFace; //Single Detected Image Variable


    public Bitmap getImage(){
        return mFireImage.getBitmap();
        //return mDetectedFace;
    }
    private void adjustHappinessProbability(float prob){
        mHappinessProbability += Math.abs(prob);
        mHappinessProbability /= 2;
        //mHappinessProbability = prob;
    }
    public String getHappiness() {
        //Happiness is a float between 0.0 and 1.0, converted to a double
        return "Happiness: " +Double.toString(mHappinessProbability) + " %";
    }
    public String getEmotion() {
        return mEmotion;
    }

    public void setFireImage(Bitmap image){
        mFireImage = FirebaseVisionImage.fromBitmap(image);
    }

    public Bitmap getFireImage()
    {
        return mFireImage.getBitmap();
    }

    //Source of reference: https://stackoverflow.com/questions/5432495/cut-the-portion-of-bitmap
    //https://stackoverflow.com/questions/10998843/create-a-cropped-bitmap-from-an-original-bitmap
    public Bitmap getCutFace(Bitmap image, int x, int y, int width, int height) {
        //Bitmap temp = Bitmap.createBitmap(image, x, y, width, height);
        return Bitmap.createBitmap(image, x, y, width, height);
    }


    public void scanFaces(Bitmap image) {
       //setFireImage(image);
        mFireImage = FirebaseVisionImage.fromBitmap(image);
        Task<List<FirebaseVisionFace>> result = mDetector.detectInImage(mFireImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> faces) {

                //Starting here: These lines can be removed when fully functional, unless we want to log properly
                if(faces.size() <= 0) {
                    //Toast.makeText(getActivity(), "NO FACES DETECTED", Toast.LENGTH_SHORT).show();
                    Log.d(SCANNER_LOG_TAG, NO_FACE_LOG);
                }
                else if (faces.size() ==  1){
                    //Toast.makeText(getActivity(), "SINGLE FACE DETECTED", Toast.LENGTH_SHORT).show();
                    Log.d(SCANNER_LOG_TAG, ONE_FACE_LOG);

                }
                else{
                    //Toast.makeText(getActivity(), "MULTIPLE FACES DETECTED", Toast.LENGTH_SHORT).show();
                    Log.d(SCANNER_LOG_TAG, MULTI_FACE_LOG);
                }
                // End of Code that can be Audited

                //source for landmarks: https://medium.com/androidiots/firebase-ml-kit-101-face-detection-5057190e58c0
                //https://firebase.google.com/docs/ml-kit/detect-faces
                //Specific source for Boundaries: https://medium.com/google-developer-experts/exploring-firebase-mlkit-on-android-face-detection-part-two-de7e307c52e0
                for (FirebaseVisionFace face : faces) {
                    Log.d(SCANNER_LOG_TAG, ACKNOWLEDGED_FACE_LOG);
                    Rect bounds = face.getBoundingBox(); // Bounds of the Face that was detected
                    float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees


                    //Crashes here most likely will need to troubleshoot
                    //mDetectedFace = getCutFace(getFireImage(), bounds.left, bounds.bottom, bounds.width(), bounds.height());
                    //mEmotion = Emo.processEmo(mDetectedFace);
                    //mEmotion = String.valueOf(face.getSmilingProbability() + " Level of Happiness");

                    adjustHappinessProbability(face.getSmilingProbability());
                }
            }


        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(SCANNER_LOG_TAG, "FUNCTION FAILURE");
                            }
                        });

       Log.d(FIREBASE_IMAGE_RESULT_LOG_TAG, result.toString());
    } //end Scan Faces Function
}
