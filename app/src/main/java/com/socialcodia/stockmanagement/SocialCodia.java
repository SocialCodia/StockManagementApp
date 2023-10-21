package com.socialcodia.stockmanagement;

import android.app.Application;

public class SocialCodia extends Application {

    private static SocialCodia instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance==null)
            instance = this;
    }

    public static SocialCodia getInstance()
    {
        return instance;
    }
}



