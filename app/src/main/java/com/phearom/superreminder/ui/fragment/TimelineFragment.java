package com.phearom.superreminder.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phearom.api.core.binder.CompositeItemBinder;
import com.phearom.api.core.binder.ItemBinder;
import com.phearom.superreminder.BR;
import com.phearom.superreminder.R;
import com.phearom.superreminder.binder.LocationBinder;
import com.phearom.superreminder.databinding.FragmentLocationsBinding;
import com.phearom.superreminder.databinding.FragmentTimelineBinding;
import com.phearom.superreminder.model.Location;
import com.phearom.superreminder.viewmodel.LocationViewModel;
import com.phearom.superreminder.viewmodel.LocationsViewModel;

import java.util.UUID;

/**
 * Created by phearom on 5/19/16.
 */
public class TimelineFragment extends BaseFragment {
    private static TimelineFragment instance;
    private FragmentTimelineBinding mBinding;
    private LocationsViewModel mViewModels;

    public static TimelineFragment init() {
        if (null == instance)
            instance = new TimelineFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModels = new LocationsViewModel();
        mBinding.setModels(mViewModels);
        mBinding.setView(this);

        Location loc;
        for (int i = 0; i < 50; i++) {
            loc = new Location();
            loc.setId(UUID.randomUUID().toString().split("-")[0]);
            loc.setLat(14);
            loc.setLng(34);
            loc.setName("Sample " + i);
            mViewModels.addLocation(new LocationViewModel(loc));
        }
    }

    public ItemBinder<LocationViewModel> itemViewBinder() {
        return new CompositeItemBinder<>(
                new LocationBinder(BR.location, R.layout.item_location)
        );
    }
}
