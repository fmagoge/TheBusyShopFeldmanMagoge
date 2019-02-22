package com.ikhokha.techcheck.model;

import android.app.Application;

import com.firebase.client.Firebase;

public class FirebaseContent extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}


