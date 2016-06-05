package com.phearom.superreminder.binder;

import com.phearom.api.core.binder.ConditionalDataBinder;
import com.phearom.superreminder.viewmodel.CountryViewModel;
import com.phearom.superreminder.viewmodel.LocationViewModel;
import com.phearom.superreminder.viewmodel.LocationsViewModel;

/**
 * Created by phearom on 5/11/16.
 */
public class LocationBinder extends ConditionalDataBinder<LocationViewModel> {
    public LocationBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    @Override
    public boolean canHandle(LocationViewModel model) {
        return true;
    }
}
