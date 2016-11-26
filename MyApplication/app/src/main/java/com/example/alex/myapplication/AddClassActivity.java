package com.example.alex.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



public class AddClassActivity extends AppCompatActivity {
    static final String xmlFile = "classes.xml";
    private ClassObj c;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);
        Log.d("MYTAG", getFilesDir().toString());


        Button b = (Button) findViewById(R.id.AddClassToList);
        b.setOnClickListener(new OnClickListener(){
            public void onClick(View view)
            {

                Document myDoc = getDocument(getApplicationContext());
                c = readInfo();
                saveInfoToDoc(myDoc, c);

                /*Bundle extras = new Bundle();
                extras.putString("className", c.getClassName());
                extras.putStringArrayList("weekDays",c.getWeekDays());
                extras.putString("startTime", c.getClassTime());*/

                Intent myIntent = new Intent(AddClassActivity.this, ClassesActivity.class);
                //myIntent.putExtras(extras);
                startActivity(myIntent);
            }

        });
    }

    private ClassObj readInfo() {
        String className = ((EditText) findViewById(R.id.classNameInput)).getText().toString();
        ArrayList<String> days = new ArrayList<String>();
        String startTime = ((EditText) findViewById(R.id.editText2)).getText().toString();

        CheckBox checkBox = (CheckBox) findViewById(R.id.classMonday);
        if (checkBox.isChecked())
            days.add("Monday");
        checkBox = (CheckBox) findViewById(R.id.classTuesday);
        if (checkBox.isChecked())
            days.add("Tuesday");
        checkBox = (CheckBox) findViewById(R.id.classWednesday);
        if (checkBox.isChecked())
            days.add("Wednesday");
        checkBox = (CheckBox) findViewById(R.id.classThursday);
        if (checkBox.isChecked())
            days.add("Thursday");
        checkBox = (CheckBox) findViewById(R.id.classFriday);
        if (checkBox.isChecked())
            days.add("Friday");
        return new ClassObj(className, startTime, days);
    }


        /*
        //Save xml to file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(file));

        SharedPreferences sp = getPreferences(classes);
        SharedPreferences.Editor prefEditor = sp.edit();
        prefEditor.putString("", "");
        prefEditor.apply();
        */
    private void saveInfoToDoc(Document xmlDoc, ClassObj c){
        //Appends the information to a new Element
        Element Class = xmlDoc.getDocumentElement();
        Element newClass = xmlDoc.createElement("class");
        Element newName = xmlDoc.createElement("name");
        Element newDay1 = xmlDoc.createElement("day1");
        Element newDay2 = xmlDoc.createElement("day2");
        Element StartTime = xmlDoc.createElement("startTime");

        ArrayList<String> days = new ArrayList<>();
        days = c.getWeekDays();
        newName.appendChild(xmlDoc.createTextNode(c.getClassName()));
        newDay1.appendChild(xmlDoc.createTextNode(days.get(0)));
        newDay2.appendChild(xmlDoc.createTextNode(days.get(1)));
        StartTime.appendChild(xmlDoc.createTextNode(c.getClassTime()));

        newClass.appendChild(newName);
        newClass.appendChild(newDay1);
        newClass.appendChild(newDay2);
        newClass.appendChild(StartTime);
        Class.appendChild(newClass);

        //Save to xml
        try {
            DOMSource source = new DOMSource(xmlDoc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(openFileOutput(xmlFile, Context.MODE_PRIVATE));
            transformer.transform(source, result);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Gets the classes.xml file
     * @return
     */
    private Document getDocument(Context context){
        //Error is that cannot read from xmlFile, doc is always null
        Document document = null;
        try{
            //Load xml
            String filename = "classes.xml";
            AssetManager assetManager = this.getAssets();
            InputStream file = assetManager.open(filename);
            //FileInputStream file= fileDescriptor.createInputStream();

            //FileInputStream file = context.openFileInput(xmlFile);
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
            document = domBuilder.parse(file);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Log.d("Document: ",""+(document == null));
        return document;

    }


}


