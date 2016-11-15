package com.example.alex.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.lang.Class;

public class ClassesActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<ClassObj> classList = new ArrayList<ClassObj>();
        ArrayList<String> className = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            ClassObj c = new ClassObj(extras.getString("className"),extras.getString("startTime"),extras.getStringArrayList("weekDays"));
            classList.add(c);
        }
        /*
        Intent i = getIntent();
        if(i.getStringExtra("name") != null){
            for(int j = 0; j < classList.size(); j++){
                if(classList.get(j).getClassName() == i.getStringExtra("name")){
                    classList.remove(j);
                }
            }
        }*/

        try {
            parseXML(this, classList);
        }
        catch(XmlPullParserException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        for(int  k= 0; k < classList.size(); k++){
            className.add(classList.get(k).getClassName());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, className);



        ListView listView = (ListView) findViewById(R.id.class_list);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                Object selectedItem = adapter.getItemAtPosition(position);


                Intent intent = new Intent(ClassesActivity.this, EditClassActivity.class);
                intent.putExtra("pos", position);
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

    /**
     * Parses through the classes.xml file and gets the start
     * @param context
     * @param classList
     */
    private void parseXML(Context context, ArrayList<ClassObj> classList)
    throws XmlPullParserException, IOException{
        int eventType = -1;
        String n = "";
        String time = "";
        ArrayList<String> days= new ArrayList<String>();
        XmlResourceParser parser = context.getResources().getXml(R.xml.classes);
        parser.next();
        eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                Log.d(TAG, "Start Doc");
            }

                if(eventType == XmlPullParser.TEXT ){

                    //Log.d(TAG,parser.getText());
                    n= parser.getText();
                    parser.next();
                    days.add(parser.getText());
                    parser.next();
                    days.add(parser.getText());
                    parser.next();
                    time = parser.getText();
                    if(n.startsWith("COMP") || n.startsWith("ELEC") || n.startsWith("BUSI")
                           ||n.startsWith("comp") || n.startsWith("elec") || n.startsWith("busi") ){
                        ClassObj c = new ClassObj(n, time, days);
                        classList.add(c);
                    }


                }

            eventType = parser.next();
        }
        parser.close();

        /*
        try{

            InputStream inputFile = context.getResources().openRawResource(R.raw.classes);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();


            Element el = doc.getElementById("ClassList");
            NodeList list = el.getElementsByTagName("class");
            for(int i = 0; i < list.getLength(); i++){
                Node n = list.item(i);
                Element e = (Element) n;

                String name = "";
                String time = "";
                ArrayList<String> days= new ArrayList<String>();


                for(int k =0; k < e.getElementsByTagName("name").getLength(); k++){
                    name = e.getElementsByTagName("name").item(k).getTextContent();
                    time = e.getElementsByTagName("startTime").item(k).getTextContent();
                    days.add(e.getElementsByTagName("day1").item(k).getTextContent());
                    days.add(e.getElementsByTagName("day2").item(k).getTextContent());
                }

                ClassObj c = new ClassObj(name, time, days);
                classList.add(c);
            }


        }
        catch(Exception e){
            e.printStackTrace();
        }*/
    }




}
