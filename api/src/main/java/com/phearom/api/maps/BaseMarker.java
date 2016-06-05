package com.phearom.api.maps;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by phearom on 5/17/16.
 */
public abstract class BaseMarker{

    private MarkerOptions markerOptions;
    private Context context;

    public BaseMarker(Context context) {
        this.context = context;
    }

    public abstract int getType();

    public abstract BitmapDescriptor getIcon();

    public abstract String getTitle();

    public abstract LatLng getLatLng();

    public abstract String getDescription();

    public MarkerOptions getMarkerOptions() {
        if (markerOptions == null)
            markerOptions = new MarkerOptions();
        markerOptions.position(getLatLng())
                .icon(getIcon())
                .flat(true);
        return this.markerOptions;
    }

    private float getZoom(){
        return 15;
    }

    public CameraUpdate getCameraUpdate() {
        //13f
        return CameraUpdateFactory.newLatLngZoom(getLatLng(), getZoom());
    }

    public CameraUpdate getAnimationCameraUpdate() {
        CameraPosition position = CameraPosition.builder()
                .target(getLatLng())
                .zoom(getZoom())
                .build();
        return CameraUpdateFactory.newCameraPosition(position);
    }
}
