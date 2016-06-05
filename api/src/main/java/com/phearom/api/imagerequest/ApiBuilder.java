package com.phearom.api.imagerequest;

import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by phearom on 6/4/16.
 */
public class ApiBuilder {
    private static ApiBuilder _builder;
    private DrawableTypeRequest _drawableTypeRequest;

    private ApiBuilder(DrawableTypeRequest drawableTypeRequest) {
        this._drawableTypeRequest = drawableTypeRequest;
    }

    public static ApiBuilder init(DrawableTypeRequest drawableTypeRequest) {
        if (_builder == null)
            _builder = new ApiBuilder(drawableTypeRequest);
        return _builder;
    }

    public ApiBuilder listener(RequestListener listener) {
        this._drawableTypeRequest.listener(listener);
        return this;
    }

    public ApiBuilder thumbnail(DrawableRequestBuilder drawableRequestBuilder) {
        this._drawableTypeRequest.thumbnail(drawableRequestBuilder);
        return this;
    }

    public Target into(Target target) {
        return this._drawableTypeRequest.into(target);
    }

    public Target<GlideDrawable> into(ImageView imageView) {
        return this._drawableTypeRequest.into(imageView);
    }
}
