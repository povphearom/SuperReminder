package com.phearom.api.repositories;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmHelper {
    private static RealmHelper instance;
    private Context mContext;
    private RealmConfiguration mConfiguration;

    private RealmHelper(Context context) {
        mContext = context;
    }

    public static RealmHelper init(Context context) {
        if (null == instance)
            instance = new RealmHelper(context);
        return instance;
    }

    protected RealmConfiguration getConfiguration() {
        if (null == mConfiguration)
            mConfiguration = new RealmConfiguration.Builder(mContext).name("superreminder.realm").migration(new Migration()).schemaVersion(8).build();
        return mConfiguration;
    }

    public <T extends RealmObject> void addObject(T object) {
        Realm realm = Realm.getInstance(getConfiguration());
        realm.beginTransaction();
        realm.setAutoRefresh(true);
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }

    public <T extends RealmObject> void addObject(List<T> objects) {
        Realm realm = Realm.getInstance(getConfiguration());
        realm.beginTransaction();
        realm.setAutoRefresh(true);
        realm.copyToRealmOrUpdate(objects);
        realm.commitTransaction();
    }

    public <T extends RealmObject> RealmResults<T> getObject(Class<T> clazz) {
        Realm realm = Realm.getInstance(getConfiguration());
        return realm.where(clazz).findAll();
    }

    public <T extends RealmObject> void removeObject(RealmQuery<T> query) {
        Realm realm = Realm.getInstance(getConfiguration());
        realm.beginTransaction();
        realm.setAutoRefresh(true);
        query.findFirst().removeFromRealm();
        realm.commitTransaction();
    }

    public <T extends RealmObject> void removeObject(Class<T> clazz, String field, Object value) {
        Realm realm = Realm.getInstance(getConfiguration());
        realm.beginTransaction();
        realm.setAutoRefresh(true);
        RealmResults<T> data = realm.where(clazz).findAll();
        RealmQuery<T> query;
        if (value instanceof Integer)
            query = data.where().equalTo(field, (int) value);
        else if (value instanceof Boolean)
            query = data.where().equalTo(field, (boolean) value);
        else if (value instanceof Double)
            query = data.where().equalTo(field, (double) value);
        else if (value instanceof Float)
            query = data.where().equalTo(field, (float) value);
        else
            query = data.where().equalTo(field, (String) value);
        query.findFirst().removeFromRealm();
        realm.commitTransaction();
    }
}