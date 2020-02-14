package com.example.peithoproject;

//Tensorflow Lite Imports

// Tensorflow Adapted from https://github.com/EliotAndres/tensorflow-2-run-on-mobile-devices-ios-android-browser
// and https://github.com/tensorflow/examples/blob/master/lite/examples/image_classification/android/EXPLORE_THE_CODE.md
// and https://github.com/tensorflow/tensorflow/blob/master/tensorflow/lite/experimental/support/java/README.md
// and https://firebase.google.com/docs/ml-kit/android/use-custom-models


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class EmoIdentifier extends Peitho {
    private Object mFaces;
    private FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder().setAssetFilePath("model.tflite").build();

    //Constructer
    public EmoIdentifier(){}

    //Setters
    public void setFaces(Object faces){mFaces = faces;}

    //To Finn:
    // I will be sending you a Bitmap array of the faces
    //I Am Sending you a Single Bitmap with this line of code: mEmotion = Emo.processEmo(mDetectedFace);
    //You return a string of the emotion, you take the bitmap of ONLY the face
    //Once working for one face we will loop an array after
    //Comment everything in great detail, IDC if it is a ton of lines

    public String processEmo(Bitmap bitmap){
        FirebaseModelInterpreter interpreter;
        final HashMap<String, Float> emotionMap = new HashMap<>();

        try {
            FirebaseModelInterpreterOptions options =
                    new FirebaseModelInterpreterOptions.Builder(localModel).build();
            interpreter = FirebaseModelInterpreter.getInstance(options);

            FirebaseModelInputOutputOptions inputOutputOptions =
                    new FirebaseModelInputOutputOptions.Builder()
                            .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 224, 224, 3}) //Input bitmap dimensions Nx224x224x3 picture
                            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 7}) // Output: an array containing float values for each emotions presence
                            .build();


            bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true); //Scales input bitmap
            int batchNum = 0;
            float[][][][] input = new float[1][224][224][3];
            for (int x = 0; x < 224; x++) {  //Translates bitmap to float array
                for (int y = 0; y < 224; y++) {
                    int pixel = bitmap.getPixel(x, y);
                    input[batchNum][x][y][0] = (Color.red(pixel) - 127) / 128.0f;
                    input[batchNum][x][y][1] = (Color.green(pixel) - 127) / 128.0f;
                    input[batchNum][x][y][2] = (Color.blue(pixel) - 127) / 128.0f;
                }
            }

            FirebaseModelInputs inputs = new FirebaseModelInputs.Builder() //Translates input array to firebase type input
                    .add(input)
                    .build();

            interpreter.run(inputs, inputOutputOptions) //Runs Model
                    .addOnSuccessListener(
                            new OnSuccessListener<FirebaseModelOutputs>() {
                                @Override
                                public void onSuccess(FirebaseModelOutputs result) {
                                    float[][] output = result.getOutput(0);
                                    float[] probabilities = output[0];

                                    AssetManager assetManager = getContext().getAssets(); //Need to pass context to access assets folder

                                    try {
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("label_emotions.txt")));

                                        for (int i = 0; i < probabilities.length; i++) {
                                            String label = reader.readLine();//reader.readLine();
                                            Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]));//Outputs the probabilites to Log for now
                                            emotionMap.put(label, probabilities[i]);
                                        }

                                    } catch (Exception e) {
                                        Log.d("Label Error", "Unable to read labels");
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("MODEL FAIL", "Model failed to run");
                                }
                            });


            float bestGuess = 0;
            String bestGuessEmotion = "";
            for (HashMap.Entry<String, Float> emotion : emotionMap.entrySet()) {
                if (emotion.getValue() > bestGuess) {
                    bestGuess = emotion.getValue();
                    bestGuessEmotion = emotion.getKey();
                }
            }

            return bestGuessEmotion;

        } catch (FirebaseMLException e) {
            Log.d("MODEL ERROR", "Model failed");
        }


        return "ERROR GETTING EMOTION";
    } //End of processEmo

} //End of Class
