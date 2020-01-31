package com.example.peithoproject;

//Tensorflow Lite Imports



// Tensorflow Adapted from https://github.com/EliotAndres/tensorflow-2-run-on-mobile-devices-ios-android-browser
// and https://github.com/tensorflow/examples/blob/master/lite/examples/image_classification/android/EXPLORE_THE_CODE.md
// and https://github.com/tensorflow/tensorflow/blob/master/tensorflow/lite/experimental/support/java/README.md


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

public class EmoIdentifier {
    private Object mFaces;

    //Tensor Flow
    /*
    private ImageProcessor mImageProcessor = new ImageProcessor.Builder().add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)).build();
    private TensorImage tImage = new TensorImage(DataType.UINT8);
    private TensorBuffer mProbabilityBuffer = TensorBuffer.createFixedSize(new int[]{1, 1001}, DataType.UINT8);
    private TensorProcessor mProbabilityProcessor = new TensorProcessor.Builder().add(new NormalizeOp(0, 255)).build();
    private TensorBuffer mDeQuantBuffer = mProbabilityProcessor.process(mProbabilityBuffer);

    private final String AXIS_LABELS = "label_emotions.txt";
    private List<String> axisLabels = null;
    */

    private FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
            .setAssetFilePath("model.tflite")
            .build();


    //Constructer
    public EmoIdentifier(){}

    //Setters
    public void setFaces(Object faces){mFaces = faces;}


    public String processEmo(Bitmap bitmap){
        FirebaseModelInterpreter interpreter;

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

                                    //AssetManager assetManager = Context.getAssets(); //Need to pass context to access assets folder

                                    try {
                                        //BufferedReader reader = new BufferedReader(
                                                //new InputStreamReader(assetManager.open("label_emotions.txt")));

                                        for (int i = 0; i < probabilities.length; i++) {
                                            String label = "";//reader.readLine();
                                            Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]));//Outputs the probabilites to Log for now
                                        }
                                        //ADD RETURN STATEMENT HERE
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

        } catch (FirebaseMLException e) {
            Log.d("MODEL ERROR", "Model failed");
        }


        return "Happy";
    }

     /*
            tImage.load(image);
            tImage = mImageProcessor.process(tImage);

            try{
                MappedByteBuffer tfliteModel
                        = FileUtil.loadMappedFile(getContext(), "mobilenet_v1_1.0_224_quant.tflite");
                Interpreter tflite = new Interpreter(tfliteModel);

                if(tflite != null) {
                    tflite.run(tImage.getBuffer(), mProbabilityBuffer.getBuffer());
                }
            } catch (IOException e) {
                Log.e("TF", "Error Reading Model", e);
            }

            try {
                axisLabels = FileUtil.loadLabels(getContext(), AXIS_LABELS);
            } catch (IOException e) {
                Log.e("TF", "Error Reading Labels", e);
            }

            if (axisLabels != null) {
                TensorLabel labels = new TensorLabel(axisLabels, mDeQuantBuffer);

                Map<String, Float> results = labels.getMapWithFloatValue();

                for(Map.Entry<String, Float> entry : results.entrySet()) {
                    mEmotionTextResults.setText(mEmotionTextResults.getText().toString() + entry.getKey() + ": " + entry.getValue() + "\n");
                }
            }
            */


}
