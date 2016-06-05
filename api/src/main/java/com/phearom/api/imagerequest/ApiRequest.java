package com.phearom.api.imagerequest;

import android.net.Uri;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.RequestManager;

import java.io.File;

/**
 * Created by phearom on 6/4/16.
 */
public class ApiRequest {
    private DrawableTypeRequest _drawableTypeRequest;
    private RequestManager _requestManager;

    public ApiRequest(RequestManager requestManager) {
        this._requestManager = requestManager;
    }

    public ApiBuilder load(File file) {
        this._drawableTypeRequest = this._requestManager.load(file);
        return ApiBuilder.init(_drawableTypeRequest);
    }

    public ApiBuilder load(String string) {
        this._drawableTypeRequest = this._requestManager.load(string);
        return ApiBuilder.init(_drawableTypeRequest);
    }

    public ApiBuilder load(Integer resourceId) {
        this._drawableTypeRequest = this._requestManager.load(resourceId);
        return ApiBuilder.init(_drawableTypeRequest);
    }

    public ApiBuilder load(byte[] bytes) {
        this._drawableTypeRequest = this._requestManager.load(bytes);
        return ApiBuilder.init(_drawableTypeRequest);
    }

    public <T> ApiBuilder load(T model) {
        this._drawableTypeRequest = this._requestManager.load(model);
        return ApiBuilder.init(_drawableTypeRequest);
    }

    public ApiBuilder load(Uri uri) {
        this._drawableTypeRequest = this._requestManager.load(uri);
        return ApiBuilder.init(_drawableTypeRequest);
    }

    public void pauseRequests() {
        this._requestManager.pauseRequests();
    }

    public void resumeRequests() {
        this._requestManager.resumeRequests();
    }
}
