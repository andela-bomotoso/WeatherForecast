package com.example.bukola_omotoso.weatherforecast;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        radioGroup = (RadioGroup)findViewById(R.id.displayGroup);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        sharedPreferenceManager = new SharedPreferenceManager(this);
        if(sharedPreferenceManager.retrieveDisplayType() == Constants.DISPLAY_CUSTOM)   {
            radioGroup.check(R.id.custom_location);
        }   else        {
            radioGroup.check(R.id.current_location);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.current_location)  {
                    sharedPreferenceManager.saveDisplayOption(Constants.DISPLAY_DEFAULT);
                }   else    {
                    sharedPreferenceManager.saveDisplayOption(Constants.DISPLAY_CUSTOM);
                }
            }
        });
    }



}
