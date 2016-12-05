package com.example.alex.myapplication;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Notification Type: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent myIntent = getIntent();
        String Endroom ="";
        Log.d("Intent has extra: ", ""+myIntent.hasExtra("roomString"));
        if(myIntent.hasExtra("roomString")) {
            Endroom = myIntent.getExtras().getString("roomString");

            Log.d("Extra: ",Endroom);

                EditText editText = (EditText) findViewById(R.id.text_edit_end_room);

                editText.setText(Endroom);
            }

        Building b = new Building(this, "HP");
        final Context context = getApplicationContext();


        Button button = (Button) findViewById(R.id.button_settings);

                button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        SettingsActivity.class);
                startActivity(myIntent);
            }
        });

        button = (Button) findViewById(R.id.button_about);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        AboutActivity.class);
                startActivity(myIntent);
            }
        });


        button = (Button) findViewById(R.id.button_search);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        MapActivity.class);

                EditText searchBar = (EditText) findViewById(R.id.text_edit_end_room);
                String end = searchBar.getText().toString();
                EditText startBar = (EditText) findViewById(R.id.text_edit_start_room);
                String start = startBar.getText().toString();

                myIntent.putExtra("roomString", end);
                myIntent.putExtra("startRoom", start);
                startActivity(myIntent);
            }
        });


        button = (Button) findViewById(R.id.button_schedule);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        ClassesActivity.class);
                startActivity(myIntent);
            }
        });
    }


}
