package com.cbrady.personalcaddy.models;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Shots {

    public String holeid;
    public String distance;
    public String club;
    public String shotNum;

    public Shots() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Shots(String holeid, String distance, String club, String shotNum) {
        this.holeid = holeid;
        this.distance = distance;
        this.club = club;
        this.shotNum = shotNum;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("holeid", holeid);
        result.put("distance", distance);
        result.put("club", club);
        result.put("shotNum", shotNum);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]