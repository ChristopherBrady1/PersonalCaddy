package com.cbrady.personalcaddy.models;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Round {



    public String uid;
    public String user;
    public String golfCourseName;
    public String parCourse;
    public String currentDate;
    public int score;
    public int totalPutts;
    public int totalGIR;
    public int totalFIR;
    public int numPar3s;
    public Map<String, Boolean> holes = new HashMap<>();

    public Round() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Round(String uid, String user, String golfCourseName, String parCourse, String currentDate, int score, int totalPutts, int totalGIR, int totalFIR, int numPar3s) {
        this.uid = uid;
        this.user = user;
        this.golfCourseName = golfCourseName;
        this.parCourse = parCourse;
        this.currentDate = currentDate;
        this.score = score;
        this.totalPutts = totalPutts;
        this.totalGIR =totalGIR;
        this.totalFIR = totalFIR;
        this.numPar3s = numPar3s;

    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("user", user);
        result.put("golfCourseName", golfCourseName);
        result.put("parCourse", parCourse);
        result.put("currentDate", currentDate);
        result.put("holes", holes);
        result.put("score",score);
        result.put("totalPutts", totalPutts);
        result.put("totalGIR",totalGIR);
        result.put("totalFIR",totalFIR);
        result.put("numPar3s", numPar3s);

        return result;
    }
    // [END post_to_map]

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGolfCourseName() {
        return golfCourseName;
    }

    public void setGolfCourseName(String golfCourseName) {
        this.golfCourseName = golfCourseName;
    }

    public String getParCourse() {
        return parCourse;
    }

    public void setParCourse(String parCourse) {
        this.parCourse = parCourse;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public Map<String, Boolean> getHoles() {
        return holes;
    }

    public void setHoles(Map<String, Boolean> holes) {
        this.holes = holes;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalPutts() {
        return totalPutts;
    }

    public void setTotalPutts(int totalPutts) {
        this.totalPutts = totalPutts;
    }



    public int getTotalGIR() {
        return totalGIR;
    }

    public void setTotalGIR(int totalGIR) {
        this.totalGIR = totalGIR;
    }


    public int getTotalFIR() {
        return totalFIR;
    }

    public void setTotalFIR(int totalFIR) {
        this.totalFIR = totalFIR;
    }



    public int getNumPar3s() {
        return numPar3s;
    }

    public void setNumPar3s(int numPar3s) {
        this.numPar3s = numPar3s;
    }
}
// [END post_class]