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
    public Map<String, Boolean> holes = new HashMap<>();

    public Round() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Round(String uid, String user, String golfCourseName, String parCourse, String currentDate) {
        this.uid = uid;
        this.user = user;
        this.golfCourseName = golfCourseName;
        this.parCourse = parCourse;
        this.currentDate = currentDate;
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
}
// [END post_class]