package com.example.alex.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void settings(View view) {
        setContentView(R.layout.activity_settings);
    }

}
