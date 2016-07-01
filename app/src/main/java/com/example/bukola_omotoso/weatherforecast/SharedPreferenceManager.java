package com.example.bukola_omotoso.weatherforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by bukola_omotoso on 01/07/16.
 */
public class SharedPreferenceManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferenceManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void saveDisplayOption(String displayType)   {
        editor.putString("DISPLAY_TYPE",displayType);
        editor.commit();
    }

    public String retrieveDisplayType() {
        return sharedPreferences.getString(Constants.DISPLAY_TYPE,Constants.DISPLAY_DEFAULT);
    }

    public void saveLongitude(float longitude) {

        editor.putFloat(Constants.LONGITUDE, longitude);

        editor.commit();
    }

    public float retrieveLongitude() {

        return sharedPreferences.getFloat(Constants.LONGITUDE, 0);
    }

    public void saveLatitude(float longitude) {

        editor.putFloat(Constants.LATITUDE, longitude);

        editor.commit();
    }

    public float retrieveLatitude() {

        return sharedPreferences.getFloat(Constants.LATITUDE, 0);
    }

}
