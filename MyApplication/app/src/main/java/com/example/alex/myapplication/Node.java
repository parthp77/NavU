package com.example.alex.myapplication;

import java.util.ArrayList;

/**
 * Created by Alex on 2016-10-22.
 */

public class Node {

    private short x, y;
    private ArrayList<Node> connections;
    private String id;

    public Node(short posx, short posy, String iden, ArrayList<Node> nodes)
    {
        x = posx;
        y = posy;
        connections = nodes;
        id = iden;
    }



}
