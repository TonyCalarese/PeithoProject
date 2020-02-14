package com.example.peithoproject;

        import android.os.Handler;

        import com.google.firebase.ml.vision.FirebaseVision;
        import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
        import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

public interface PeithoInterface {

     //Google Form for Consent: https://forms.gle/LPSRbLoE81PV3akb8

     //Scanner Strings
     String SCANNER_LOG_TAG = "SCANNING_FACES",
             FIREBASE_IMAGE_RESULT_LOG_TAG = "FIREBASE_RESULTS",
             EMPTY_EMOPTION_STRING = "NO EMOTION DETECTED";

     //Log Strings
     String NO_FACE_LOG = "NO FACES DETECTED",
          ONE_FACE_LOG = "SINGLE FACE DETECTED",
          MULTI_FACE_LOG = "MULTIPLE FACES DETECTED",
          ACKNOWLEDGED_FACE_LOG = "FACE DETECTED AND REGISTERED";


     int REQUEST_PHOTO = 101;


     //Handler Elements
     Handler mVideoHandler = new Handler();

     //Face Related Classes
     FacialDetector FD = new FacialDetector();
     EmoIdentifier Emo = new EmoIdentifier();

     FirebaseVisionFaceDetectorOptions mFaceDetectorOptions = new FirebaseVisionFaceDetectorOptions.Builder()
             .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
             .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
             //.enableTracking()
             .build();

     FirebaseVisionFaceDetector mDetector = FirebaseVision.getInstance().
             getVisionFaceDetector(mFaceDetectorOptions);


     //Handler RefrechRate
     int mStandardRefreshRate = 1000; //in milliseconds, every 10 seconds
}
