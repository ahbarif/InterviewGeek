package com.example.Interview.geek.Contest.Reminder;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ContestXMLStringParser {

    private Document parseXmlFromString(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
        Document document = builder.parse(inputStream);
        return document;
    }

    public ArrayList<UpcomingContest> getContestList(String xml) throws IOException, SAXException, ParserConfigurationException {
        ArrayList<UpcomingContest> list = new ArrayList<>();

        Document doc = (Document) parseXmlFromString(xml);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("object");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String name = eElement
                        .getElementsByTagName("event")
                        .item(0)
                        .getTextContent();

                String link = eElement
                        .getElementsByTagName("href")
                        .item(0)
                        .getTextContent();

                String start = eElement
                        .getElementsByTagName("start")
                        .item(0)
                        .getTextContent();

                String end = eElement
                        .getElementsByTagName("end")
                        .item(0)
                        .getTextContent();

                String website = eElement
                        .getElementsByTagName("name")
                        .item(0)
                        .getTextContent();

                UpcomingContest contest = new UpcomingContest();

                contest.setName(name);
                contest.setStart(start);
                contest.setEnd(end);
                contest.setLink(link);
                contest.setWebsite(website);

                list.add(contest);
            }
        }

        return list;


    }
}
