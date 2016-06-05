package com.phearom.superreminder.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.phearom.api.core.listener.ClickHandler;
import com.phearom.api.repositories.RealmHelper;
import com.phearom.superreminder.BR;
import com.phearom.superreminder.mappers.LocationMapper;
import com.phearom.superreminder.model.Location;

/**
 * Created by phearom on 5/19/16.
 */
public class LocationViewModel extends BaseObservable {
    private final Location location;
    private ClickHandler<LocationViewModel> optionHandler;

    public LocationViewModel(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.getLocation().getName();
    }

    @Bindable
    public boolean isAlert() {
        return this.location.isAlert();
    }

    public void setAlert(boolean isAlert) {
        this.location.setAlert(isAlert);
        notifyPropertyChanged(BR.alert);
    }

    public void setOptionHandler(ClickHandler<LocationViewModel> optionHandler) {
        this.optionHandler = optionHandler;
    }

    public View.OnClickListener onEnabledAlert() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != optionHandler)
                    optionHandler.onClick(LocationViewModel.this, v);
            }
        };
    }

    @BindingAdapter("switchChanged")
    public static void setSwitchListener(Switch switchView, final LocationViewModel locationViewModel) {
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (locationViewModel != null) {
                    locationViewModel.setAlert(isChecked);
                    RealmHelper.init(buttonView.getContext()).addObject(LocationMapper.toLocationRealm(locationViewModel.getLocation()));
                }
            }
        });
    }
}
