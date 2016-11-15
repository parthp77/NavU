package com.example.alex.myapplication;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by mikes on 2016-11-14.
 */

public class EditClassActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editclass);


    }

    /**
     * This will read the xml and display the previously inputted class details
     * @param context
     */
    private void parseXML(Context context)
        throws XmlPullParserException, IOException{
            int eventType =-1;
        XmlResourceParser parser = context.getResources().getXml(R.xml.classes);
        parser.next();
        eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {


            eventType = parser.next();

        }

    }
}
