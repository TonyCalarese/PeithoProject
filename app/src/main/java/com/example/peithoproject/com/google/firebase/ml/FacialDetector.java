package com.example.peithoproject.com.google.firebase.ml;

import android.graphics.Bitmap;
import android.graphics.Rect;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


//Sources of Reference for Async tasks: https://stackoverflow.com/questions/51411110/are-the-firebase-ml-kit-functions-asynchronous-so-that-i-could-run-multiple-dete
//https://www.upwork.com/hiring/mobile/why-you-should-use-asynctask-in-android-development/
//Finn Look here: Whole class is restructured
public class FacialDetector extends Peitho implements PeithoInterface {
    //FireBaseCode
    FirebaseVisionFaceDetectorOptions mRealTimeOpts = new FirebaseVisionFaceDetectorOptions.Builder()
            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .build();
    FirebaseVisionFaceDetector mDetector = FirebaseVision.getInstance()
            .getVisionFaceDetector(mRealTimeOpts);

    public int[] happiness = new int[0]; // make a blank array


    public float mHappinessProbability = 0.0f;
    public String mEmotion = EMPTY_EMOPTION_STRING;

    //FireBase Declareations //Check interface for the full settings
    FirebaseVisionImage mFireImage;
    Bitmap mDetectedFace;
    ArrayList<Bitmap> mDetectedFaces=new ArrayList<Bitmap>();

    //Setters and Functions
    public void setFireImage(Bitmap image){
        mFireImage = FirebaseVisionImage.fromBitmap(image);
    }
    public void appendFacetoList(Bitmap face) {
        mDetectedFaces.add(face);
    }
    public void clearList() {
        mDetectedFaces.clear();
    }

    //Getters
    public String getHappiness() {
        return "Happiness: " +Float.toString(mHappinessProbability) + " %";
    }
    public Bitmap getFireImage()
    {
        return mFireImage.getBitmap();
    }

    public Bitmap getImage(){
        return mDetectedFace;
    }

    //Source of reference: https://stackoverflow.com/questions/5432495/cut-the-portion-of-bitmap
    //https://stackoverflow.com/questions/10998843/create-a-cropped-bitmap-from-an-original-bitmap
    public Bitmap getCutoutFace(Bitmap image, int x, int y, int width, int height) {
        return Bitmap.createBitmap(image, x, y, width, height);
    }


    public UserEmotionData scanFaces(Bitmap image, final UserEmotionData userEmo) throws ExecutionException, InterruptedException {
       setFireImage(image);
       //Start of Async Task
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

                    userEmo.add(face.getSmilingProbability());
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
        //Finn Look here: also in Peitho as well in AdjustScreen
        //Dont know where this is supposed to go but this waits
        //Tasks.await(result); //This will crash the app
       Log.d(FIREBASE_IMAGE_RESULT_LOG_TAG, result.toString());

       return userEmo;
    } //end Scan Faces Function

    //Tony's version
    public void scanHappiness(Bitmap image) throws ExecutionException, InterruptedException {
        //Start of Async Task
        setFireImage(image);
        //Start of Async Task
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

                    updateChart(new int[]{25});
                }
                //updateChart(happiness);
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
