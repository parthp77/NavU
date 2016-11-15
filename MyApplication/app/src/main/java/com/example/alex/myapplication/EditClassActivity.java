package com.example.alex.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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
        Intent intent = getIntent();
        int position = intent.getIntExtra("pos", 0);
        Log.d("MyActivity", ""+position);
        try{
            parseXML(this, position);
        }
        catch(XmlPullParserException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        Button button = (Button) findViewById(R.id.RemoveClass);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(EditClassActivity.this,
                        ClassesActivity.class);
                //myIntent.putExtra("name", ((EditText)findViewById(R.id.classNameOutput)).getText().toString());
                startActivity(myIntent);
            }
        });

    }

    /**
     * This will read the xml and display the previously inputted class details
     * @param context
     */
    private void parseXML(Context context, int pos)
        throws XmlPullParserException, IOException{
            int eventType =-1;
        XmlResourceParser parser = context.getResources().getXml(R.xml.classes);
        CheckBox mon = (CheckBox)findViewById(R.id.classMondayOut);
        CheckBox tues = (CheckBox)findViewById(R.id.classTuesdayOut);
        CheckBox wed = (CheckBox)findViewById(R.id.classWednesdayOut);
        CheckBox thu = (CheckBox)findViewById(R.id.classThursdayOut);
        CheckBox fri = (CheckBox)findViewById(R.id.classFridayOut);
        parser.next();
        eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            for(int i = pos*4; i < pos*4 ; i++){

                    //Log.d("MyActivity", parser.getText());
                    EditText nameOut = (EditText)findViewById(R.id.classNameOutput);
                    nameOut.setText(parser.getText(), TextView.BufferType.EDITABLE);
                    parser.next();

                    if(parser.getText().equals( "Monday")){ mon.setChecked(true);parser.next();}
                    if(parser.getText().equals( "Tuesday")){ tues.setChecked(true);parser.next();}
                    if(parser.getText().equals( "Wednesday")){ wed.setChecked(true);parser.next();}
                    if(parser.getText().equals(  "Thursday")){ thu.setChecked(true);parser.next();}
                    if(parser.getText().equals( "Friday")){ fri.setChecked(true);parser.next();}

                    EditText timeOut = (EditText)findViewById(R.id.StartTimeOutput);
                    timeOut.setText(parser.getText(), TextView.BufferType.EDITABLE);
                    //parser.next();



            }


            eventType = parser.next();
            break;


        }
        parser.close();

    }
}
