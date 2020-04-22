package com.example.peithoproject;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    //Scroller
    private ScrollView mScroller;
    //TextViews
    private TextView mInfo, mBeforeGettingStarted, mBody1, mBody2, mConsentForm;
    //ImageViews
    private ImageView mPermissionsNeded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mScroller = (ScrollView) findViewById(R.id.scroller); //Setting the scroller

        mInfo = (TextView) findViewById(R.id.informationTextView);

        mBeforeGettingStarted = (TextView)findViewById(R.id.beforeGettingStarted);

        mPermissionsNeded = (ImageView) findViewById(R.id.permissions_needed);

        mBody1 = (TextView) findViewById(R.id.body1);

        mBody2 = (TextView) findViewById(R.id.body2);

        //Source for connecting Links: https://www.google.com/search?q=android+xml+how+to+put+links&oq=android+xml+how+to+put+links&aqs=chrome..69i57.7302j0j7&sourceid=chrome&ie=UTF-8#kpvalbx=_cKOgXszRGaaJgge2x7TAAQ34
        mConsentForm = (TextView) findViewById(R.id.consent_form_link);
        mConsentForm.setMovementMethod(LinkMovementMethod.getInstance());


    }


}
