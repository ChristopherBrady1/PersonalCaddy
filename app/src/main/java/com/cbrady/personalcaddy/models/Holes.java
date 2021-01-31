package com.cbrady.personalcaddy.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Holes {

    public String roundid;
    public String distance;
    public String par;
    public String holeNum;

    public Holes() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Holes(String roundid, String distance, String par, String holeNum) {
        this.roundid = roundid;
        this.distance = distance;
        this.par = par;
        this.holeNum = holeNum;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roundid", roundid);
        result.put("distance", distance);
        result.put("par", par);
        result.put("holeNum", holeNum);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]