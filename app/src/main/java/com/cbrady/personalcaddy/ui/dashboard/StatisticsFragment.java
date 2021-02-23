package com.cbrady.personalcaddy.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.models.ShotTemp;
import com.cbrady.personalcaddy.models.Shots;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private RecyclerView mRecycler;
    private List<Shots> shotTempList;
    private List<Round>  roundTempList;
    int[] scores = new int[5];
    String[] dates = new String[5];
    String[] clubNames = {"Driver", "3Wood", "5Wood", "3-iron", "4-iron", "5-iron", "6-iron", "7-iron", "8-iron", "9-iron", "Pitching Wedge", "Sand Wedge"};
    float[] avgClub = new float[12];
    //variables to store the average of each club
    float avgDriver, avg3Wood, avg5Wood, avg3iron, avg4iron, avg5iron, avg6iron, avg7iron, avg8iron, avg9iron, avgPW, avgSW = 0;
    float avgDriverTot, avg3WoodTot, avg5WoodTot, avg3ironTot, avg4ironTot, avg5ironTot, avg6ironTot, avg7ironTot, avg8ironTot, avg9ironTot, avgPWTot, avgSWTot = 0;
    float avgDriverNum, avg3WoodNum, avg5WoodNum, avg3ironNum, avg4ironNum, avg5ironNum, avg6ironNum, avg7ironNum, avg8ironNum, avg9ironNum, avgPWNum, avgSWNum = 0;
    String actualDistance = "";
    int x = 0;
    private LineChart lineChart;
    ArrayList<Entry> yEntrys = new ArrayList<>();
    ArrayList<String> xEntrys = new ArrayList<>();


    public StatisticsFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);

        shotTempList = new ArrayList<>();
        roundTempList = new ArrayList<>();
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference("shots");
        // [END create_database_reference]

        lineChart = root.findViewById(R.id.lineChart);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);

        Query query1 =  FirebaseDatabase.getInstance().getReference("rounds").orderByChild("uid").equalTo("z353UXcdNJam9yT1s8qoiW4CX4j2").limitToLast(5);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot rounds : snapshot.getChildren()) {
                        Round round = rounds.getValue(Round.class);
                        roundTempList.add(round);

                        int score = round.getScore();
                        String date = round.getCurrentDate();
                        String delim = "[ ]+";
                        String[] dateOwn = date.split(delim);

                        scores[x] = score;
                        dates[x] = dateOwn[0];
                        //Log.d("SCORES", String.valueOf(score) + " date: " + dateOwn[0] + " x = " + String.valueOf(scores[x]));
                        x++;

                    }
                }

                for(int i=0; i<scores.length; i++){
                    Log.d("SCORES", "Score = " + String.valueOf(scores[i]) + " date =  " + String.valueOf(dates[i]));
                }

                //call graph
                makeGraph(scores,dates);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                    //float counter = 1;
                    //int j = 0;
                    //float temp = 0;
                    String prevClub = "";
                    for (DataSnapshot shots : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Shots shot = shots.getValue(Shots.class);
                        shotTempList.add(shot);

                        //String actualDistance = shot.getActualDistance();
                        String club = shot.getClub();

                        //if previous club was the different
                        /*if(!prevClub.equals(club)){
                            avgClub[j] = temp/counter;
                            Log.d("AVERAGES", String.valueOf(clubNames[j]) + " = " + String.valueOf(avgClub[j]));
                            j++;
                            temp = 0;
                            counter =0;
                        }

                        switch(club){
                            case "Driver":
                                actualDistance = shot.getActualDistance();
                                avgDriverTot = avgDriverTot + Integer.parseInt(actualDistance);
                                avgDriverNum++;
                                //Log.d("AVERAGES", "THIS WORKED -- > avg = " + String.valueOf(avgDriverTot));
                                break;
                            case "3Wood":
                                actualDistance = shot.getActualDistance();
                                avg3WoodTot = avg3WoodTot + Integer.parseInt(actualDistance);
                                avg3WoodNum++;
                                break;
                            case "5Wood":
                                actualDistance = shot.getActualDistance();
                                avg5WoodTot = avg5WoodTot + Integer.parseInt(actualDistance);
                                avg5WoodNum++;
                                break;
                            case "3-iron":
                                actualDistance = shot.getActualDistance();
                                avg3ironTot = avg3ironTot + Integer.parseInt(actualDistance);
                                avg3ironNum++;
                                break;
                            case "4-iron":
                                actualDistance = shot.getActualDistance();
                                avg4ironTot = avg4ironTot + Integer.parseInt(actualDistance);
                                avg4ironNum++;
                                break;
                            case "5-iron":
                                actualDistance = shot.getActualDistance();
                                avg5ironTot = avg5ironTot + Integer.parseInt(actualDistance);
                                avg5ironNum++;
                                break;
                            case "6-iron":
                                actualDistance = shot.getActualDistance();
                                avg6ironTot = avg6ironTot + Integer.parseInt(actualDistance);
                                avg6ironNum++;
                                break;
                            case "7-iron":
                                actualDistance = shot.getActualDistance();
                                avg7ironTot = avg7ironTot + Integer.parseInt(actualDistance);
                                avg7ironNum++;
                                break;
                            case "8-iron":
                                actualDistance = shot.getActualDistance();
                                avg8ironTot = avg8ironTot + Integer.parseInt(actualDistance);
                                avg8ironNum++;
                                break;
                            case "9-iron":
                                actualDistance = shot.getActualDistance();
                                avg9ironTot = avg9ironTot + Integer.parseInt(actualDistance);
                                avg9ironNum++;
                                break;
                            case "Pitching Wedge":
                                actualDistance = shot.getActualDistance();
                                avgPWTot = avgPWTot + Integer.parseInt(actualDistance);
                                avgPWNum++;
                                break;
                            case "Sand Wedge":
                                actualDistance = shot.getActualDistance();
                                avgSWTot = avgSWTot + Integer.parseInt(actualDistance);
                                avgSWNum++;
                                break;
                            default:
                                break;
                        }

                        /*
                        //getting the average for each club
                        for(int i=0; i<clubNames.length; i++){

                            if(club.equals(clubNames[i])){
                                String actualDistance = shot.getActualDistance();
                                temp = temp + Integer.parseInt(actualDistance);
                                //Log.d("AVERAGES", " Temp : " + String.valueOf(temp));
                                counter++;
                                prevClub = club;
                            }

                        }

                    }
                }

                avgDriver = avgDriverTot/avgDriverNum;
                Log.d("AVERAGES", "Here is the avgDriver--> " + String.valueOf(avgDriver));
                avg3Wood = avg3WoodTot/avg3WoodNum;
                avg5Wood = avg5WoodTot/avg5WoodNum;
                avg3iron = avg3ironTot/avg3ironNum;
                avg4iron = avg4ironTot/avg4ironNum;
                avg5iron = avg5ironTot/avg5ironNum;
                avg6iron = avg6ironTot/avg6ironNum;
                avg7iron = avg7ironTot/avg7ironNum;
                avg8iron = avg8ironTot/avg8ironNum;
                avg9iron = avg9ironTot/avg9ironNum;
                avgPW = avgPWTot/avgPWNum;
                avgSW = avgSWTot/avgSWNum;

                avgClub[0] = avgDriver;
                avgClub[1] = avg3Wood;
                avgClub[2] = avg5Wood;
                avgClub[3] = avg3iron;
                avgClub[4] = avg4iron;
                avgClub[5] = avg5iron;
                avgClub[6] = avg6iron;
                avgClub[7] = avg7iron;
                avgClub[8] = avg8iron;
                avgClub[9] = avg9iron;
                avgClub[10] = avgPW;
                avgClub[11] = avgSW;

                for(int i=0; i<avgClub.length; i++){
                    Log.d("AVERAGES", String.valueOf(clubNames[i]) + " = " + String.valueOf(avgClub[i]));
                }
            }


            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



        return root;
    }

    public void makeGraph(int[] scores, String[] dates){
        //calculating average club values and putting into an array
        for(int i=0; i<scores.length; i++){
            yEntrys.add(new Entry(i, scores[i]));
        }

        for(int i=0; i<dates.length; i++){
            xEntrys.add(dates[i]);
        }

        LineDataSet lineDataSet = new LineDataSet(yEntrys, "Data Set 1");

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        final String weekArr[]={"Week 1 ","Week 2","Week 3","Week 4"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);


        lineChart.getAxisLeft().setAxisMaxValue(150f);
        lineChart.getAxisLeft().setAxisMinValue(50f);

        LineData data = new LineData(lineDataSet);
        lineChart.setData(data);
        lineChart.invalidate();
    }
}