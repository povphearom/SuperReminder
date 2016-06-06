package com.phearom.api.utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by phearom on 6/6/16.
 */
public class AnimUtils {
    public static void enter(View v, float distance) {
        v.animate().translationY(distance).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    public static void exit(View v, float distance) {
        v.animate().translationY(distance).setInterpolator(new AccelerateInterpolator(2)).start();
    }
}
