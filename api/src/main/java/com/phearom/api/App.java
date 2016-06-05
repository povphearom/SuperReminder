package com.phearom.api;

import android.app.Application;

/**
 * Created by phearom on 5/18/16.
 */
public class App extends Application {
    private static App application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        application = null;
    }

    public static App getApplication() {
        return application;
    }
}
