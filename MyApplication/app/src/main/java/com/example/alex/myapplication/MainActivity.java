package com.example.alex.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.HOUR, 10);
        myCalendar.set(Calendar.MINUTE, 5);


        Building b = new Building(this, "HP");
        final Context context = getApplicationContext();


        Button button = (Button) findViewById(R.id.button_settings);

                button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        SettingsActivity.class);
                startActivity(myIntent);
            }
        });

        button = (Button) findViewById(R.id.button_about);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        AboutActivity.class);
                startActivity(myIntent);
            }
        });


        button = (Button) findViewById(R.id.button_search);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        MapActivity.class);
                startActivity(myIntent);
            }
        });


        button = (Button) findViewById(R.id.button_schedule);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,
                        ClassesActivity.class);
                scheduleNotification(getNotification("Class begins shortly"),myCalendar );
                startActivity(myIntent);
            }
        });
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
        Calendar alarm = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        alarm.setTime(date);
        alarm.set(Calendar.HOUR_OF_DAY, 5);
        alarm.set(Calendar.MINUTE, 20);
        alarm.set(Calendar.SECOND, 0);
        if (alarm.before(now)){
            alarm.add(Calendar.DATE, 1);
        }

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, alarm.getTimeInMillis(), pendingIntent);
    }

    /** Builds the notification using the preferences specified by the user
     *
     * @param content   The text of the notification
     * @return
     */
    private Notification getNotification(String content){
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
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0));

        //Setting the type of notification - User may only want vibrate notification and not sound
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
