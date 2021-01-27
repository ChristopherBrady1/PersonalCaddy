package com.cbrady.personalcaddy.models;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Shots {

    public String holeid;
    public String desiredDistance;
    public String actualDistance;
    public String club;
    public String shotNum;
    public String lieBall;

    public Shots() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Shots(String holeid, String desiredDistance, String actualDistance, String club, String shotNum, String lieBall) {
        this.holeid = holeid;
        this.desiredDistance = desiredDistance;
        this.actualDistance = actualDistance;
        this.club = club;
        this.shotNum = shotNum;
        this.lieBall = lieBall;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("holeid", holeid);
        result.put("desiredDistance", desiredDistance);
        result.put("actualDistance", actualDistance);
        result.put("club", club);
        result.put("shotNum", shotNum);
        result.put("lie of ball", lieBall);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]