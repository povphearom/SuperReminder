package com.phearom.superreminder.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phearom.api.core.binder.CompositeItemBinder;
import com.phearom.api.core.binder.ItemBinder;
import com.phearom.api.keys.IntentKeys;
import com.phearom.api.repositories.RealmHelper;
import com.phearom.superreminder.BR;
import com.phearom.superreminder.R;
import com.phearom.superreminder.binder.LocationBinder;
import com.phearom.superreminder.databinding.FragmentLocationsBinding;
import com.phearom.superreminder.mappers.LocationMapper;
import com.phearom.superreminder.model.realm.LocationRealm;
import com.phearom.superreminder.viewmodel.LocationViewModel;
import com.phearom.superreminder.viewmodel.LocationsViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmResults;

/**
 * Created by phearom on 5/19/16.
 */
public class LocationsFragment extends BaseFragment {
    private static LocationsFragment instance;
    private FragmentLocationsBinding mBinding;
    private LocationsViewModel mViewModels;

    public static LocationsFragment init() {
        if (null == instance)
            instance = new LocationsFragment();
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmResults<LocationRealm> locationRealms = RealmHelper.init(getContext()).getObject(LocationRealm.class);
        mBinding.getModels().setLocations(LocationMapper.toLocations(locationRealms));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_locations, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModels = new LocationsViewModel();
        mBinding.setModels(mViewModels);
        mBinding.setView(this);
    }

    @Override
    protected void onReceivedBroadcast(String action, Intent data) {
        super.onReceivedBroadcast(action, data);
        try {
            String id = data.getStringExtra("Id");
            JSONObject jsonObject = new JSONObject(data.getExtras().getString(IntentKeys.EXTRA_DATA));
            mBinding.getModels().findById(id).setDistance(jsonObject.getJSONObject("distance").getString("text"));
            mBinding.getModels().findById(id).setTimeLeft(jsonObject.getJSONObject("duration").getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ItemBinder<LocationViewModel> itemViewBinder() {
        return new CompositeItemBinder<>(
                new LocationBinder(BR.location, R.layout.item_location)
        );
    }
}
