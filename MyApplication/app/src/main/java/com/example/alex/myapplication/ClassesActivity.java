package com.example.alex.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ClassesActivity extends AppCompatActivity {
    String[] listEle = {"class1", "class2", "class3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, listEle);

        ListView listView = (ListView) findViewById(R.id.class_list);
        listView.setAdapter(adapter);
    }

}
