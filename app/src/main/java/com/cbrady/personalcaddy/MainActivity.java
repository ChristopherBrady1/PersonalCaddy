package com.cbrady.personalcaddy;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity {



    private String currentRoundKey;
    private String currentClub1;
    private String currentClub2;
    private String currentLie1;
    private String currentLie2;
    private String holePar;
    private String holeDistance;

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

}