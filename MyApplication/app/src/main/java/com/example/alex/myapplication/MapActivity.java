package com.example.alex.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private Building building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        String test = "ML472";
        loadData(test);
    }

    private void loadData(String room)
    {
        String build = room.substring(0,2);
        short floor = Short.parseShort(room.substring(2,3));
        short roomN = Short.parseShort(room.substring(3));

        building = new Building(build);
    }

}
