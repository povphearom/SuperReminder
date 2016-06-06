package com.phearom.superreminder.viewmodel;

import android.databinding.BaseObservable;

import com.phearom.api.repositories.defaultmodel.CountryModel;

/**
 * Created by phearom on 5/11/16.
 */
public class CountryViewModel extends BaseObservable {
    private final CountryModel model;

    public CountryViewModel(CountryModel model) {
        this.model = model;
    }

    public CountryModel getModel() {
        return this.model;
    }

    public String getCountry() {
        return this.model.getCountry();
    }

    public double getLatitude_average() {
        return this.model.getLatitude_average();
    }

    public double getLongitude_average() {
        return this.model.getLongitude_average();
    }
}
