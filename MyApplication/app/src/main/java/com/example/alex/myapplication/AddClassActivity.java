package com.example.alex.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
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
        Intent intent = getIntent();
        final int size = intent.getIntExtra("size", 0);
        Button b = (Button) findViewById(R.id.AddClassToList);
        b.setOnClickListener(new OnClickListener(){
            public void onClick(View view)
            {

                Document myDoc = getDocument(getApplicationContext());
                c = readInfo();
                saveInfoToDoc(myDoc, c, size);
                Intent myIntent = new Intent(AddClassActivity.this, ClassesActivity.class);
                startActivity(myIntent);
            }

        });
    }

    private ClassObj readInfo() {
        String className = ((EditText) findViewById(R.id.classNameInput)).getText().toString();
        ArrayList<String> days = new ArrayList<String>();
        String startTime = ((EditText) findViewById(R.id.editText2)).getText().toString();
        String roomString  =((EditText)findViewById(R.id.classRoomInput)).getText().toString();
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
        return new ClassObj(className, startTime, days, roomString);
    }

    private void saveInfoToDoc(Document xmlDoc, ClassObj c, int position){
        //Appends the information to a new Element
        Element Class = xmlDoc.getDocumentElement();
        Element newClass = xmlDoc.createElement("class");
        Element newName = xmlDoc.createElement("name");
        Element newDay1 = xmlDoc.createElement("day1");
        Element newDay2 = xmlDoc.createElement("day2");
        Element StartTime = xmlDoc.createElement("startTime");
        Element roomString = xmlDoc.createElement("roomString");


        ArrayList<String> days = new ArrayList<>();
        days = c.getWeekDays();
        newName.appendChild(xmlDoc.createTextNode(c.getClassName()));
        newDay1.appendChild(xmlDoc.createTextNode(days.get(0)));
        newDay2.appendChild(xmlDoc.createTextNode(days.get(1)));
        StartTime.appendChild(xmlDoc.createTextNode(c.getClassTime()));
        roomString.appendChild(xmlDoc.createTextNode(c.getRoom()));

        newClass.appendChild(newName);
        newClass.appendChild(newDay1);
        newClass.appendChild(newDay2);
        newClass.appendChild(StartTime);
        newClass.appendChild(roomString);

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
        Document document = null;
        try{
            File file = new File(getApplicationContext().getFilesDir(), "classes.xml");
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
            if(file.exists())
                document = domBuilder.parse(file);
            else{
                document = domBuilder.newDocument();
                Element Class = document.createElement("classList");
                document.appendChild(Class);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return document;
    }
}


