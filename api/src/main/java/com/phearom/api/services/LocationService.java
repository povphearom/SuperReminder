//package com.phearom.api.services;
//
//import android.Manifest;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.support.v4.app.ActivityCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.android.volley.VolleyError;
//import com.google.android.gms.maps.model.LatLng;
//import com.phearom.api.keys.IntentKeys;
//import com.phearom.api.repositories.RealmHelper;
//import com.phearom.api.request.OnResponseCallback;
//import com.phearom.api.request.RequestGetDistance;
//import com.phearom.api.utils.SessionManager;
//
//import org.json.JSONObject;
//
//import io.realm.RealmResults;
//
//
//public class LocationService extends Service {
//    private static final int TWO_MINUTES = 1000 * 60 * 2;
//    public LocationManager locationManager;
//    public MyLocationListener listener;
//    public Location previousBestLocation = null;
//    private LatLng mStart = new LatLng(0, 0);
//
//    Intent intent;
//    int counter = 0;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        intent = new Intent(IntentKeys.BROADCAST_ACTION);
//        try {
//            RealmResults<LocationRealm> locationRealm = RealmHelper.init(getApplicationContext()).getObject(LocationRealm.class);
//            LocationRealm loc = locationRealm.first();
//            mStart = new LatLng(loc.getLat(), loc.getLng());
//        } catch (Exception e) {
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.v("START_SERVICE", "DONE");
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        listener = new MyLocationListener();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return START_STICKY;
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
//        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
//        if (currentBestLocation == null) {
//            return true;
//        }
//
//        // Check whether the new location fix is newer or older
//        long timeDelta = location.getTime() - currentBestLocation.getTime();
//        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
//        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
//        boolean isNewer = timeDelta > 0;
//
//        // If it's been more than two minutes since the current location, use the new location
//        // because the user has likely moved
//        if (isSignificantlyNewer) {
//            return true;
//            // If the new location is more than two minutes older, it must be worse
//        } else if (isSignificantlyOlder) {
//            return false;
//        }
//
//        // Check whether the new location fix is more or less accurate
//        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
//        boolean isLessAccurate = accuracyDelta > 0;
//        boolean isMoreAccurate = accuracyDelta < 0;
//        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
//
//        // Check if the old and new location are from the same provider
//        boolean isFromSameProvider = isSameProvider(location.getProvider(),
//                currentBestLocation.getProvider());
//
//        // Determine location quality using a combination of timeliness and accuracy
//        if (isMoreAccurate) {
//            return true;
//        } else if (isNewer && !isLessAccurate) {
//            return true;
//        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Checks whether two providers are the same
//     */
//    private boolean isSameProvider(String provider1, String provider2) {
//        if (provider1 == null) {
//            return provider2 == null;
//        }
//        return provider1.equals(provider2);
//    }
//
//
//    @Override
//    public void onDestroy() {
//        // handler.removeCallbacks(sendUpdatesToUI);
//        super.onDestroy();
//        Log.v("STOP_SERVICE", "DONE");
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        locationManager.removeUpdates(listener);
//    }
//
//    public static Thread performOnBackgroundThread(final Runnable runnable) {
//        final Thread t = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    runnable.run();
//                } finally {
//
//                }
//            }
//        };
//        t.start();
//        return t;
//    }
//
//
//    public class MyLocationListener implements LocationListener {
//
//        public void onLocationChanged(final Location loc) {
//            Log.i("*******", "Location changed");
//            JSONObject data = null;
//            if (isBetterLocation(loc, previousBestLocation)) {
//                loc.getLatitude();
//                loc.getLongitude();
//                try {
//                    data = new JSONObject();
//                    data.put("Latitude", loc.getLatitude());
//                    data.put("Longitude", loc.getLongitude());
//                    data.put("Provider", loc.getProvider());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (SessionManager.init(getApplicationContext()).isEnabledGetDistance()) {
//                    getFarFrom(new LatLng(loc.getLatitude(), loc.getLongitude()));
//                }
//
//                intent.putExtra(IntentKeys.EXTRA_DATA, data.toString());
//                sendBroadcast(intent);
//            } else {
//
//            }
//        }
//
//        public void onProviderDisabled(String provider) {
//            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
//        }
//
//        public void onProviderEnabled(String provider) {
//            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
//        }
//
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//    }
//
//    private void getFarFrom(LatLng end) {
//        RequestGetDistance getDistance = new RequestGetDistance(getApplicationContext());
//        getDistance.setParams(mStart, end, RequestGetDistance.MODE_DRIVING);
//        getDistance.setOnResponseCallback(new OnResponseCallback() {
//            @Override
//            public void onSuccess(String response) {
//                Log.i("Data", response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response).getJSONObject("distance");
//                    if (jsonObject.getDouble("value") < 50) {
//                        Toast.makeText(getApplicationContext(), "You far from setted Location is : " + jsonObject.getString("text"), Toast.LENGTH_SHORT).show();
//                        SessionManager.init(getApplicationContext()).setEnabledGetDistance(false);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//
//            }
//        });
//        getDistance.execute();
//    }
//}