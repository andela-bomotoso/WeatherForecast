package com.example.bukola_omotoso.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    private SharedPreferences sharedPreferences;
    private final String TEMPERATURE = "temperature";
    private  final String FORECAST_LOCATION = "forecast_location";
    private  final String FORECAST_NUMBER = "forecast_number";
    private boolean preferenceChanged = true;
    private List<Weather> weatherList = new ArrayList<>();
    private WeatherArrayAdapter weatherArrayAdapter;
    private ListView weatherListView;
    private URL url;
    private EditText locationEditText;
    private FloatingActionButton fab;
    private String displayType = "";
    private String temperature = "";
    private String forecastNum = "";
    private int forecastNumber = 0;
    private LocationDetector locationDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationEditText = (EditText)findViewById(R.id.locationEditText);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        weatherListView = (ListView)findViewById(R.id.weatherListView);
        weatherArrayAdapter = new WeatherArrayAdapter(this,weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        displayType = sharedPreferences.getString(FORECAST_LOCATION, Constants.DISPLAY_DEFAULT);
        temperature = sharedPreferences.getString(TEMPERATURE,Constants.DEFAULT_TEMPERATURE);
        forecastNum = sharedPreferences.getString(FORECAST_NUMBER,"");
        forecastNumber = Integer.parseInt(sharedPreferences.getString(FORECAST_NUMBER,"7"));

        Log.e("FORECAST_NUMBER",forecastNum);

        if (displayType .equals( Constants.DISPLAY_DEFAULT)) {
            fab.setVisibility(View.GONE);
            locationEditText.setVisibility(View.GONE);
            URL url = createURL(Constants.DISPLAY_LOCATION);
            Log.e("URL", url.toString());
            if (url != null) {
                // dismissKeyboard(locationEditText);
                Snackbar.make(findViewById(R.id.coordinator_layout), Constants.DISPLAY_LOCATION + " weather forecast", Snackbar.LENGTH_LONG).show();
                GetWeatherTask getWeatherTask = new GetWeatherTask();
                getWeatherTask.execute(url);
            } else {
                Snackbar.make(findViewById(R.id.coordinator_layout), R.string.invalid_url, Snackbar.LENGTH_LONG).show();

            }
        } else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    URL url = createURL(locationEditText.getText().toString());
                    Log.e("URL", url.toString());
                    if (url != null) {
                        dismissKeyboard(locationEditText);
                        Snackbar.make(findViewById(R.id.coordinator_layout), locationEditText.getText() + " weather forecast", Snackbar.LENGTH_LONG).show();
                        GetWeatherTask getWeatherTask = new GetWeatherTask();
                        getWeatherTask.execute(url);
                    } else {
                        Snackbar.make(findViewById(R.id.coordinator_layout), R.string.invalid_url, Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)   {

        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }


    private void dismissKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private URL createURL(String city)  {
        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.web_service_url);
        try {
            String temp = "";
            if(temperature .equals("Celsius"))  {
                temp = "metric";
            }else{
                temp = "imperial";
            }
            String urlString = baseUrl + URLEncoder.encode(city,"UTF-8") + "&units="+temp+"&cnt="+forecastNumber+"&APPID=" + apiKey;
            return new URL(urlString);
        }   catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject>   {

        @Override
        protected JSONObject doInBackground(URL... params)  {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();
                Log.e("RESPONSE_CODE",response+"");

                if(response == HttpURLConnection.HTTP_OK)   {

                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                        String line;
                        while ((line = bufferedReader.readLine()) != null)  {
                            builder.append(line);
                        }

                    }
                    catch (IOException e)   {
                        Snackbar.make(findViewById(R.id.coordinator_layout), R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    return new JSONObject(builder.toString());
                }   else    {
                    Snackbar.make(findViewById(R.id.coordinator_layout),R.string.connect_error,Snackbar.LENGTH_LONG).show();
                }
            }   catch (Exception e) {
                Snackbar.make(findViewById(R.id.coordinator_layout),R.string.connect_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weather)    {

            convertJSONtoArrayList(weather);
            weatherArrayAdapter.notifyDataSetChanged();
            weatherListView.smoothScrollToPosition(0);
        }
    }


    private void convertJSONtoArrayList(JSONObject forecast)    {
        weatherList.clear();
        try {
            JSONArray list = forecast.getJSONArray("list");
            for(int  i =0; i < list.length(); i++)  {
                JSONObject day =  list.getJSONObject(i);
                JSONObject temperatures = day.getJSONObject("temp");
                JSONObject weather = day.getJSONArray("weather").getJSONObject(0);
                weatherList.add(new Weather(
                        day.getLong("dt"),
                        temperatures.getDouble("min"),
                        temperatures.getDouble("max"),
                        day.getDouble("humidity"),
                        weather.getString("description"),
                        weather.getString("icon")));

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }





}
