package com.example.alex.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * This class will hold the class name, start time and days of the week that the class occurs
 *
 * Created by mikes on 2016-11-14.
 */

public class ClassObj {

    private String name;
    private String startTime;
    private ArrayList<String> weekDays;
    private String room;


    public ClassObj(String n, String sT, ArrayList<String> wD, String r){
        name = n;
        startTime = sT;
        weekDays = wD;
        room = r;
    }

    public String getClassName(){return name;}
    public String getClassTime(){return startTime;}
    public ArrayList<String> getWeekDays(){return weekDays;}
    public String getRoom(){return room;}
}
