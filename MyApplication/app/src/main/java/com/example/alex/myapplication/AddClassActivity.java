package com.example.alex.myapplication;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
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
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static java.lang.Integer.parseInt;


public class AddClassActivity extends AppCompatActivity {
    static final String xmlFile = "classes.xml";
    private ClassObj c;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final int size = intent.getIntExtra("size", 0);
        Button b = (Button) findViewById(R.id.AddClassToList);
        b.setOnClickListener(new OnClickListener(){
            public void onClick(View view)
            {

                Document myDoc = getDocument(getApplicationContext());
                c = readInfo();
                CreateNotification(c);
                saveInfoToDoc(myDoc, c, size);
                Intent myIntent = new Intent(AddClassActivity.this, ClassesActivity.class);
                startActivity(myIntent);
            }

        });
    }

    private void CreateNotification(ClassObj myClass){
        String className = myClass.getClassName();
        String timeToParse = myClass.getClassTime();
        String[] hourMin = timeToParse.split(":");
        String classRoom = myClass.getRoom();
        ArrayList<String> days = myClass.getWeekDays();
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        myCalendar.set(Calendar.HOUR_OF_DAY, parseInt(hourMin[0]));
        myCalendar.set(Calendar.MINUTE, parseInt(hourMin[1]));
        myCalendar.set(Calendar.SECOND, 0);
        if(parseInt(hourMin[0])<=7)myCalendar.set(Calendar.AM_PM, Calendar.PM);
        else myCalendar.set(Calendar.AM_PM, Calendar.AM);
        //myCalendar.set(Calendar.AM_PM, Calendar.PM);

        for(int d = 0; d < days.size(); d++){
            Log.d("days values:", days.get(d));
            if(days.get(d).equals("Monday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);}
            else if(days.get(d).equals("Tuesday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);}
            else if(days.get(d).equals("Wednesday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);}
            else if(days.get(d).equals("Thursday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);}
            else if(days.get(d).equals("Friday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);}

            Log.d("Calendar time:", myCalendar.getTime().toString());
            scheduleNotification(getNotification(className+" begins shortly. Click here to get your directions."
                    ,classRoom),myCalendar);
        }
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


    //Notification functions

    /** This function will set an alarm to push a notification to the user if they wish to be
     *  notified that their class will be beginning shortly
     *
     * @param notification  The notification to be pushed
     * @param classTime     The time in which the notification will display to the user
     */
    private void scheduleNotification(Notification notification, Calendar classTime){
        Date date = new Date();
        Calendar now = Calendar.getInstance();
        Calendar weekAhead = Calendar.getInstance();
        weekAhead.setTime(date);
        weekAhead.add(Calendar.DATE, 7);
        now.setTime(date);
        if (classTime.before(now)){
            classTime.add(Calendar.DATE, 1);
        }
        //classTime.setTime(date);
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1253, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, classTime.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, classTime.getTimeInMillis(),weekAhead.getTimeInMillis() ,pendingIntent);
    }

    /** Builds the notification using the preferences specified by the user
     *
     * @param content   The text of the notification
     * @return
     */
    private Notification getNotification(String content, String classroom){
        Intent myIntent = new Intent(this, MainActivity.class);
        Log.d("roomString: ", classroom);
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

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


