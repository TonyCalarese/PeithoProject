package com.example.peithoproject;

import android.os.Handler;

import com.example.peithoproject.com.google.firebase.ml.EmoIdentifier;
import com.example.peithoproject.com.google.firebase.ml.FacialDetector;


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
          ACKNOWLEDGED_FACE_LOG = "FACE DETECTED AND REGISTERED, ATTMEPTING TO IDENTIFY",
          NO_EMOTION_LOGGED = "NO EMOTION REGISTERED DEFAULTING TO HAPPINESS",
             ATTEMPTING_EMOTION = "ATTEMPTING TO READ EMOTION";


     int REQUEST_PHOTO = 101;

     //Emotion Info & Key
     String FACE_SUBSCRIPTION_KEY="d3fc622cd10f42e9a9576d37cbb9a4b0";
     String FACE_ENDPOINT="https://pietho.cognitiveservices.azure.com/";

     //Handler Elements
     Handler mVideoHandler = new Handler();

     //Face Related Classes
     FacialDetector FD = new FacialDetector();
     EmoIdentifier Emo = new EmoIdentifier();






     int mStandardRefreshRate = 10000; //in milliseconds, every 10 seconds
}
