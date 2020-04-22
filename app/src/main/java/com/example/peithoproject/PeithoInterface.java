package com.example.peithoproject;

import android.os.Handler;

import com.example.peithoproject.com.google.firebase.ml.FacialDetector;


public interface PeithoInterface {
     //Google Form for Consent: https://forms.gle/LPSRbLoE81PV3akb8
     //Scanner Strings
     String SCANNER_LOG_TAG = "SCANNING_FACES";

     //Log Strings
     String NO_FACE_LOG = "NO FACES DETECTED",
          MULTI_FACE_LOG = "MULTIPLE FACES DETECTED";

     //Handler Elements
     Handler mVideoHandler = new Handler();

     //Face Related Classes
     FacialDetector FD = new FacialDetector();

     int mStandardRefreshRate = 7000; //in milliseconds
}
