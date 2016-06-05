package com.phearom.superreminder.request;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by phearom on 5/12/16.
 */
public class RequestRouteDirection {
    private static final String URL = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%d,%d&destination=%d,%d&sensor=false&mode=%s";
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

    public String getUrlRequest(LatLng start, LatLng end, String mode) {
        return String.format(URL, start.latitude, start.longitude, end.latitude, end.longitude,mode);
    }
}
