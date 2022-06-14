package com.example.greenscene.Injection;

import android.app.Application;

import androidx.hilt.work.HiltWorkerFactory;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class BaseApp extends Application {

    @Inject
    public HiltWorkerFactory hiltWorkerFactory;
}
