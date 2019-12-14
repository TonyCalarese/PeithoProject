package com.example.peithoproject;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.util.List;

public class emo_identifier {
    private Object mFaces;

    //Tensor Flow
    private ImageProcessor mImageProcessor = new ImageProcessor.Builder().add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)).build();
    private TensorImage tImage = new TensorImage(DataType.UINT8);
    private TensorBuffer mProbabilityBuffer = TensorBuffer.createFixedSize(new int[]{1, 1001}, DataType.UINT8);
    private TensorProcessor mProbabilityProcessor = new TensorProcessor.Builder().add(new NormalizeOp(0, 255)).build();
    private TensorBuffer mDeQuantBuffer = mProbabilityProcessor.process(mProbabilityBuffer);

    private final String AXIS_LABELS = "label_emotions.txt";
    private List<String> axisLabels = null;

    //Constructer
    public emo_identifier(){}

    //Setters
    public void setFaces(Object faces){mFaces = faces;}


    public String processEmo(){
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
