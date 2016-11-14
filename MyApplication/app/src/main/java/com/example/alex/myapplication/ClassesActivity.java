package com.example.alex.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

import static com.example.alex.myapplication.R.xml.classes;

public class ClassesActivity extends AppCompatActivity {
     String[] listEle = new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        listEle = fillList();
        //String [] listEle  = {"C1","C2","C3","C4","C5"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, listEle);

        ListView listView = (ListView) findViewById(R.id.class_list);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                Object selectedItem = adapter.getItemAtPosition(position);
                Intent intent = new Intent(ClassesActivity.this, AddClassActivity.class);
                startActivity(intent);

            }
        });
        listView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.AddClass);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(ClassesActivity.this,
                        AddClassActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private String[] fillList(){
        //String[] list = new String[5];
        String[] list  = {"COMP9001","COMP3000","COMP3004","ENG1000","ELEC1001"};
        SharedPreferences sp = ClassesActivity.this.getSharedPreferences("saved_classes", 0);
        Log.v("Tag", sp.getString("class1", list[0]));
        //list[1] = sp.getString("Class2", "temp2");

        return list;

    }

}
