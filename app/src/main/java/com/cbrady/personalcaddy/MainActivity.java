package com.cbrady.personalcaddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends BaseActivity {

    private String currentRoundKey;
    private String currentHoleNum;
    private int holeNum = 1;
    private String currentClub1;
    private String currentClub2;
    private String currentLie = "Fairway";
    private String currentWind = "No Wind";
    private String currentHill = "Flat";
    private String holePar;
    private String holeDistance;
    private String holeKey;
    private int holeDetailsComplete;
    private int currentShot =1;

    public int getShotTotal() {
        return shotTotal;
    }

    public void setShotTotal(int shotTotal) {
        this.shotTotal = shotTotal;
    }

    private int shotTotal=0;

    public int getShotCounter() {
        return shotCounter;
    }

    public void setShotCounter(int shotCounter) {
        this.shotCounter = shotCounter;
    }

    private int shotCounter=1;
    private int fir =0;
    private String hiddenKey;

    public String getHiddenHoleKey() {
        return hiddenHoleKey;
    }

    public void setHiddenHoleKey(String hiddenHoleKey) {
        this.hiddenHoleKey = hiddenHoleKey;
    }

    private String hiddenHoleKey;

    private int numPar3s =0;

    private float desired_distance;

    public ArrayList<String> scorecardScores = new ArrayList<String>();
    public ArrayList<String> scorecardPars = new ArrayList<String>();

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    private int counter =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);


        //setting defaults for scorecard array list
        for (int i=0; i < 18; i++){
            scorecardScores.add(String.valueOf(0));
            scorecardPars.add(String.valueOf(0));
        }



        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();
        Set<String> sets = new HashSet<>();
        sets.add("def Value");

        builder.append("\n" + "Perform Sync:\t" + sharedPrefs.getBoolean("perform_sync", false));
        builder.append("\n" + "Measurement Unit:\t" + sharedPrefs.getString("dist_Measure", "1"));
        builder.append("\n" + "Clubs:\t" + sharedPrefs.getStringSet("club_List",sets ));
        builder.append("\n" + "Name:\t" + sharedPrefs.getString("full_name", "Not known to us"));
        builder.append("\n" + "Email Address:\t" + sharedPrefs.getString("email_address", "No EMail Address Provided"));
        builder.append("\n" + "Customized Notification Ringtone:\t" + sharedPrefs.getString("notification_ringtone", ""));
        builder.append("\n\nClick on Settings Button at bottom right corner to Modify Your Prefrences");

        Log.d("pref", builder.toString());

        ArrayList<String> arrayListClubs = new ArrayList<String>();
        Set<String> setNew = sharedPrefs.getStringSet("club_List",sets);
        for (String str : setNew)
            arrayListClubs.add(str);

        int x=0;
        for(String str : arrayListClubs) {
            Log.d("ClubsArrayList", "club" + String.valueOf(x) + ": " + str);
            x++;
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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

    public String getCurrentLie() {
        return currentLie;
    }

    public void setCurrentLie(String currentLie) {
        this.currentLie = currentLie;
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

    public String getCurrentWind() {
        return currentWind;
    }

    public void setCurrentWind(String currentWind) {
        this.currentWind = currentWind;
    }

    public String getCurrentHill() {
        return currentHill;
    }

    public void setCurrentHill(String currentHill) {
        this.currentHill = currentHill;
    }

    public int getCurrentShot() {
        return currentShot;
    }

    public void setCurrentShot(int currentShot) {
        this.currentShot = currentShot;
    }



    public int getFIR() {
        return fir;
    }

    public void setFIR(int fir) {
        this.fir = fir;
    }



    public int getNumPar3s() {
        return numPar3s;
    }

    public void setNumPar3s(int numPar3s) {
        this.numPar3s = numPar3s;
    }



    public String getHiddenKey() {
        return hiddenKey;
    }

    public void setHiddenKey(String hiddenKey) {
        this.hiddenKey = hiddenKey;
    }


}