package com.phearom.superreminder.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.phearom.api.keys.IntentKeys;
import com.phearom.api.repositories.RealmHelper;
import com.phearom.api.request.OnResponseCallback;
import com.phearom.api.request.RequestGetDistance;
import com.phearom.superreminder.model.realm.LocationRealm;

import io.realm.RealmResults;


public class LocationService extends Service {
    private static final int TWO_MINUTES = 1000 * 60 * 5;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    private LatLng mStart = new LatLng(0, 0);

    Intent intent;
    private int counter = 0;
    RealmResults<LocationRealm> locationRealm;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(IntentKeys.BROADCAST_ACTION);
        try {
            locationRealm = RealmHelper.init(getApplicationContext()).getObject(LocationRealm.class);
        } catch (Exception e) {
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("START_SERVICE", "DONE");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_STICKY;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(listener);
    }


    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            Log.i("*******", "Location changed");
            if (isBetterLocation(loc, previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();
                getFarFrom(new LatLng(loc.getLatitude(), loc.getLongitude()));
            } else {
                Log.i("LOC=>>>>", "No changed");
            }
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private void getFarFrom(final LatLng end) {
        try {
            final LocationRealm locRealm = locationRealm.get(counter);
            if (null != locRealm) {
                if (!locRealm.isAlert()) {
                    mStart = new LatLng(locRealm.getLat(), locRealm.getLng());
                    RequestGetDistance getDistance = new RequestGetDistance(getApplicationContext());
                    getDistance.setParams(mStart, end, RequestGetDistance.MODE_DRIVING);
                    getDistance.setOnResponseCallback(new OnResponseCallback() {
                        @Override
                        public void onSuccess(String response) {
                            Log.i("Data", response);
                            try {
//                                JSONObject jsonDistance = new JSONObject(response).getJSONObject("distance");
//                                JSONObject jsonDuration = new JSONObject(response).getJSONObject("duration");
                                intent.putExtra(IntentKeys.EXTRA_DATA, response.toString());
                                intent.putExtra("Id", locRealm.getId());
                                sendBroadcast(intent);
                                counter++;
                                getFarFrom(end);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                        }
                    });
                    getDistance.execute();
                }
            }
        } catch (Exception e) {
            counter = 0;
        }
    }
}