package com.phearom.api.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by phearom on 5/11/16.
 */
public class SessionManager {
    private static SessionManager instance;
    private SharedPreferences mShare;
    private SharedPreferences.Editor mEditor;

    private SessionManager(Context context) {
        mShare = context.getSharedPreferences("superreminder", Context.MODE_PRIVATE);
        mEditor = mShare.edit();
    }

    public static SessionManager init(Context context) {
        if (instance == null)
            instance = new SessionManager(context);
        return instance;
    }

    public void setEnabledGetDistance(boolean enabledGetDistance) {
        mEditor.putBoolean("enabledDistance", enabledGetDistance);
        mEditor.apply();
    }

    public boolean isEnabledGetDistance() {
        return mShare.getBoolean("enabledDistance", false);
    }

    private void setCountry(double lat, double lng) {
        setLatLang(lat, lng);
    }

    public void setLatLang(double lat, double lng) {
        setLat(lat);
        setLng(lng);
    }

    private void setLat(double lat) {
        mEditor.putString("Lat", String.valueOf(lat));
        mEditor.apply();
    }

    private void setLng(double lng) {
        mEditor.putString("Lng", String.valueOf(lng));
        mEditor.apply();
    }

    public LatLng getLatLng() {
        return new LatLng(Double.valueOf(mShare.getString("Lat", "0.0")), Double.valueOf(mShare.getString("Lng", "0.0")));
    }
}
