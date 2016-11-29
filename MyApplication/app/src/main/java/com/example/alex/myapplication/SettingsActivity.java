package com.example.alex.myapplication;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.view.MenuItem;


/**
 * Created by mike on 2016-10-18.
 */

public class SettingsActivity extends PreferenceActivity {
    private final static String TAG = "Settings";

    public SettingsActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new LocationFragment()).commit();


    }

    public static class LocationFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            //SharedPreferences.Editor editor =

            addPreferencesFromResource(R.xml.preferences);

        }
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

