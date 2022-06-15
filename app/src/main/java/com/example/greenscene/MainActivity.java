package com.example.greenscene;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    public GoogleSignInClient googleSignInClientAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, PackageManager.PERMISSION_GRANTED);
    }

    public GoogleSignInClient getGoogleSignInClientAct() {
        return googleSignInClientAct;
    }

    public void setGoogleSignInClientAct(GoogleSignInClient googleSignInClientAct) {
        this.googleSignInClientAct = googleSignInClientAct;
    }
}