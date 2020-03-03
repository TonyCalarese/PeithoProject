package com.example.peithoproject.com.google.firebase.ml;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

//Depreciated
public class ImageClassifier {
    private static final int RESULTS_TO_SHOW = 3;

    private static final String TAG = "IMAGE CLASSIFIER";

    private byte[][] labelProbArray = null;

    //Dim of inputs
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 1;

    //Buffers for storing image data
    private int[] intValues = new int[getImageSizeX() * getImageSizeY()];

    //Interpreter Opts
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();

    //Tensorflow Lite Models
    private MappedByteBuffer tfliteModel;

    protected Interpreter tflite;

    // Labels corresponding model output
    private List<String> labelList;

    // Img Data to be fed into Model
    protected ByteBuffer imgData = null;

    private float[][] filterLabelProbArray = null;

    private static final int FILTER_STATGES = 3;
    private static final float FILTER_FACTOR = 0.4f;

    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });

    ImageClassifier(Activity activity) throws IOException {
        tfliteModel = loadModelFile(activity);
        tflite = new Interpreter(tfliteModel, tfliteOptions);
        labelList = loadLabelList(activity);

        imgData =
                ByteBuffer.allocateDirect(DIM_BATCH_SIZE * getImageSizeX() * getImageSizeY()
                        * DIM_PIXEL_SIZE * getNumBytesPerChannel());
        imgData.order(ByteOrder.nativeOrder());
        filterLabelProbArray = new float[FILTER_STATGES][getNumLabels()];
        labelProbArray = new byte[1][getNumLabels()];
        Log.d(TAG, "Image Classifier");
    }

    void classifyImage(Bitmap bitmap) {
        if (tflite == null) {
            Log.e(TAG, "Img Classifier not init; skipped");
        }
        convertBitmapToByteBuffer(bitmap);

    }

    private List<String> loadLabelList(Activity activity) throws IOException {
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(activity.getAssets().open(getLabelPath())));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        long startTime = SystemClock.uptimeMillis();
        for (int i = 0; i < getImageSizeX(); ++i) {
            for (int j = 0; j < getImageSizeY(); ++j) {
                final int val = intValues[pixel++];
                addPixelValue(val);
            }
        }
        long endTime = SystemClock.uptimeMillis();
        Log.d(TAG, "Timecost to put values into ByteBuffer: " + Long.toString(endTime - startTime));
    }

    private String getModelPath() {
        return "mobilenet_v1_1.0_224_quant.tflite";
    }

    private String getLabelPath() {
        return "label_emotions.txt";
    }

    private int getImageSizeX() {
        return 224;
    }

    private int getImageSizeY() { return 224; }

    private int getNumBytesPerChannel() {
        return 1;
    }

    private void addPixelValue(int pixelValue) {
        imgData.put((byte) ((pixelValue >> 16) & 0xFF));
        imgData.put((byte) ((pixelValue >> 8) & 0xFF));
        imgData.put((byte) (pixelValue & 0xFF));
    }

    private float getProbability(int labelIndex) {
        return labelProbArray[0][labelIndex];
    }

    private void setPobability(int labelIndex, Number value) {
        labelProbArray[0][labelIndex] = value.byteValue();
    }

    private float getNormalizedProbability(int labelIndex) {
        return (labelProbArray[0][labelIndex] & 0xff) / 255.0f;
    }

    private void runInference() {
        tflite.run(imgData, labelProbArray);
    }

    private int getNumLabels() {
        return labelList.size();
    }
}
// Adapted from https://github.com/EliotAndres/tensorflow-2-run-on-mobile-devices-ios-android-browser
// and https://github.com/tensorflow/examples/blob/master/lite/examples/image_classification/android/EXPLORE_THE_CODE.md
// and https://github.com/tensorflow/tensorflow/blob/master/tensorflow/lite/experimental/support/java/README.md

