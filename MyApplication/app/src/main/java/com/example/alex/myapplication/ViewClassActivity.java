package com.example.alex.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mikes on 2016-11-14.
 */

public class ViewClassActivity extends AppCompatActivity{
    private static final String ns = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editclass);
        ArrayList<ClassObj> c = new ArrayList<>();
        Intent intent = getIntent();
        int position = intent.getIntExtra("pos", 0);
        Log.d("Selected Position", ""+position);
        try{
            c = parseXML(this);
        }
        catch(XmlPullParserException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        displayClass(c.get(position));

        Button button = (Button) findViewById(R.id.RemoveClass);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(ViewClassActivity.this,
                        ClassesActivity.class);
                //myIntent.putExtra("name", ((EditText)findViewById(R.id.classNameOutput)).getText().toString());
                startActivity(myIntent);
            }
        });

    }
    private void displayClass(ClassObj c){
        EditText className = (EditText)findViewById(R.id.classNameOutput);
        EditText classRoom = (EditText)findViewById(R.id.classRoomOutput);
        CheckBox monday = (CheckBox)findViewById(R.id.classMondayOut);
        CheckBox tuesday = (CheckBox)findViewById(R.id.classTuesdayOut);
        CheckBox wednesday = (CheckBox)findViewById(R.id.classWednesdayOut);
        CheckBox thursday = (CheckBox)findViewById(R.id.classThursdayOut);
        CheckBox friday = (CheckBox)findViewById(R.id.classFridayOut);
        EditText time = (EditText)findViewById(R.id.StartTimeOutput);

        className.setText(c.getClassName());
        classRoom.setText(c.getRoom());
        time.setText(c.getClassTime());
        ArrayList<String> days = c.getWeekDays();
        for(int i = 0; i < days.size(); i++){
            if(days.get(i).equals("Monday")){monday.setChecked(true);}
            else if(days.get(i).equals("Tuesday")){tuesday.setChecked(true);}
            else if(days.get(i).equals("Wednesday")){wednesday.setChecked(true);}
            else if(days.get(i).equals("Thursday")){thursday.setChecked(true);}
            else if(days.get(i).equals("Friday")){friday.setChecked(true);}

        }

    }

    /**
     * This will read the xml and display the previously inputted class details
     * @param context
     */
    private ArrayList<ClassObj> parseXML(Context context)
            throws XmlPullParserException, IOException{
        ArrayList<ClassObj> classList = new ArrayList<ClassObj>();
        try {
            InputStream file = context.openFileInput("classes.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(file, null);
            parser.nextTag();
            classList = readClassList(parser);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return classList;
    }

    private ArrayList<ClassObj> readClassList(XmlPullParser parser)throws XmlPullParserException, IOException{
        ArrayList<ClassObj> entries = new ArrayList<ClassObj>();
        ClassObj c;
        int count =0;
        parser.require(XmlPullParser.START_TAG, ns, "classList");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name= parser.getName();
            //Log.d("CurrParserName:" , name);
            if(name.equals("class")){
                entries.add(readClass(parser));
            }

            else{
                skip(parser);
            }
        }
        return entries;
    }

    private ClassObj readClass(XmlPullParser parser) throws  XmlPullParserException, IOException{

        parser.require(XmlPullParser.START_TAG, ns, "class");
        String className="", classTime = "", room="";
        ArrayList<String> days = new ArrayList<>();
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("name")){
                className = readText(parser);
            }
            else if(name.equals("day1")){
                days.add(readText(parser));
            }
            else if (name.equals("day2")){
                days.add(readText(parser));
            }
            else if (name.equals("startTime")){
                classTime = readText(parser);
            }
            else if (name.equals("roomString")){
                room = readText(parser);
            }
            else{
                skip(parser);
            }
        }
        return (new ClassObj(className,classTime,days,room));
    }


    private String readText(XmlPullParser parser) throws  XmlPullParserException, IOException{
        String result = "";
        if(parser.next() == XmlPullParser.TEXT){
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
