package com.phearom.superreminder.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.phearom.api.keys.IntentKeys;
import com.phearom.api.maps.BaseMarker;
import com.phearom.api.repositories.RealmHelper;
import com.phearom.api.repositories.models.LocationRealm;
import com.phearom.api.services.LocationService;

import java.util.Map;

/**
 * Created by phearom on 5/17/16.
 */
public abstract class BaseMapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = BaseMapActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    private Map<BaseMarker, Marker> markers;
    private Marker mMarker;

    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markers = new ArrayMap<>();
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(IntentKeys.BROADCAST_ACTION);
        startService(new Intent(this, LocationService.class));
    }

    protected abstract GoogleMap getMap();

    protected void onStart() {
        mGoogleApiClient.connect();
        registerReceiver(receiver, mIntentFilter);
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            displayLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
        int resultCode = gApi.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (gApi.isUserResolvableError(resultCode)) {
                gApi.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    public void addMarker(BaseMarker marker) {
        mMarker = getMap().addMarker(marker.getMarkerOptions());
        markers.put(marker, mMarker);
        LocationRealm loc = new LocationRealm();
        loc.setId("001");
        loc.setLat(marker.getLatLng().latitude);
        loc.setLng(marker.getLatLng().longitude);
        loc.setName(marker.getTitle());
        RealmHelper.init(getApplicationContext()).addObject(loc);
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mCurrentLocation != null) {
            double latitude = mCurrentLocation.getLatitude();
            double longitude = mCurrentLocation.getLongitude();
            if (getMap() != null) {
                for (BaseMarker m : markers.keySet()) {
                    if (m.getType() == 0)
                        markers.get(m).remove();
                }

                CameraPosition position = CameraPosition.builder()
                        .target(new LatLng(latitude, longitude))
                        .zoom(16)
                        .build();
                getMap().animateCamera(CameraUpdateFactory.newCameraPosition(position));
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        displayLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    protected void onReceivedBroadcast(String action, String data) {
        Log.i(action, data);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onReceivedBroadcast(intent.getAction(), intent.getExtras().getString(IntentKeys.EXTRA_DATA));
        }
    };
}
