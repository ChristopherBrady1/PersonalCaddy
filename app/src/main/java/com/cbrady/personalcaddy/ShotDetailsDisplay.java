package com.cbrady.personalcaddy;

public class ShotDetailsDisplay {
    public String desiredDistance;
    public String actualDistance;
    public String club;
    public String shotNum;
    public String lieBall;

    public ShotDetailsDisplay(String desiredDistance, String actualDistance, String club, String shotNum, String lieBall)
    {
        this.desiredDistance = desiredDistance;
        this.actualDistance = actualDistance;
        this.club = club;
        this.shotNum = shotNum;
        this.lieBall = lieBall;
    }

}
