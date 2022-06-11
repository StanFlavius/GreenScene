package com.example.greenscene;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}