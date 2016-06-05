package com.phearom.api.core.feature;

import android.content.Context;
import android.net.Uri;

import com.phearom.api.repositories.RealmHelper;
import com.phearom.api.repositories.models.LocationRealm;

import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by phearom on 5/17/16.
 */
public class LocationReminder extends Reminder {
    public LocationReminder(Context context) {
        super(context);
    }

    public void addLoc(LocationRealm loc){
        RealmHelper.init(getContext()).addObject(loc);
    }

    public void removeLoc(double lat,double lng){
        RealmResults<LocationRealm> realmResults = RealmHelper.init(getContext()).getObject(LocationRealm.class).where().findAll();
        RealmQuery<LocationRealm> realmQuery= realmResults.where().equalTo("lat",lat).equalTo("lng",lng);
        RealmHelper.init(getContext()).removeObject(realmQuery);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setAlertTime(int d) {

    }

    @Override
    public void setAlertSound(Uri uri) {

    }
}
