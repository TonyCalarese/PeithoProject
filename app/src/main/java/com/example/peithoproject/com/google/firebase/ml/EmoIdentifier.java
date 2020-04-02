package com.example.peithoproject.com.google.firebase.ml;

//Tensorflow Lite Imports

// Tensorflow Adapted from https://github.com/EliotAndres/tensorflow-2-run-on-mobile-devices-ios-android-browser
// and https://github.com/tensorflow/examples/blob/master/lite/examples/image_classification/android/EXPLORE_THE_CODE.md
// and https://github.com/tensorflow/tensorflow/blob/master/tensorflow/lite/experimental/support/java/README.md
// and https://firebase.google.com/docs/ml-kit/android/use-custom-models


import android.graphics.Bitmap;

import com.example.peithoproject.Peitho;
import com.example.peithoproject.recyclerassets.UserEmotionData;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;

public class EmoIdentifier extends Peitho {
    private Object mFaces;
    private FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder().setAssetFilePath("model.tflite").build();

    //Setters
    public void setFaces(Object faces){mFaces = faces;}

    //To Finn:
    // I will be sending you a Bitmap array of the faces
    //I Am Sending you a Single Bitmap with this line of code: mEmotion = Emo.processEmo(mDetectedFace);
    //You return a string of the emotion, you take the bitmap of ONLY the face
    //Once working for one face we will loop an array after
    //Comment everything in great detail, IDC if it is a ton of lines

    public void processEmo(Bitmap bitmap, final UserEmotionData userEmo){

    } //End of processEmo


} //End of Class


