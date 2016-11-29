package com.example.alex.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
            String timeToParse = classList.get(k).getClassTime();
            String[] hourMin = timeToParse.split(":");
            String classRoom = classList.get(k).getRoom();
            ArrayList<String> days = classList.get(k).getWeekDays();
            final Calendar myCalendar = Calendar.getInstance();
            myCalendar.set(Calendar.HOUR, parseInt(hourMin[0]));
            myCalendar.set(Calendar.MINUTE, parseInt(hourMin[1]));
            myCalendar.set(Calendar.SECOND, 0);
            myCalendar.set(Calendar.AM_PM, Calendar.PM);
            //if(Calendar.HOUR <= 7)myCalendar.set(Calendar.AM_PM, Calendar.PM);
            //else myCalendar.set(Calendar.AM_PM, Calendar.AM);

            for(int d = 0; d < days.size(); d++){
                Log.d("days values:", days.get(d));
                if(days.get(d).equals("Monday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);}
                else if(days.get(d).equals("Tuesday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);}
                else if(days.get(d).equals("Wednesday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);}
                else if(days.get(d).equals("Thursday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);}
                else if(days.get(d).equals("Friday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);}

                Log.d("Calendar time:", myCalendar.getTime().toString());
                scheduleNotification(getNotification("Class begins shortly. Click here to get your directions.",classRoom),myCalendar);
            }
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
        final int size = classList.size();
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(ClassesActivity.this,
                        AddClassActivity.class);
                myIntent.putExtra("size", size);
                startActivity(myIntent);
            }
        });
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


    //Notification functions

    /** This function will set an alarm to push a notification to the user if they wish to be
     *  notified that their class will be beginning shortly
     *
     * @param notification  The notification to be pushed
     * @param classTime     The time in which the notification will display to the user
     */
    private void scheduleNotification(Notification notification, Calendar classTime){
        Date date = new Date();
        //Calendar alarm = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        classTime.setTime(date);
        if (classTime.before(now)){
            classTime.add(Calendar.DATE, 1);
        }

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, classTime.getTimeInMillis(), pendingIntent);
    }

    /** Builds the notification using the preferences specified by the user
     *
     * @param content   The text of the notification
     * @return
     */
    private Notification getNotification(String content, String classroom){
        Intent myIntent = new Intent(this, MapActivity.class);
        myIntent.putExtra("roomString", classroom);
        Notification.Builder builder = new Notification.Builder(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_stat_new_message)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setStyle(new Notification.BigTextStyle()
                        .bigText("Directions")
                        .setBigContentTitle("NavU")
                        .setSummaryText("Get Directions to Class"))
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, myIntent, 0));

        //Setting the type of notification - User may only want vibrate notification and not sound
        String TAG = "Notification Setting";
        if(sp.getBoolean("sound", false) && sp.getBoolean("vibrate", false)){
            Log.d(TAG, "Sound And Vibrate");
            builder.setDefaults(Notification.DEFAULT_ALL);
        }
        else if(sp.getBoolean("vibrate", false)){
            Log.d(TAG, "Vibrate Only");
            builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        }
        else if(sp.getBoolean("sound", false)){
            Log.d(TAG, "Sound Only");
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
        }

        return builder.build();
    }
}
