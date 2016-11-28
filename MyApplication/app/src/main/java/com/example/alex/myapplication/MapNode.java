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
    public ArrayList<String> connections;
    //room id (e.g. ML472 or HP3341)
    private String id;
    //for searching only
    private MapNode parent;

    public MapNode(float posx, float posy, String iden, ArrayList<String> s)
    {
        x = posx;
        y = posy;
        id = iden;
        connections = s;
        parent = null;
    }

    public int getFloor() { return Integer.parseInt(id.substring(2,3)); }

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

    public MapNode getParent() { return parent; }

    public void setParent(MapNode p) { parent = p; }

}
