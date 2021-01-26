package com.cbrady.personalcaddy.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Holes {

    public String uid;
    public String user;
    public String golfCourseName;
    public String parCourse;
    public String currentDate;
    public Map<String, Boolean> holes = new HashMap<>();

    public Holes() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Holes(String uid, String user, String golfCourseName, String parCourse, String currentDate) {
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

}
// [END post_class]