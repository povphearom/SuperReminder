package com.phearom.api.maps;

import android.content.Context;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by phearom on 5/17/16.
 */
public class NormalMarker extends BaseMarker {
    private String title;
    private LatLng latLng;
    private String description;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NormalMarker(Context context) {
        super(context);
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public BitmapDescriptor getIcon() {
        return BitmapDescriptorFactory.defaultMarker();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
