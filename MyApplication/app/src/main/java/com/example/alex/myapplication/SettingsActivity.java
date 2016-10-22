package com.example.alex.myapplication;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
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

        private final static String TAG = "LocationFragment";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }
    }

}

