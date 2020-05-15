package com.example.peithoproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.peithoproject.com.google.firebase.ml.Peitho;
//Source of reference: https://www.youtube.com/watch?v=u5PDdg1G4Q4

public class PeithoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peitho_activity);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fm.beginTransaction().add(R.id.fragment_container, new Peitho()).commit();
        }

    }
}
