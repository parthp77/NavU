package com.example.alex.myapplication;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import org.w3c.dom.Attr;
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
        Log.d("AddClass Size: ", ""+size);
        Button b = (Button) findViewById(R.id.AddClassToList);
        b.setOnClickListener(new OnClickListener(){
            public void onClick(View view)
            {

                Document myDoc = getDocument(getApplicationContext());
                c = readInfo();
                CreateNotification(c, size);
                saveInfoToDoc(myDoc, c, size);
                Intent myIntent = new Intent(AddClassActivity.this, ClassesActivity.class);
                startActivity(myIntent);
            }

        });
    }

    private void CreateNotification(ClassObj myClass, int size){
        String className = myClass.getClassName();
        String timeToParse = myClass.getClassTime();
        String[] hourMin = timeToParse.split(":");
        String classRoom = myClass.getRoom();
        ArrayList<String> days = myClass.getWeekDays();
        //myCalendar.setTime(new Date());


        //for(int d = 0; d < 1; d++){
        for(int d = 0; d < days.size(); d++){

            Calendar myCalendar = Calendar.getInstance();
            myCalendar.setTimeInMillis(System.currentTimeMillis());
            if(parseInt(hourMin[0]) <= 7){
                myCalendar.set(Calendar.HOUR_OF_DAY, parseInt(hourMin[0])+12);
            }
            else
                myCalendar.set(Calendar.HOUR_OF_DAY, parseInt(hourMin[0]));
            myCalendar.set(Calendar.MINUTE, parseInt(hourMin[1]));
            myCalendar.set(Calendar.SECOND, 0);
            Log.d("days values:", days.get(d));
            if(days.get(d).equals("Monday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);}
            else if(days.get(d).equals("Tuesday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);}
            else if(days.get(d).equals("Wednesday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);}
            else if(days.get(d).equals("Thursday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);}
            else if(days.get(d).equals("Friday")){myCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);}

            Log.d("Calendar time:", myCalendar.getTime().toString());
            Log.d("Classroom:", classRoom);
            scheduleNotification(getNotification(className+" begins shortly. Click here to get your directions."
                    ,classRoom,d * size),myCalendar, d + (size * 2));
        }
    }

    private ClassObj readInfo() {
        String className = ((EditText) findViewById(R.id.classNameInput)).getText().toString().toUpperCase();
        if(className.equals("")){className="COMP3004";}
        ArrayList<String> days = new ArrayList<String>();

        String startTime = ((EditText) findViewById(R.id.editText2)).getText().toString();
        if(startTime.equals(""))startTime = "1:05";
        String roomString  =((EditText)findViewById(R.id.classRoomInput)).getText().toString().toUpperCase();
        if(roomString.equals("") )roomString = "HP3115";
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

        if(days.size() ==0){
            days.clear();
            days.add("Tuesday");
            days.add("Thursday");
        }
        if(days.size() == 1){
            days.get(0);
            days.add(days.get(0));
        }

        return new ClassObj(className, startTime, days, roomString);
    }

    private void saveInfoToDoc(Document xmlDoc, ClassObj c, int position){
        //Appends the information to a new Element
        Element Class = xmlDoc.getDocumentElement();


        Element newClass = xmlDoc.createElement("class");
        newClass.setAttribute("id", c.getClassName());
        newClass.setIdAttribute("id",true);
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
    private void scheduleNotification(Notification notification, Calendar classTime, int id){
        //int NotificationID = (int) System.currentTimeMillis();
        //int NotificationID = id;
        //classTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        //classTime.set(Calendar.MONTH, 12);
        //classTime.set(Calendar.YEAR, 2016);
        //classTime.set(Calendar.DAY_OF_MONTH, 8);
        //classTime.set(Calendar.HOUR_OF_DAY, 4);
        //classTime.set(Calendar.MINUTE, 35);
        //classTime.set(Calendar.SECOND, 0);
        //classTime.set(Calendar.AM_PM, Calendar.AM);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //notificationManager.notify(NotificationID, notification);

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id );
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);


        Log.d("ClassTime", classTime.toString());




        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, classTime.getTimeInMillis(), (1000*60*60*24*7),pendingIntent);
    }

    /** Builds the notification using the preferences specified by the user
     *
     * @param content   The text of the notification
     * @return
     */
    private Notification getNotification(String content, String classroom, int id){
        Intent myIntent = new Intent(this, MainActivity.class);
        Log.d("roomString: ", classroom);
        myIntent.putExtra("roomString", classroom);
        Log.d("roomStringAfterAdd", myIntent.getStringExtra("roomString"));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id , myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_stat_new_message)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Directions")
                        .setBigContentTitle("NavU")
                        .setSummaryText("Get Directions to Class"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

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


