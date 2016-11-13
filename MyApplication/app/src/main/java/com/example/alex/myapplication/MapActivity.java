package com.example.alex.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
        String test = "ML474";

        loadData(test);
        route = building.plotCourse(building.findNode(10,10), building.getNodeById(test));

        for (int i=0; i < route.size(); i++)
            Log.d("node", route.get(i).getId());

    }

    private void loadData(String room)
    {
        String build = room.substring(0,2);
        building = new Building(this, build);
    }

    private void drawRoute(Canvas c)
    {
        if (route == null) return;

        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setColor(Color.RED);
        for (int i=0; i < route.size()-1; i++)
        {
            c.drawLine(route.get(i).getX()*20, route.get(i).getY()*20, route.get(i+1).getX()*20, route.get(i+1).getY()*20, p);
        }
    }

    public class MyView extends View
    {

        public MyView(Context context)
        {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            paint.setColor(Color.BLUE);
            //canvas.drawCircle(getWidth()/2, getHeight()/2, 100, paint);
            drawRoute(canvas);
        }
    }

}
