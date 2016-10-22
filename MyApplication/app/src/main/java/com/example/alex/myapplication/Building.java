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

    private ArrayList<MapNode> MapNodes = new ArrayList<MapNode>();

    public Building(Context context, String init)
    {
        parseXML(context);
    }

    public void parseXML(Context context)
    {
        try {
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
                int j = Integer.parseInt(eElement.getElementsByTagName("num").item(0).getTextContent());
                for (int k=0; k < j; k++){
                    connections.add(eElement.getElementsByTagName("connection").item(k).getTextContent());
                }
                MapNodes.add(new MapNode(
                    Short.parseShort(eElement.getElementsByTagName("positionx").item(0).getTextContent()),
                    Short.parseShort(eElement.getElementsByTagName("positiony").item(0).getTextContent()),
                    eElement.getElementsByTagName("id").item(0).getTextContent(),
                    connections
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
