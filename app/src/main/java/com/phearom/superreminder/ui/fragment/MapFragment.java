package com.phearom.superreminder.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.phearom.api.maps.NormalMarker;
import com.phearom.api.repositories.RealmHelper;
import com.phearom.api.repositories.defaultmodel.CountryModel;
import com.phearom.superreminder.model.realm.LocationRealm;
import com.phearom.api.utils.AppUtils;
import com.phearom.api.utils.DataUtils;
import com.phearom.api.utils.MapUtils;
import com.phearom.api.utils.SessionManager;
import com.phearom.superreminder.R;

import io.realm.RealmResults;

/**
 * Created by phearom on 6/6/16.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    private static MapFragment instance;
    private GoogleMap mMap;

    public static MapFragment init() {
        if (null == instance)
            instance = new MapFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                SessionManager.init(getContext()).setEnabledGetDistance(true);
                mMap.clear();
                NormalMarker marker = new NormalMarker(getContext());
                marker.setLatLng(latLng);
                marker.setTitle("Start location");
                mMap.addMarker(marker.getMarkerOptions());
            }
        });

        RealmResults<LocationRealm> locationRealm = RealmHelper.init(getContext()).getObject(LocationRealm.class);
        for (LocationRealm l : locationRealm) {
            NormalMarker marker = new NormalMarker(getContext());
            marker.setLatLng(new LatLng(l.getLat(), l.getLng()));
            marker.setTitle(l.getName());
            mMap.addMarker(marker.getMarkerOptions());
        }

        CountryModel countryModel = DataUtils.getCountries(AppUtils.loadJSONFromAsset(getContext())).get(20);
        MapUtils.init(mMap).setAnimateCamera(new LatLng(countryModel.getLatitude_average(), countryModel.getLongitude_average()), 12);
    }
}
