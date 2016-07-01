package com.example.bukola_omotoso.weatherforecast;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;

/**
 * Created by bukola_omotoso on 01/07/16.
 */
public class LocationDetector {

    public LocationDetector() {

    }

    public static String fetchStreetName(Context context, double latitude, double longitude) {

        String street = "";
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        }
        catch (Exception exception) {

        }
        if(addresses != null && addresses.size() > 0 ){

            Address address = addresses.get(0);
            street = address.getThoroughfare()+ " "+address.getAdminArea();
        }

        return street;
    }
}
