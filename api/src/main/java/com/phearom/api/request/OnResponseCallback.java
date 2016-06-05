package com.phearom.api.request;

import com.android.volley.VolleyError;

/**
 * Created by phearom on 5/12/16.
 */
public interface OnResponseCallback {
    void onSuccess(String response);

    void onError(VolleyError error);
}
