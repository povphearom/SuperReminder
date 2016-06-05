package com.phearom.api.request;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

/**
 * Created by phearom on 5/12/16.
 */
public class RequestGetDistance extends BaseGMapRequest {
    private static final String URL = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=%s&language=en-EN&sensor=false";
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

    private String urlRequest = null;

    public RequestGetDistance(Context context) {
        super(context);
    }

    public void setParams(LatLng start, LatLng end, String mode) {
        urlRequest = String.format(URL, start.latitude, start.longitude, end.latitude, end.longitude, mode);
    }

    @Override
    protected String onResponseData(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject data = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
        return data.toString();
    }

    @Override
    public String getRequestUrl() {
        Log.i("URL",urlRequest);
        return urlRequest;
    }
}
