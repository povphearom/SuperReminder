package com.phearom.superreminder.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.phearom.api.bases.BaseAbstractActivity;
import com.phearom.api.repositories.RealmHelper;
import com.phearom.superreminder.R;
import com.phearom.superreminder.databinding.ActivityManageLocationBinding;
import com.phearom.superreminder.mappers.LocationMapper;
import com.phearom.superreminder.model.Location;
import com.phearom.superreminder.model.realm.LocationRealm;
import com.phearom.superreminder.viewmodel.LocationViewModel;

import io.realm.Realm;

public class ManageLocationActivity extends BaseAbstractActivity {
    private ActivityManageLocationBinding mBinding;
    private int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_location);
        mBinding.setView(this);

        mBinding.locationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.getLocation().setName(s.toString());
            }
        });
        mBinding.locationAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.getLocation().setAddress(s.toString());
            }
        });
    }

    private void initLocation() {
        if (null != getId()) {
            LocationRealm locationRealm = RealmHelper.init(ManageLocationActivity.this).getObject(LocationRealm.class).where().equalTo("id", getId()).findFirst();
            mBinding.setLocation(new LocationViewModel(LocationMapper.toLocation(locationRealm)));
            mBinding.locationRemove.setVisibility(View.VISIBLE);
        } else {
            mBinding.locationRemove.setVisibility(View.GONE);
        }
    }

    private String getId() {
        return getIntent().getStringExtra("Id");
    }

    public View.OnClickListener onChoose() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(ManageLocationActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public View.OnClickListener onSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToRealm();
            }
        };
    }

    public View.OnClickListener onDecard() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

    public View.OnClickListener onRemove() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromRealm();
            }
        };
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getBaseContext(), data);
                Location location = new Location();
                location.setId(place.getId());
                location.setName(place.getName().toString());
                location.setAddress(place.getAddress().toString());
                location.setLat(place.getLatLng().latitude);
                location.setLng(place.getLatLng().longitude);
                mBinding.setLocation(new LocationViewModel(location));
            }
        }
    }

    private void removeFromRealm() {
        if (null == getId())
            return;
        RealmHelper.init(ManageLocationActivity.this).getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LocationRealm locationRealm = RealmHelper.init(ManageLocationActivity.this).getObject(LocationRealm.class).where().equalTo("id", getId()).findFirst();
                locationRealm.removeFromRealm();
            }
        });
    }

    private void saveToRealm() {
        Location location = mBinding.getLocation().getLocation();
        if (null == location)
            return;
        if (location.getLat() > 0 && location.getLng() > 0)
            RealmHelper.init(this).addObject(LocationMapper.toLocationRealm(location));
        else
            Toast.makeText(this, "Please choose your location", Toast.LENGTH_SHORT).show();
    }
}
