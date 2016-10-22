package com.example.alex.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * Created by Alex on 2016-10-22.
 */

public class Building {

    //list of nodes in the building
    private ArrayList<MapNode> mapNodes = new ArrayList<MapNode>();

    //constructor, a room ID (for determing what building to load)
    public Building(Context context, String init)
    {
        parseXML(context);
    }

    //finds optimal path
    //takes user's position and destination node (room)
    public void plotCourse(MapNode start, MapNode end)
    {

    }

    //finds node closest to a position (e.g. user input)
    public MapNode findNode(float x, float y)
    {
        MapNode curr = mapNodes.get(0);

        if (mapNodes.isEmpty())
            return null;

        for (int i=1; i < mapNodes.size(); i++)
        {
            curr = compareDist(curr, mapNodes.get(i), x, y);
        }

        return curr;
    }

    //compares distance between 2 nodes and a point, returning the closest
    public MapNode compareDist(MapNode n1, MapNode n2, float x, float y)
    {
        if (dist(n1.getX(), n1.getY(), x, y) < dist(n2.getX(), n2.getY(), x, y))
            return n1;
        else
            return n2;
    }

    //returns distance between 2 points
    public float dist(float x1, float y1, float x2, float y2)
    {
        return (float)Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }

    //parses the XML for building data
    public void parseXML(Context context)
    {
        try {
            //init parser
            InputStream inputFile = context.getResources().openRawResource(R.raw.node_data);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("node");
            for (int i=0; i < nList.getLength(); i++)
            {
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;
                ArrayList<String> connections = new ArrayList<String>();
                //find number of connetions
                int j = Integer.parseInt(eElement.getElementsByTagName("num").item(0).getTextContent());
                //add connetions to list
                for (int k=0; k < j; k++){
                    connections.add(eElement.getElementsByTagName("connection").item(k).getTextContent());
                }
                //create new mapNode
                mapNodes.add(new MapNode(
                    Float.parseFloat(eElement.getElementsByTagName("positionx").item(0).getTextContent()),
                    Float.parseFloat(eElement.getElementsByTagName("positiony").item(0).getTextContent()),
                    eElement.getElementsByTagName("id").item(0).getTextContent(),
                    connections
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
