package com.example.alex.myapplication;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Alex on 2016-10-22.
 */

public class MapNode {

    private short x, y;
    private ArrayList<String> connections;
    private String id;

    public MapNode(short posx, short posy, String iden, ArrayList<String> nodes)
    {
        x = posx;
        y = posy;
        connections = nodes;
        id = iden;
    }



}
