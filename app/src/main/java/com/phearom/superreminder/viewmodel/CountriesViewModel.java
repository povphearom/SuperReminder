package com.phearom.superreminder.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

/**
 * Created by phearom on 5/11/16.
 */
public class CountriesViewModel extends BaseObservable{
    private ObservableList<CountryViewModel> items;
    public CountriesViewModel(){
        items = new ObservableArrayList<>();
    }

    public void addItem(CountryViewModel countryViewModel){
        items.add(countryViewModel);
    }
}
