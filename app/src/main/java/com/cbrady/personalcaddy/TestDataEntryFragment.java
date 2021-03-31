package com.cbrady.personalcaddy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cbrady.personalcaddy.models.Holes;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.models.Shots;
import com.cbrady.personalcaddy.ui.AR.ArFragment;
import com.cbrady.personalcaddy.ui.map.MapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class TestDataEntryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    String roundKey, holeKey,shotKey;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    int shotNumber;

    public TestDataEntryFragment() {
        // Required empty public constructor
    }


    public static TestDataEntryFragment newInstance(String param1, String param2) {
        TestDataEntryFragment fragment = new TestDataEntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_test_data_entry, container, false);

        Button subRound = root.findViewById(R.id.subRoundButton);
        Button subHole = root.findViewById(R.id.subHoleButton);
        Button subShot = root.findViewById(R.id.subShotButton);

        Button driverB = root.findViewById(R.id.driverB);
        Button Wood3 = root.findViewById(R.id.Wood3);
        Button Wood5 = root.findViewById(R.id.Wood5);
        Button Wood7 = root.findViewById(R.id.Wood7);
        Button iron3 = root.findViewById(R.id.iron3);
        Button iron4 = root.findViewById(R.id.iron4);
        Button iron5 = root.findViewById(R.id.iron5);
        Button iron6 = root.findViewById(R.id.iron6);
        Button iron7 = root.findViewById(R.id.iron7);
        Button iron8 = root.findViewById(R.id.iron8);
        Button iron9 = root.findViewById(R.id.iron9);
        Button PWedge = root.findViewById(R.id.PWedge);
        Button SWedge = root.findViewById(R.id.SWedge);
        Button GWedge = root.findViewById(R.id.GWedge);
        Button LWedge = root.findViewById(R.id.LWedge);
        Button Putter = root.findViewById(R.id.Putter);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        shotNumber=1;

        subRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //date parameter
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm:ss", Locale.getDefault());
                String currentDate = df.format(c);

                roundKey = mDatabase.child("rounds").push().getKey();

                TextView courseNameTxt = (EditText)getView().findViewById(R.id.courseNameEntry);
                final String courseName = courseNameTxt.getText().toString();
                TextView numPar3Txt = (EditText)getView().findViewById(R.id.numPar3sEntry);
                final String numPar3s = numPar3Txt.getText().toString();
                int numPar3 = Integer.valueOf(numPar3s);
                TextView parCourseTxt = (EditText)getView().findViewById(R.id.parCourseEntry);
                final String parCourse = parCourseTxt.getText().toString();
                TextView scoreTxt = (EditText)getView().findViewById(R.id.scoreEntry);
                final String scores = scoreTxt.getText().toString();
                int score = Integer.valueOf(scores);
                TextView totalFIRtxt = (EditText)getView().findViewById(R.id.totalFIREntry);
                final String totalFIRs = totalFIRtxt.getText().toString();
                int totalFIR = Integer.valueOf(totalFIRs);
                TextView totalGIRtxt = (EditText)getView().findViewById(R.id.totalGIREntry);
                final String totalGIRs = totalGIRtxt.getText().toString();
                int totalGIR = Integer.valueOf(totalGIRs);
                TextView totalPuttstxt = (EditText)getView().findViewById(R.id.totalPuttsEntry);
                final String totalPuttss = totalPuttstxt.getText().toString();
                int totalPutts = Integer.valueOf(totalPuttss);


                Round round = new Round(userId, "demo" ,courseName ,parCourse ,currentDate , score, totalPutts, totalGIR,totalFIR,numPar3);
                Map<String, Object> roundValues = round.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/rounds/" + roundKey, roundValues);

                mDatabase.updateChildren(childUpdates);
            }
        });

        subHole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView distanceTxt = (EditText)getView().findViewById(R.id.distanceEntry);
                final String distanceString = distanceTxt.getText().toString();
                TextView parTxt = (EditText)getView().findViewById(R.id.parEntry);
                final String par = parTxt.getText().toString();
                TextView holeNumTxt = (EditText)getView().findViewById(R.id.holeNumEntry);
                final String holeNum = holeNumTxt.getText().toString();
                TextView scoreHoleTxt = (EditText)getView().findViewById(R.id.scoreHoleEntry);
                final String scoreHole = scoreHoleTxt.getText().toString();
                TextView puttsTxt = (EditText)getView().findViewById(R.id.PuttsEntry);
                final String puttsS = puttsTxt.getText().toString();
                int putts = Integer.valueOf(puttsS);

                holeKey = mDatabase.child("holes").push().getKey();
                Holes hole = new Holes(roundKey, userId, distanceString, par, holeNum,scoreHole,putts);
                Map<String, Object> holeValues = hole.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/holes/" + holeKey, holeValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber=1;
            }
        });

        subShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView shotDistanceTxt = (EditText)getView().findViewById(R.id.shotDistanceEntry);
                final String shotDistance = shotDistanceTxt.getText().toString();
                TextView clubTxt = (EditText)getView().findViewById(R.id.clubEntry);
                final String club = clubTxt.getText().toString();
                TextView shotNumTxt = (EditText)getView().findViewById(R.id.shotNumEntry);
                final String shotNum = shotNumTxt.getText().toString();
                TextView lieBallTxt = (EditText)getView().findViewById(R.id.lieBallEntry);
                final String lie_ball = lieBallTxt.getText().toString();

                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", shotDistance, club, shotNum, lie_ball);
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;

            }
        });

        driverB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "230", "Driver", String.valueOf(shotNumber), "Tee Box");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        Wood3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "215", "3Wood", String.valueOf(shotNumber), "Tee Box");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        Wood5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "195", "5Wood", String.valueOf(shotNumber), "Tee Box");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        Wood7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "187", "7Wood", String.valueOf(shotNumber), "Tee Box");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        iron3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "180", "3-iron", String.valueOf(shotNumber), "Fairway");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        iron4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "170", "4-iron", String.valueOf(shotNumber), "Fairway");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        iron5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "160", "5-iron", String.valueOf(shotNumber), "Fairway");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        iron6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "150", "6-iron", String.valueOf(shotNumber), "Fairway");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        iron7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "140", "7-iron", String.valueOf(shotNumber), "Fairway");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        iron8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "130", "8-iron", String.valueOf(shotNumber), "Fairway");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        iron9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "120", "9-iron", String.valueOf(shotNumber), "Fairway");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        PWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "105", "Pitching-Wedge", String.valueOf(shotNumber), "Tee Box");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        SWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "80", "Sand-Wedge", String.valueOf(shotNumber), "Tee Box");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        GWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "70", "Gap-Wedge", String.valueOf(shotNumber), "Tee Box");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        LWedge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "60", "Lob-Wedge", String.valueOf(shotNumber), "Tee Box");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });

        Putter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotKey = mDatabase.child("shots").push().getKey();
                Shots shot = new Shots(holeKey,userId,roundKey, "0", "0", "Putter", String.valueOf(shotNumber), "Tee Box");
                Map<String, Object> shotValues = shot.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/shots/" + shotKey, shotValues);

                mDatabase.updateChildren(childUpdates);

                shotNumber++;
            }
        });


        return root;
    }
}