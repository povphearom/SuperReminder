//package com.phearom.superreminder.ui;
//
//import android.Manifest;
//import android.content.DialogInterface;
//import android.content.pm.PackageManager;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AlertDialog;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Spinner;
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.phearom.api.core.adapter.BindingBaseListViewAdapter;
//import com.phearom.api.core.binder.CompositeItemBinder;
//import com.phearom.api.core.binder.ItemBinder;
//import com.phearom.api.maps.NormalMarker;
//import com.phearom.api.repositories.RealmHelper;
//import com.phearom.api.repositories.defaultmodel.CountryModel;
//import com.phearom.superreminder.model.realm.LocationRealm;
//import com.phearom.api.utils.AppUtils;
//import com.phearom.api.utils.DataUtils;
//import com.phearom.api.utils.MapUtils;
//import com.phearom.api.utils.SessionManager;
//import com.phearom.superreminder.R;
//import com.phearom.superreminder.binder.CountryBinder;
//import com.phearom.superreminder.databinding.DialogSettingsBinding;
//import com.phearom.superreminder.databinding.FragmentMapBinding;
//import com.phearom.superreminder.viewmodel.CountryViewModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.realm.RealmResults;
//
//public class MapActivity extends BaseMapActivity implements OnMapReadyCallback {
//    //AIzaSyDAwlpfUBTdfY9tRjdnSn2U2d1f82dzJZ8
//    private FragmentMapBinding mBinding;
//    private GoogleMap mMap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_map);
//        getSupportActionBar().show();
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    protected GoogleMap getMap() {
//        return mMap;
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setAllGesturesEnabled(true);
//        mMap.getUiSettings().setMapToolbarEnabled(true);
//        mMap.setTrafficEnabled(true);
//        mMap.setBuildingsEnabled(true);
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                SessionManager.init(MapActivity.this).setEnabledGetDistance(true);
//                mMap.clear();
//                NormalMarker marker = new NormalMarker(MapActivity.this);
//                marker.setLatLng(latLng);
//                marker.setTitle("Start location");
//                addMarker(marker);
//            }
//        });
//
//        RealmResults<LocationRealm> locationRealm = RealmHelper.init(getApplicationContext()).getObject(LocationRealm.class);
//        for (LocationRealm l : locationRealm) {
//            NormalMarker marker = new NormalMarker(this);
//            marker.setLatLng(new LatLng(l.getLat(), l.getLng()));
//            marker.setTitle(l.getName());
//            getMap().addMarker(marker.getMarkerOptions());
//        }
//
//        CountryModel countryModel = DataUtils.getCountries(AppUtils.loadJSONFromAsset(MapActivity.this)).get(20);
//        MapUtils.init(mMap).setAnimateCamera(new LatLng(countryModel.getLatitude_average(), countryModel.getLongitude_average()), 12);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                dialogSettings();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void dialogSettings() {
//        final AlertDialog dialog = new AlertDialog.Builder(this).create();
//        DialogSettingsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_settings, null, false);
//        dialog.setView(binding.getRoot());
//        final Spinner spinner = binding.spnCountry;
//        List<CountryViewModel> list = new ArrayList<>();
//        CountryViewModel model;
//        for (CountryModel m : DataUtils.getCountries(AppUtils.loadJSONFromAsset(this))) {
//            model = new CountryViewModel(m);
//            list.add(model);
//        }
//        spinner.setAdapter(new BindingBaseListViewAdapter<CountryViewModel>(itemViewBinder(), list));
//        dialog.setTitle("Please choose your country!");
//        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                CountryViewModel cmodel = (CountryViewModel) spinner.getSelectedItem();
//                MapUtils.init(mMap).setAnimateCamera(new LatLng(cmodel.getLatitude_average(), cmodel.getLongitude_average()), 6);
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }
//
//    private ItemBinder<CountryViewModel> itemViewBinder() {
//        return new CompositeItemBinder<>(
//                new CountryBinder(com.phearom.superreminder.BR.country, R.layout.item_country)
//        );
//    }
//}
