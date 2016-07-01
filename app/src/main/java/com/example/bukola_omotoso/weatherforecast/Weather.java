package com.example.bukola_omotoso.weatherforecast;

import java.net.PortUnreachableException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by bukola_omotoso on 24/06/16.
 */
public class Weather {
    public final String dayOfWeek;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconURL;

    public Weather(long timeStamp, double minTemp, double maxTemp, double humidity, String description, String iconName)    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(0);
        this.dayOfWeek = convertTimeStampToDay(timeStamp);
        convertTimeStampToDay(System.currentTimeMillis());
        this.minTemp = numberFormat.format(minTemp);
        this.maxTemp = numberFormat.format(maxTemp);
        this.humidity = numberFormat.getPercentInstance().format(humidity/100);
        this.description = description;
        this.iconURL = "http://openweathermap.org/img/w/" + iconName + ".png";
    }

    private static String convertTimeStampToDay(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        TimeZone timezone = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, timezone.getOffset(calendar.getTimeInMillis()));

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE");
        return dateFormatter.format(calendar.getTime());
    }



}
