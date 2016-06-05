package com.phearom.api.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public abstract class BaseGMapRequest {
    private RequestQueue queue;
    private Context mContext;
    private OnResponseCallback mOnResponseCallback;

    public BaseGMapRequest(Context context) {
        mContext = context;
    }

    protected String onResponseData(String json) throws Exception {
        return new JSONObject("data").toString();
    }

    public void setOnResponseCallback(OnResponseCallback onResponseCallback) {
        mOnResponseCallback = onResponseCallback;
    }

    public RequestQueue getQueue() {
        if (queue == null)
            queue = Volley.newRequestQueue(mContext);
        return queue;
    }

    public int getMethod() {
        return Request.Method.GET;
    }

    public abstract String getRequestUrl();


    public void execute() {
        StringRequest request = new StringRequest(getMethod(), getRequestUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (mOnResponseCallback != null)
                    try {
                        mOnResponseCallback.onSuccess(onResponseData(response));
                    } catch (Exception e) {
                        mOnResponseCallback.onSuccess(response);
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mOnResponseCallback != null)
                    mOnResponseCallback.onError(error);
            }
        });
        getQueue().add(request);
    }
}