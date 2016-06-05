package com.phearom.api.imagerequest;

import android.app.Fragment;
import android.content.Context;

import com.bumptech.glide.Glide;

/**
 * Created by phearom on 6/4/16.
 */
public class Api {
    private static Api _api;
    private ApiRequest _apiRequest;

    private Api(Context context) {
        _apiRequest = new ApiRequest(Glide.with(context));
    }

    private Api(Fragment fragment) {
        _apiRequest = new ApiRequest(Glide.with(fragment));
    }

    public static ApiRequest init(Context context) {
        if (_api == null)
            _api = new Api(context);
        return _api._apiRequest;
    }

    public static ApiRequest init(Fragment fragment) {
        if (_api == null)
            _api = new Api(fragment);
        return _api._apiRequest;
    }
}

