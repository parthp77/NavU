package com.example.alex.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private Building building;
    ArrayList<MapNode> route;
    private Drawable map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

        //getting room string from main activity's editText
        Intent intent = getIntent();
        String roomString = intent.getExtras().getString("roomString");
        //Log.d("String Passed: ", roomString);


        String test = "HP3160";
        loadData(test);
        route = building.plotCourse(building.findNode(0,0), building.getNodeById(test));

        Log.d("test: ", roomString);
        Log.d("start", building.findNode(0,0).getId());
        Log.d("end", building.getNodeById(test).getId());
    }

    private void loadData(String room)
    {
        String build = room.substring(0,2);
        building = new Building(this, build);
    }

    public class MyView extends View
    {

        public MyView(Context context)
        {
            super(context);
            map = context.getResources().getDrawable(R.drawable.hp3);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            map.setBounds(0,0,getWidth(),getHeight());
            map.draw(canvas);

            if (route == null) return;
            Paint p = new Paint();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(5);
            p.setColor(Color.RED);
            for (int i=0; i < route.size()-1; i++)
            {
                canvas.drawLine(route.get(i).getX()*getWidth(), route.get(i).getY()*getHeight(), route.get(i+1).getX()*getWidth(), route.get(i+1).getY()*getHeight(), p);
            }
        }
    }

}
