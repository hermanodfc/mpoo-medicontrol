package com.adht.android.medicontrol.infra.ui;

import android.app.Application;
import android.content.Context;

public class MediControlApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
