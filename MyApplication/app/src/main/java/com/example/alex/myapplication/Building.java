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

    private int numFloors;

    public int getNumFloors() { return numFloors; }

    //constructor, a room ID (for determing what building to load)
    public Building(Context context, String init)
    {
        parseXML(context, init);
    }

    //finds optimal path
    //takes user's position and destination node (room)
    public ArrayList<MapNode> plotCourse(MapNode start, MapNode end)
    {
        ArrayList<MapNode> visitedNodes = new ArrayList<MapNode>();
        ArrayList<MapNode> toVisit = new ArrayList<MapNode>();
        toVisit.add(start);

        while (!toVisit.isEmpty())
        {
            MapNode node = toVisit.remove(0);
            //check if path has been found
            if (node == end) return buildPath(end);
            else
            {
                //add current node to the list of visited nodes
                visitedNodes.add(node);

                for (int i=0; i < node.connections.size(); i++)
                {
                    //add each unvisited node to the list of visited and add each to the list of branches to search
                    MapNode neighbour = getNodeById(node.connections.get(i));
                    if (!visitedNodes.contains(neighbour) && !toVisit.contains(neighbour))
                    {
                        neighbour.setParent(node);
                        toVisit.add(neighbour);
                    }
                }
            }
        }

        return null;
    }

    public MapNode getNodeById(String id)
    {
        for (int i=0; i < mapNodes.size(); i++)
        {
            if (mapNodes.get(i).getId().equals(id)) return mapNodes.get(i);
        }
        return null;
    }

    private ArrayList<MapNode> buildPath(MapNode n)
    {
        ArrayList<MapNode> path = new ArrayList<MapNode>();
        //build list of nodes along the path
        while (n != null) {
            path.add(n);
            n = n.getParent();
        }
        //reset all the parents for next search
        for (int i=0; i < path.size(); i++)
        {
            path.get(i).setParent(null);
        }
        return path;
    }

    //finds node closest to a position (e.g. user input)
    public MapNode findNode(float x, float y)
    {

        if (mapNodes.isEmpty())
            return null;

        MapNode curr = mapNodes.get(0);

        for (int i=1; i < mapNodes.size(); i++)
        {
            curr = compareDist(curr, mapNodes.get(i), x, y);
        }

        return curr;
    }

    //compares distance between 2 nodes and a point, returning the closest
    private MapNode compareDist(MapNode n1, MapNode n2, float x, float y)
    {
        if (dist(n1.getX(), n1.getY(), x, y) < dist(n2.getX(), n2.getY(), x, y))
            return n1;
        else
            return n2;
    }

    //returns distance between 2 points
    private float dist(float x1, float y1, float x2, float y2)
    {
        return (float)Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }

    //parses the XML for building data
    private void parseXML(Context context, String building)
    {
        try {
            //init parser
            InputStream inputFile = context.getResources().openRawResource(R.raw.node_data);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            //find the element corresponding to the building we're interested in
            Element ele = doc.getElementById(building);
            NodeList fc = ele.getElementsByTagName("floorcount");
            numFloors = Integer.parseInt(fc.item(0).getTextContent());
            //find all elements in that building
            NodeList nList = ele.getElementsByTagName("node");
            for (int i=0; i < nList.getLength(); i++)
            {
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;
                ArrayList<String> connections = new ArrayList<String>();
                //add connetions to list
                for (int k=0; k < eElement.getElementsByTagName("connection").getLength(); k++){
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
