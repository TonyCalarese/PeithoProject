package com.example.peithoproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

//Source of reference: https://www.youtube.com/watch?v=u5PDdg1G4Q4
public class MainMenuFragement extends Fragment {
    private static final String MAIN_MENU_STATIC = "TEST";
    private static final int REQUEST_PHOTO = 101;
    private Button mPhotoButton;
    private ImageView mPhotoView;
    private TextView mEmotionTextResults;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.prototype_layout_1, container, false);
        PackageManager packageManager = getActivity().getPackageManager();

        mPhotoView = (ImageView) v.findViewById(R.id.cameraPreview);
        mPhotoButton = (Button) v.findViewById(R.id.cameraButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFace(v);
            }
        });
        mEmotionTextResults = (TextView) v.findViewById(R.id.analysisView);
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            mPhotoView.setImageBitmap(image);
        }


    }
    //Source: https://developer.android.com/guide/topics/media/camera#java
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public void scanFace(View view){
        Intent scanFace = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        //mPhotoButton.setEnabled(canTakePhoto);

        if(scanFace.resolveActivity(getActivity().getPackageManager())!= null)
        {
            startActivityForResult(scanFace, REQUEST_PHOTO);

        }

    }



}
