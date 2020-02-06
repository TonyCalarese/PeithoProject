package com.example.peithoproject;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

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
    public float mHappinessProbability = 0.0f;
    public String mEmotion = EMPTY_EMOPTION_STRING;

    FirebaseVisionFaceDetectorOptions mRealTimeOpts = new FirebaseVisionFaceDetectorOptions.Builder().setContourMode(FirebaseVisionFaceDetectorOptions.FAST).build();
    FirebaseVisionFaceDetector mDetector = FirebaseVision.getInstance().getVisionFaceDetector(mRealTimeOpts);
    FirebaseVisionImage mFireImage;

    Bitmap mDetectedFace;


    public Bitmap getImage(){
        //return fireImage.getBitmap();
        return mDetectedFace;
    }
    private void adjustHappinessProbability(float prob){
        //happinessProbability = (happinessProbability + prob) / 2;
        //happinessProbability *= -100;
        mHappinessProbability = prob;
    }
    public String getHappiness() {
        //Happiness is a float between 0.0 and 1.0
        //return "Happiness: " +Float.toString(happinessProbability) + " %";
        return Float.toString(mHappinessProbability);
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
       setFireImage(image);
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
                for (FirebaseVisionFace face : faces) {
                    Log.d(SCANNER_LOG_TAG, ACKNOWLEDGED_FACE_LOG);
                    Rect bounds = face.getBoundingBox(); // Bounds of the Face that was detected
                    //float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    //float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees
                     adjustHappinessProbability(face.getSmilingProbability());

                    //Crashes here most likely will need to troubleshoot
                    mDetectedFace = getCutFace(getFireImage(), bounds.left, bounds.bottom, bounds.width(), bounds.height());
                    mEmotion = Emo.processEmo(mDetectedFace);
                    //For testing purposes
                    // the last face detected will be the face

                }

                //Reference for checking if Bitmap is empty: https://stackoverflow.com/questions/28767466/how-to-check-if-a-bitmap-is-empty-blank-on-android
               /*
                Bitmap emptyBitmap = Bitmap.createBitmap(cutFace.getWidth(), cutFace.getHeight(), cutFace.getConfig());
                if (cutFace.sameAs(emptyBitmap)) {
                    // cutFace is empty/blank
                    cutFace = fireImage.getBitmap();
                }

                */




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
