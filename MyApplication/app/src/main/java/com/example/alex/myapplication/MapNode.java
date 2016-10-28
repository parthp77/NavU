package com.example.alex.myapplication;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Alex on 2016-10-22.
 * Standard node, created from node_data.xml
 * Should be positioned outside doors and at corners
 */

public class MapNode {

    //position of nodes
    private float x, y;
    //list of connected nodes
    private ArrayList<String> connections;
    //room id (e.g. ML472 or HP3341)
    private String id;

    public MapNode(float posx, float posy, String iden, ArrayList<String> nodes)
    {
        x = posx;
        y = posy;
        connections = nodes;
        id = iden;

        Log.d("id: ", id);
        Log.d("posx: ", Float.toString(x));
        Log.d("posy: ", Float.toString(y));
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public String getId()
    {
        return id;
    }

}
