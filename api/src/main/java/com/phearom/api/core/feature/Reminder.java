package com.phearom.api.core.feature;

import android.content.Context;
import android.net.Uri;

/**
 * Created by phearom on 5/17/16.
 */
public abstract class Reminder {
    private Context context;
    public Reminder(Context context){
        this.context = context;
    }
    public abstract void start();
    public abstract void stop();
    public abstract void setAlertTime(int d);
    public abstract void setAlertSound(Uri uri);

    public Context getContext() {
        return context;
    }
}
