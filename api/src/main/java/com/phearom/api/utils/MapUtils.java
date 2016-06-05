package com.phearom.api.utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by phearom on 5/11/16.
 */
public class MapUtils {
    private static MapUtils instance = null;
    private GoogleMap mMap;

    private MapUtils(GoogleMap map) {
        mMap = map;
    }

    public static MapUtils init(GoogleMap map) {
        if (instance == null)
            instance = new MapUtils(map);
        return instance;
    }

    public void setAnimateCamera(LatLng latLng, float zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoom).build();
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
