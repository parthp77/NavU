package com.example.alex.myapplication;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static java.lang.Integer.parseInt;

public class ClassesActivity extends AppCompatActivity {
    private static final String ns = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        ArrayList<ClassObj> classList = new ArrayList<>();
        ArrayList<String> className = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        try {
             classList = parseXML(this);
        }

        catch(XmlPullParserException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        Log.d("ClassList size: ", ""+classList.size());
        for(int  k= 0; k < classList.size(); k++){
            className.add(classList.get(k).getClassName());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, className);
        ListView listView = (ListView) findViewById(R.id.class_list);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                Object selectedItem = adapter.getItemAtPosition(position);
                Intent intent = new Intent(ClassesActivity.this, ViewClassActivity.class);
                intent.putExtra("pos", position);
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.AddClass);
        final int size = classList.size();    //Position in list rather than size
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(ClassesActivity.this,
                        AddClassActivity.class);
                myIntent.putExtra("size", size);
                startActivity(myIntent);
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Parses through the classes.xml file and gets the start
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
        parser.require(XmlPullParser.START_TAG, ns, "classList");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name= parser.getName();
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
            if (name.equals("name")) {
                className = readText(parser);
            } else if (name.equals("day1")) {
                days.add(readText(parser));
            } else if (name.equals("day2")) {
                days.add(readText(parser));
            } else if (name.equals("startTime")) {
                classTime = readText(parser);
            } else if (name.equals("roomString")) {
                room = readText(parser);
            }
             else {
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
