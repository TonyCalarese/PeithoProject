package com.example.peithoproject;

import android.os.Handler;

public interface PeithoInterface {

     //Google Form for Consent: https://forms.gle/LPSRbLoE81PV3akb8

     String ORIENTATION = "TEST",
             SCANNER_LOG_TAG = "SCANNING_FACES",
             FIREBASE_IMAGE_RESULT_LOG_TAG = "FIREBASE_RESULTS";
     int REQUEST_PHOTO = 101;


     //Handler Elements
     Handler mVideoHandler = new Handler();


     //Face Related Classes
     FacialDetector FD = new FacialDetector();
     EmoIdentifier Emo = new EmoIdentifier();


     int mStandardRefreshRate = 1000; //in milliseconds
}
