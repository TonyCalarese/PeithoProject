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

    FirebaseVisionFaceDetectorOptions mRealTimeOpts = new FirebaseVisionFaceDetectorOptions.Builder()
            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            //.enableTracking()
            .build();

    FirebaseVisionFaceDetector mDetector = FirebaseVision.getInstance().
            getVisionFaceDetector(mRealTimeOpts);

    FirebaseVisionImage mFireImage;

    Bitmap mDetectedFace;


    public Bitmap getImage(){
        return mFireImage.getBitmap();
        //return mDetectedFace;
    }
    private void adjustHappinessProbability(float prob){
        //mHappinessProbability = (mHappinessProbability + prob) / 2;
        mHappinessProbability *= -100;
        //mHappinessProbability = prob;
    }
    public String getHappiness() {
        //Happiness is a float between 0.0 and 1.0
        return "Happiness: " +Float.toString(mHappinessProbability) + " %";
        //return Float.toString(mHappinessProbability);
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
                switch(faces.size()){
                    case 0:
                        Log.d(SCANNER_LOG_TAG, NO_FACE_LOG);
                        break;
                    case 1:
                        Log.d(SCANNER_LOG_TAG, ONE_FACE_LOG);
                        break;
                    default:
                        Log.d(SCANNER_LOG_TAG, MULTI_FACE_LOG);
                        break;

                } //End of Auditable Code

                //source for landmarks: https://medium.com/androidiots/firebase-ml-kit-101-face-detection-5057190e58c0
                //https://firebase.google.com/docs/ml-kit/detect-faces
                //Specific source for Boundaries: https://medium.com/google-developer-experts/exploring-firebase-mlkit-on-android-face-detection-part-two-de7e307c52e0
                for (FirebaseVisionFace face : faces) {
                    Rect bounds = face.getBoundingBox(); // Bounds of the Face that was detected
                    float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                    float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                    try {
                        mDetectedFace = getCutFace(getFireImage(), bounds.left, bounds.top, bounds.width(), bounds.height());
                    } catch (Exception e) {
                        Log.d("CUT FACE ERROR", e.getMessage());
                    }

                    try{
                        Log.d(SCANNER_LOG_TAG, ATTEMPTING_EMOTION);
                        Emo.processEmo(mDetectedFace, FacialDetector.this);
                        Log.d(SCANNER_LOG_TAG, ACKNOWLEDGED_FACE_LOG);
                        Log.d("EMOTION: ", mEmotion);
                    } catch (Exception e) {
                        Log.d(SCANNER_LOG_TAG, NO_EMOTION_LOGGED);
                        Log.d(SCANNER_LOG_TAG, e.getMessage());
                        adjustHappinessProbability(face.getSmilingProbability());
                    }
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
