package com.example.peithoproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peithoproject.recyclerassets.DataHolder;
import com.example.peithoproject.recyclerassets.UserEmotionData;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;


//Source of reference for Camera API: https://www.youtube.com/watch?v=u5PDdg1G4Q4
public class Peitho extends Fragment implements TextureView.SurfaceTextureListener, PeithoInterface {
    //Camera
    public TextureView mImageTextureView;
    public Camera mCamera;

    //RecyclerView
    private RecyclerView mDataFrame;
    private DataFrameAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //UserEmo Data
    private UserEmotionData mUserEmoData;

    //Handler Elements
    boolean mStarted = false;

    //TextureView
    boolean mTextureViewVisible = true;

    //Data Classes
    UserEmotionData UserEmoData = new UserEmotionData();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.peitho_main_screen, container, false);

        if (savedInstanceState != null) {

        }

        PackageManager packageManager = getActivity().getPackageManager();

        //User Emo Data
        mUserEmoData = new UserEmotionData();

        //Texture View
        mImageTextureView = (TextureView) v.findViewById((R.id.imageSurfaceView));
        mImageTextureView.setSurfaceTextureListener(Peitho.this);


        //mPhotoView = (ImageView) v.findViewById(R.id.imagePreview); //Photo View, will go away after some tesing is done
        //mEmotionTextResults = (TextView) v.findViewById(R.id.analysisView); //TextView for Results

        mDataFrame = (RecyclerView) v.findViewById(R.id.dataRecyclerView);

        mLayoutManager = new LinearLayoutManager(getContext());
        mDataFrame.setLayoutManager(mLayoutManager);

        //DataFrame
        mAdapter = new DataFrameAdapter();
        mDataFrame.setAdapter(mAdapter);

        return v;
    }

    private void UpdateUI() {
        if (mAdapter == null) {
            mAdapter = new DataFrameAdapter();
            mDataFrame.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }
    }
    //Code For TextureView Reference: https://developer.android.com/reference/android/view/TextureView
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
            System.out.println("ERROR ERRROR NO CAMERA OR PERMISSIONS ERROR");
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
    //End of Reference: https://developer.android.com/reference/android/view/TextureView

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");

        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //Source for rotating with all the proper data https://medium.com/hootsuite-engineering/handling-orientation-changes-on-android-41a6b62cb43f

    //Menu Functions
    //Source on menus: https://developer.android.com/guide/topics/ui/menus#java
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recording_features_menu_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start_menu_icon:
                if(mStarted == false){
                    mVideoHandler.post(mRefreshImageTexture);
                    item.setIcon(R.drawable.play_icon);
                }
                else{
                    mVideoHandler.removeCallbacks(mRefreshImageTexture);
                    item.setIcon(R.drawable.stop_icon);
                }
                mStarted = !mStarted;
                return true;
            case R.id.single_shot_menu_icon:
                Toast.makeText(getActivity(), "DETECTING FACES", Toast.LENGTH_SHORT).show();
                scanForFaces();
                return true;
            case R.id.download:
                //Need to work on saving
                return true;
            case R.id.camera_on_off_menu_icon:
                if(mTextureViewVisible){
                    //Need to turn textureview off
                    item.setIcon(R.drawable.ic_camera);
                }
                else {
                    item.setIcon(R.drawable.ic_camera_off);
                }

                mTextureViewVisible = !mTextureViewVisible;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Functions for Handler
    // Define the code block to be executed
    //Code Reference: https://stackoverflow.com/questions/37995564/what-is-the-way-to-make-an-infinite-loop-in-a-thread-android
    private Runnable mRefreshImageTexture = new Runnable() {
        @Override
        public void run() {
           scanForFaces();
           mVideoHandler.postDelayed(mRefreshImageTexture, mStandardRefreshRate); }
    };

    //Finn Look Here: Added the error handler as requested by the compiler
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void scanForFaces()  {
        try {
            mUserEmoData = FD.scanFaces(mImageTextureView.getBitmap(), mUserEmoData);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }


    //Adapter
    public class DataFrameAdapter extends RecyclerView.Adapter<DataHolder> {
        public DataFrameAdapter() {
        }

        @Override
        public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new DataHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DataHolder holder, int position) {
            holder.bindPosition(mUserEmoData.getIndexEmotion(position), position);
        }

        @Override
        public int getItemCount() {
            return UserEmoData.getEmotionDataSize();
        }

        public void appendEmotion(float emotion) {
            UserEmoData.add(emotion);
        }

    }

}// end of Fragment



