package com.example.Interview.geek.Contest.Reminder;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpcomingContest {
    private String name, link, start, end, website;
    private boolean status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    private void setStatus() throws ParseException {
        String t = getStart().replace('T', ' ');

        Date st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(t);

        Date current = new Date();

        if(st.compareTo(current)> 0) status =  true;
        else status = false;

    }

    public boolean getStatus() throws ParseException {
        setStatus();
        return status;
    }
}
