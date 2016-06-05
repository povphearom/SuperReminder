package com.phearom.api.repositories.models;

/**
 * Created by phearom on 5/11/16.
 */
public class CountryModel {
    private double longitude_average;
    private double latitude_average;
    private String alpha_2_code;

    private int numeric_code;

    private String alpha_3_code;
    private String country;

    public double getLongitude_average() {
        return longitude_average;
    }

    public void setLongitude_average(double longitude_average) {
        this.longitude_average = longitude_average;
    }

    public double getLatitude_average() {
        return latitude_average;
    }

    public void setLatitude_average(double latitude_average) {
        this.latitude_average = latitude_average;
    }

    public String getAlpha_2_code() {
        return alpha_2_code;
    }

    public void setAlpha_2_code(String alpha_2_code) {
        this.alpha_2_code = alpha_2_code;
    }

    public int getNumeric_code() {
        return numeric_code;
    }

    public void setNumeric_code(int numeric_code) {
        this.numeric_code = numeric_code;
    }

    public String getAlpha_3_code() {
        return alpha_3_code;
    }

    public void setAlpha_3_code(String alpha_3_code) {
        this.alpha_3_code = alpha_3_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
