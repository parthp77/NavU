package com.example.alex.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;


public class AddClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);

        Button b = (Button) findViewById(R.id.AddClassToList);
        b.setOnClickListener(new OnClickListener(){
            public void onClick(View view)
            {
                saveInfo();
                Intent myIntent = new Intent(AddClassActivity.this, ClassesActivity.class);
                startActivity(myIntent);
            }

        });
    }

    private void saveInfo(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = sp.edit();
        prefEditor.putString("", "Comp 9000");
        prefEditor.commit();


    }



}
