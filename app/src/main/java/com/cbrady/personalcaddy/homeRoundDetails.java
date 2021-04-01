package com.cbrady.personalcaddy;

public class homeRoundDetails implements Comparable<homeRoundDetails>{
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHiddenID() {
        return hiddenID;
    }

    public void setHiddenID(String hiddenID) {
        this.hiddenID = hiddenID;
    }

    String score;
    String courseName;
    String date;
    String hiddenID;

    public homeRoundDetails(String score, String courseName, String date, String hiddenID)
    {
        this.score = score;
        this.courseName = courseName;
        this.date = date;
        this.hiddenID = hiddenID;
    }

    @Override
    public int compareTo(homeRoundDetails o) {
        return this.getScore().compareTo(o.getScore());
    }
}
