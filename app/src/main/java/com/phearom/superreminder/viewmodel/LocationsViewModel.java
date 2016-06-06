package com.phearom.superreminder.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.phearom.api.core.listener.ClickHandler;
import com.phearom.superreminder.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phearom on 5/19/16.
 */
public class LocationsViewModel extends BaseObservable {
    private ClickHandler<LocationViewModel> OptionClickHandler;
    @Bindable
    public ObservableList<LocationViewModel> locations;

    public LocationsViewModel() {
        locations = new ObservableArrayList<>();
    }

    public void addLocation(LocationViewModel locationViewModel) {
        locations.add(locationViewModel);
    }

    public List<Location> getLocationList() {
        List<Location> locs = new ArrayList<>();
        for (LocationViewModel locView : locations) {
            locs.add(locView.getLocation());
        }
        return locs;
    }

    public void update(LocationViewModel locationViewModel) {
        if (locations.size() > 0) {
            int index = locations.indexOf(locationViewModel);
            if (index > -1)
                locations.set(index, locationViewModel);
        }
    }

    public LocationViewModel findById(String id) {
        for (LocationViewModel loc : locations) {
            if (id.equals(loc.getId())) {
                return loc;
            }
        }
        return null;
    }

    public void setLocations(List<Location> locationList) {
        if (locationList.size() > 0)
            locations.clear();
        LocationViewModel locationViewModel;
        for (Location loc : locationList) {
            locationViewModel = new LocationViewModel(loc);
            locationViewModel.setOptionHandler(OptionClickHandler);
            locations.add(locationViewModel);
        }
    }
}
