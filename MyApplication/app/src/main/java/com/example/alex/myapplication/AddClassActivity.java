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
import android.widget.CheckBox;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static android.R.attr.id;


public class AddClassActivity extends AppCompatActivity {
    private ClassObj c;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);

        Button b = (Button) findViewById(R.id.AddClassToList);
        b.setOnClickListener(new OnClickListener(){
            public void onClick(View view)
            {
                c = saveInfo();
                Bundle extras = new Bundle();
                extras.putString("className", c.getClassName());
                extras.putStringArrayList("weekDays",c.getWeekDays());
                extras.putString("startTime", c.getClassTime());

                Intent myIntent = new Intent(AddClassActivity.this, ClassesActivity.class);
                myIntent.putExtras(extras);
                startActivity(myIntent);
            }

        });
    }

    private ClassObj saveInfo(){
        String className = ((EditText)findViewById(R.id.classNameInput)).getText().toString();
        ArrayList<String> days = new ArrayList<String>();
        String startTime = ((EditText)findViewById(R.id.editText2)).getText().toString();

        CheckBox checkBox = (CheckBox) findViewById(R.id.classMonday);
        if(checkBox.isChecked())
            days.add("Monday");
        checkBox = (CheckBox) findViewById(R.id.classTuesday);
        if(checkBox.isChecked())
            days.add("Tuesday");
        checkBox = (CheckBox) findViewById(R.id.classWednesday);
        if(checkBox.isChecked())
            days.add("Wednesday");
        checkBox = (CheckBox) findViewById(R.id.classThursday);
        if(checkBox.isChecked())
            days.add("Thursday");
        checkBox = (CheckBox) findViewById(R.id.classFriday);
        if(checkBox.isChecked())
            days.add("Friday");

        ClassObj newClass = new ClassObj(className, startTime, days);
        return newClass;

        /*
        try{
        //Load xml from file
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
        Document document = domBuilder.parse(file);

            NodeList nodes = document.getElementsByTagName("name");

        //Save xml to file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(file));
        }
        catch(Exception e){
        }

        SharedPreferences sp = getPreferences(R.xml.classes);
        SharedPreferences.Editor prefEditor = sp.edit();
        prefEditor.putString("", "");
        prefEditor.commit();

        */
    }



}
