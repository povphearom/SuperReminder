package com.phearom.superreminder.binder;

import com.phearom.api.core.binder.ConditionalDataBinder;
import com.phearom.superreminder.viewmodel.CountryViewModel;

/**
 * Created by phearom on 5/11/16.
 */
public class CountryBinder extends ConditionalDataBinder<CountryViewModel> {
    public CountryBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    @Override
    public boolean canHandle(CountryViewModel model) {
        return true;
    }
}
