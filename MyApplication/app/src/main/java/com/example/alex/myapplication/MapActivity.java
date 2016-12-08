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

    private ArrayList<Building> buildings = new ArrayList<Building>();
    private ArrayList<ArrayList<MapNode>> routes = new ArrayList<ArrayList<MapNode>>();
    private ArrayList<String> maps = new ArrayList<String>();
    private int currentFloor;
    //private boolean startFloor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentFloor = 0;

        //getting room string from main activity's editText
        Intent intent = getIntent();
        String roomString = intent.getExtras().getString("roomString");
        String startRoom = intent.getExtras().getString("startRoom");

        //some error checking
        if (roomString == null || startRoom == null)
        {
            returnToMain();
            return;
        }

        if (roomString.length() < 5 || startRoom.length() < 5)
        {
            returnToMain();
            return;
        }

        String build = roomString.substring(0,2);

        //same floor
        if (roomString.substring(0,3).equals(startRoom.substring(0,3)))
        {
            buildings.add(new Building(this, build));
            routes.add(buildings.get(0).plotCourse(buildings.get(0).getNodeById(startRoom), buildings.get(0).getNodeById(roomString)));
            maps.add(startRoom.substring(0,3).toLowerCase());
        }
        //same building, different floors
        else if (roomString.substring(0,2).equals(startRoom.substring(0,2)))
        {
            buildings.add(new Building(this, build));
            ArrayList<MapNode> temp = buildings.get(0).plotCourse(buildings.get(0).getNodeById(startRoom), buildings.get(0).getNodeById(roomString));

            if (temp == null)
            {
                returnToMain();
                return;
            }

            routes = seperateRoute(temp);
            maps.add(startRoom.substring(0,3).toLowerCase());
            maps.add(roomString.substring(0,3).toLowerCase());
        }
        //different buildings
        else {
            buildings.add(new Building(this, startRoom.substring(0, 2)));
            buildings.add(new Building(this, "TN"));
            buildings.add(new Building(this, roomString.substring(0, 2)));

            ArrayList<MapNode> temp = buildings.get(0).plotCourse(buildings.get(0).getNodeById(startRoom),
                    buildings.get(0).getNodeById(startRoom.substring(0, 2) + "tunnels"));

            //if starting on first floor of first building
            if (!startRoom.substring(2, 3).equals("1")) {
                routes = seperateRoute(temp);
                maps.add(startRoom.substring(0, 3).toLowerCase());
            } else {
                routes.add(temp);
            }
            maps.add(startRoom.substring(0, 2).toLowerCase() + "1");

            //add tunnels
            routes.add(buildings.get(1).plotCourse(buildings.get(1).getNodeById("TN" + startRoom.substring(0, 2)),
                    buildings.get(1).getNodeById("TN" + roomString.substring(0, 2))));
            maps.add("tunnels");

            //add second building
            routes.add(buildings.get(2).plotCourse(buildings.get(2).getNodeById(roomString.substring(0, 2) + "tunnels"),
                    buildings.get(2).getNodeById(roomString)));
            maps.add(roomString.substring(0, 3).toLowerCase());
        }

            //check for tomfoolery
            for (int i=0; i < routes.size(); i++)
            {
                if (routes.get(0) == null)
                {
                    returnToMain();
                    return;
                }
            }

            //check for user input, switch floors if screen is touched
            MyView v = new MyView(this);
            v.setOnTouchListener(new MyView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        currentFloor = (++currentFloor) % maps.size();
                        v.invalidate();
                    }
                    return true;
                }
            });
            setContentView(v);
        }

    private void returnToMain()
    {
        Intent myIntent = new Intent(MapActivity.this,
                MainActivity.class);
        startActivity(myIntent);
    }

    public class MyView extends View
    {

        Paint paint, p;
        Context context;

        public MyView(Context context)
        {
            super(context);
            //map = context.getResources().getDrawable(R.drawable.hp3);

            this.context = context;
            paint = new Paint();
            p = new Paint();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            Drawable map = getDrawable(maps.get(currentFloor), context);
            map.setBounds(0,0,getWidth(),getHeight());
            map.draw(canvas);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(8);
            p.setColor(Color.RED);

            for (int i=0; i < routes.get(currentFloor).size()-1; i++)
                canvas.drawLine(routes.get(currentFloor).get(i).getX() * getWidth(),
                        routes.get(currentFloor).get(i).getY() * getHeight(),
                        routes.get(currentFloor).get(i + 1).getX() * getWidth(),
                        routes.get(currentFloor).get(i + 1).getY() * getHeight(), p);
        }
    }

    public Drawable getDrawable(String name, Context context) {
        try {
            int resourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            return context.getResources().getDrawable(resourceId);
        } catch (Exception e) {
            returnToMain();
            return null;
        }
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //seperates a route into 2 routes, one per floor
    private ArrayList<ArrayList<MapNode>> seperateRoute(ArrayList<MapNode> nodes)
    {
        ArrayList<ArrayList<MapNode>> ret = new ArrayList<ArrayList<MapNode>>();
        ArrayList<MapNode> list1 = new ArrayList<MapNode>();
        ArrayList<MapNode> list2 = new ArrayList<MapNode>();
        boolean check = false;

        for (int i=0; i < nodes.size(); i++)
        {
            if (nodes.get(i).getId().charAt(0) == 'S' && nodes.get(i).getId().length() == 3 && !check)
            {
                list1.add(nodes.get(i));
                check = true;
                continue;
            }
            if (check)
                list2.add(nodes.get(i));
            else
                list1.add(nodes.get(i));
        }

        ret.add(list1);
        ret.add(list2);
        return ret;
    }
}
