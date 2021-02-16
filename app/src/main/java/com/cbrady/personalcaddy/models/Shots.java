package com.cbrady.personalcaddy.models;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Shots {



    public String holeid;
    public String userId;
    public String roundId;
    public String desiredDistance;
    public String actualDistance;
    public String club;
    public String shotNum;
    public String lieBall;

    public Shots() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Shots(String holeid, String userId, String roundId, String desiredDistance, String actualDistance, String club, String shotNum, String lieBall) {
        this.holeid = holeid;
        this.userId = userId;
        this.roundId = roundId;
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
        result.put("userId", userId);
        result.put("roundId",roundId);
        result.put("desiredDistance", desiredDistance);
        result.put("actualDistance", actualDistance);
        result.put("club", club);
        result.put("shotNum", shotNum);
        result.put("lie of ball", lieBall);

        return result;
    }
    // [END post_to_map]
    public String getHoleid() {
        return holeid;
    }

    public void setHoleid(String holeid) {
        this.holeid = holeid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public String getDesiredDistance() {
        return desiredDistance;
    }

    public void setDesiredDistance(String desiredDistance) {
        this.desiredDistance = desiredDistance;
    }

    public String getActualDistance() {
        return actualDistance;
    }

    public void setActualDistance(String actualDistance) {
        this.actualDistance = actualDistance;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getShotNum() {
        return shotNum;
    }

    public void setShotNum(String shotNum) {
        this.shotNum = shotNum;
    }

    public String getLieBall() {
        return lieBall;
    }

    public void setLieBall(String lieBall) {
        this.lieBall = lieBall;
    }

}
// [END post_class]