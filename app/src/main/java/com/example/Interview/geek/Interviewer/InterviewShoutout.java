package com.example.Interview.geek.Interviewer;

import java.io.Serializable;

public class InterviewShoutout implements Serializable {
    private String circularID, candidateID, topic, date, time, email;

    public String getCircularID() {
        return circularID;
    }

    public void setCircularID(String circularID) {
        this.circularID = circularID;
    }

    public String getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(String candidateID) {
        this.candidateID = candidateID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
