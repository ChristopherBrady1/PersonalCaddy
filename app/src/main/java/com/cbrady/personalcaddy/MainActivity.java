package com.cbrady.personalcaddy;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {



    private String currentRoundKey;
    private String currentHoleNum;
    private int holeNum = 1;
    private String currentClub1;
    private String currentClub2;
    private String currentLie1 = "";
    private String currentLie2;
    private String holePar;
    private String holeDistance;
    private String holeKey;
    private int holeDetailsComplete;

    private float desired_distance;

    public ArrayList<String> scorecard = new ArrayList<String>();

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);


        //setting defaults for scorecard array list
        for (int i=0; i < 18; i++){
            scorecard.add(String.valueOf(i));
        }

        Log.d("ARRAYLIST", String.valueOf(scorecard.size()) );


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }



    public String getHolePar() {
        return holePar;
    }

    public void setHolePar(String holePar) {
        this.holePar = holePar;
    }

    public String getHoleDistance() {
        return holeDistance;
    }

    public void setHoleDistance(String holeDistance) {
        this.holeDistance = holeDistance;
    }

    public String getCurrentRoundKey() {
        return currentRoundKey;
    }

    public void setCurrentRoundKey(String currentRoundKey) {
        this.currentRoundKey = currentRoundKey;
    }

    public String getCurrentClub1() {
        return currentClub1;
    }

    public void setCurrentClub1(String currentClub1) {
        this.currentClub1 = currentClub1;
    }

    public String getCurrentClub2() {
        return currentClub2;
    }

    public void setCurrentClub2(String currentClub2) {
        this.currentClub2 = currentClub2;
    }

    public String getCurrentLie1() {
        return currentLie1;
    }

    public void setCurrentLie1(String currentLie1) {
        this.currentLie1 = currentLie1;
    }

    public String getCurrentLie2() {
        return currentLie2;
    }

    public void setCurrentLie2(String currentLie2) {
        this.currentLie2 = currentLie2;
    }

    public int getHoleDetailsComplete() { return holeDetailsComplete; }

    public void setHoleDetailsComplete(int holeDetailsComplete) { this.holeDetailsComplete = holeDetailsComplete; }

    public String getCurrentHoleNum() {
        return currentHoleNum;
    }

    public void setCurrentHoleNum(String currentHoleNum) {
        this.currentHoleNum = currentHoleNum;
    }

    public String getHoleKey() {
        return holeKey;
    }

    public void setHoleKey(String holeKey) {
        this.holeKey = holeKey;
    }

    public int getHoleNum() {
        return holeNum;
    }

    public void setHoleNum(int holeNum) {
        this.holeNum = holeNum;
    }

    public float getDesired_distance() {
        return desired_distance;
    }

    public void setDesired_distance(float desired_distance) {
        this.desired_distance = desired_distance;
    }
}