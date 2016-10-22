package com.example.alex.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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
