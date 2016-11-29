package com.example.alex.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private Building building;
    ArrayList<MapNode> route;
    private Drawable[] map;
    private int currentFloor;
    private String build;
    private int stair;
    private boolean startFloor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startFloor = true;
        //getting room string from main activity's editText
        Intent intent = getIntent();
        String roomString = intent.getExtras().getString("roomString");
        String startRoom = intent.getExtras().getString("startRoom");
        build = roomString.substring(0,2);
        building = new Building(this, build);

        map = new Drawable[building.getNumFloors()];

        if (startRoom == null)
            route = building.plotCourse(building.getNodeById("HP3341"), building.getNodeById(roomString));
        else
            route = building.plotCourse(building.getNodeById(startRoom), building.getNodeById(roomString));

        if (route.size() > 0) currentFloor = route.get(route.size()-1).getFloor();
        for (int i=0; i < route.size(); i++) if (route.get(i).getId().charAt(0) == 'S') stair = i;

        MyView v = new MyView(this);
        v.setOnTouchListener(new MyView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (currentFloor == 3)
                    {
                        currentFloor = 2;
                        startFloor = !startFloor;
                    }
                    else
                    {
                        currentFloor = 3;
                        startFloor = !startFloor;
                    }
                    v.invalidate();
                }
                return true;
            }
        });
        setContentView(v);
    }

    public class MyView extends View
    {

        public MyView(Context context)
        {
            super(context);
            //map = context.getResources().getDrawable(R.drawable.hp3);
            map[1] = getDrawable(build.toLowerCase() + String.valueOf(2),context);
            map[2] = getDrawable(build.toLowerCase() + String.valueOf(3),context);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            map[currentFloor-1].setBounds(0,0,getWidth(),getHeight());
            map[currentFloor-1].draw(canvas);

            if (route == null) return;
            Paint p = new Paint();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(10);
            p.setColor(Color.RED);
            Log.d("Current Floor ", String.valueOf(currentFloor));
            Log.d("Startfloor? ", String.valueOf(startFloor));
            if (!startFloor) {
                for (int i = 0; i < stair; i++) {
                    canvas.drawLine(route.get(i).getX() * getWidth(), route.get(i).getY() * getHeight(), route.get(i + 1).getX() * getWidth(), route.get(i + 1).getY() * getHeight(), p);
                }
            }
            else
            {
                for (int i = stair; i < route.size() - 1; i++) {
                    canvas.drawLine(route.get(i).getX() * getWidth(), route.get(i).getY() * getHeight(), route.get(i + 1).getX() * getWidth(), route.get(i + 1).getY() * getHeight(), p);
                }
            }
        }
    }

    public static Drawable getDrawable(String name, Context context) {
        int resourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return context.getResources().getDrawable(resourceId);
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
