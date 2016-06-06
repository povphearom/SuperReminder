package com.phearom.api.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.phearom.api.repositories.defaultmodel.CountryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phearom on 1/19/16.
 */
public class DataUtils {
    private static Gson mGSon;

    private static Gson getGSon() {
        if (mGSon == null)
            mGSon = new Gson();
        return mGSon;
    }

    //Object Mapper
    public static String objectToJSString(Object obj) {
        return getGSon().toJson(obj);
    }

    public static <T> T jsonToObject(Class<T> clazz, String json) {
        return getGSon().fromJson(json, clazz);
    }

    public static List<CountryModel> getCountries(String json) {
        try {
            return getGSon().fromJson(json, new TypeToken<List<CountryModel>>() {
            }.getType());
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}
