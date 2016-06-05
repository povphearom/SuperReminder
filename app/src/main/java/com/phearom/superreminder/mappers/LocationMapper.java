package com.phearom.superreminder.mappers;

import com.phearom.api.repositories.models.LocationRealm;
import com.phearom.superreminder.model.Location;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by phearom on 5/19/16.
 */
public class LocationMapper {
    public static Location toLocation(LocationRealm realm) {
        Location location = new Location();
        location.setId(realm.getId());
        location.setAlert(realm.isAlert());
        location.setLat(realm.getLat());
        location.setLng(realm.getLng());
        location.setName(realm.getName());
        return location;
    }

    public static List<Location> toLocations(RealmResults<LocationRealm> realms) {
        List<Location> locations = new ArrayList<>();
        Location location;
        for (LocationRealm realm : realms) {
            location = new Location();
            location.setId(realm.getId());
            location.setAlert(realm.isAlert());
            location.setLat(realm.getLat());
            location.setLng(realm.getLng());
            location.setName(realm.getName());
            locations.add(location);
        }
        return locations;
    }

    public static LocationRealm toLocationRealm(Location location) {
        LocationRealm locationRealm = new LocationRealm();
        locationRealm.setId(location.getId());
        locationRealm.setAlert(location.isAlert());
        locationRealm.setLat(location.getLat());
        locationRealm.setLng(location.getLng());
        locationRealm.setName(location.getName());
        return locationRealm;
    }

    public static RealmList<LocationRealm> toLocationsRealm(List<Location> locations) {
        RealmList<LocationRealm> locationsRealm = new RealmList<>();
        LocationRealm locationRealm;
        for (Location location : locations) {
            locationRealm = new LocationRealm();
            locationRealm.setId(location.getId());
            locationRealm.setAlert(location.isAlert());
            locationRealm.setLat(location.getLat());
            locationRealm.setLng(location.getLng());
            locationRealm.setName(location.getName());
            locationsRealm.add(toLocationRealm(location));
        }
        return locationsRealm;
    }
}
