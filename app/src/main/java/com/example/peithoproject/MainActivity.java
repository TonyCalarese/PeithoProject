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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

//Source of reference: https://www.youtube.com/watch?v=u5PDdg1G4Q4

public class MainActivity extends AppCompatActivity {
    private static final String MAIN_MENU_STATIC = "TEST";
    private static final int REQUEST_PHOTO = 101;
    private Button mPhotoButton;
    private ImageView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FragmentManager fm = getSupportFragmentManager();
        //Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //if (fragment == null) {
          //  fm.beginTransaction().add(R.id.fragment_container, new MainMenuFragement()).commit();
        //}


        mPhotoView = (ImageView) findViewById(R.id.cameraPreview);
        mPhotoButton = (Button) findViewById(R.id.cameraButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFace(v);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        if(scanFace.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(scanFace, REQUEST_PHOTO);

        }

    }

}
