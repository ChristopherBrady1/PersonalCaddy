package com.cbrady.personalcaddy.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Holes {



    public String roundid;
    public String userID;
    public String distance;
    public String par;
    public String holeNum;

    public Holes() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Holes(String roundid, String userID, String distance, String par, String holeNum) {
        this.roundid = roundid;
        this.userID = userID;
        this.distance = distance;
        this.par = par;
        this.holeNum = holeNum;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roundid", roundid);
        result.put("userID", userID);
        result.put("distance", distance);
        result.put("par", par);
        result.put("holeNum", holeNum);

        return result;
    }
    // [END post_to_map]

    public String getRoundid() {
        return roundid;
    }

    public void setRoundid(String roundid) {
        this.roundid = roundid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPar() {
        return par;
    }

    public void setPar(String par) {
        this.par = par;
    }

    public String getHoleNum() {
        return holeNum;
    }

    public void setHoleNum(String holeNum) {
        this.holeNum = holeNum;
    }
}
// [END post_class]