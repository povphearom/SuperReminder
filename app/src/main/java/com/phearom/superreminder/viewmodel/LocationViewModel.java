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
import com.phearom.superreminder.model.Location;
import com.phearom.superreminder.model.realm.LocationRealm;

import io.realm.Realm;

/**
 * Created by phearom on 5/19/16.
 */
public class LocationViewModel extends BaseObservable {
    private final Location location;
    private ClickHandler<LocationViewModel> optionHandler;

    private String distance;
    private String timeLeft;

    @Bindable
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
        notifyPropertyChanged(BR.distance);
    }

    @Bindable
    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
        notifyPropertyChanged(BR.timeLeft);
    }

    public LocationViewModel(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getId() {
        return this.location.getId();
    }

    public double getLat() {
        return this.location.getLat();
    }

    public double getLng() {
        return this.location.getLng();
    }

    @Bindable
    public String getName() {
        return this.getLocation().getName();
    }

    public void setName(String name) {
        this.location.setName(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getAddress() {
        return this.location.getAddress();
    }

    public void setAddress(String address) {
        this.location.setAddress(address);
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public boolean isAlert() {
        return this.location.isAlert();
    }

    public void setAlert(boolean alert) {
        this.location.setAlert(alert);
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
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (locationViewModel != null) {
                    locationViewModel.setAlert(isChecked);
                    final LocationRealm locationRealm = RealmHelper.init(buttonView.getContext()).getObject(LocationRealm.class).where().equalTo("id", locationViewModel.getId()).findFirst();
                    RealmHelper.init(buttonView.getContext()).getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            locationRealm.setAlert(isChecked);
                        }
                    });
                }
            }
        });
    }
}
