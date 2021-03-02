package com.cbrady.personalcaddy.ui.dashboard;

import android.graphics.Color;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private List<Round>  roundTempList1, roundTempList2;
    int[] scores = new int[5];
    float[] putts = new float[5];
    float[] avgPutts = new float[5];
    String[] dates = new String[5];
    String[] clubNames = {"Driver", "3Wood", "5Wood", "3-iron", "4-iron", "5-iron", "6-iron", "7-iron", "8-iron", "9-iron", "PW", "SW"};
    int[] clubUsage = new int[12];
    //variables to store the usage of each club
    int DriverUsage, Wood3Usage, Wood5Usage, iron3Usage, iron4Usage, iron5Usage, iron6Usage, iron7Usage, iron8Usage, iron9Usage, PWUsage, SWUsage = 0;
    String actualDistance = "";
    int x = 0;
    private LineChart lineChartScores;
    private LineChart lineChartPutts;
    ArrayList<Entry> yEntrys = new ArrayList<>();
    ArrayList<String> xEntrys = new ArrayList<>();
    ArrayList<Entry> yEntrys2 = new ArrayList<>();
    ArrayList<String> xEntrys2 = new ArrayList<>();
    BarChart clubUsageChart;


    public StatisticsFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);

        shotTempList = new ArrayList<>();
        roundTempList1 = new ArrayList<>();
        roundTempList2 = new ArrayList<>();
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference("shots");
        // [END create_database_reference]

        lineChartScores = root.findViewById(R.id.lineChartScore);
        lineChartScores.setTouchEnabled(true);
        lineChartScores.setPinchZoom(true);
        lineChartPutts = root.findViewById(R.id.lineChartPutts);
        lineChartPutts.setTouchEnabled(true);
        lineChartPutts.setPinchZoom(true);
        clubUsageChart = root.findViewById(R.id.barChartUsage);

        //final String userId = getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();


        Query query1 =  FirebaseDatabase.getInstance().getReference("rounds").orderByChild("uid").equalTo(uid).limitToLast(5);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot rounds : snapshot.getChildren()) {
                        Round round = rounds.getValue(Round.class);
                        roundTempList1.add(round);

                        int score = round.getScore();
                        int total_putts = round.getTotalPutts();
                        String date = round.getCurrentDate();
                        String delim = "[ ]+";
                        String[] dateOwn = date.split(delim);

                        scores[x] = score;

                        putts[x] = (float)total_putts/(float)18;
                        dates[x] = dateOwn[0];
                        //Log.d("SCORES", String.valueOf(score) + " date: " + dateOwn[0] + " x = " + String.valueOf(scores[x]));
                        x++;

                    }
                }

                for(int i=0; i<scores.length; i++){
                    Log.d("SCORES", "Score = " + String.valueOf(scores[i]) + " date =  " + String.valueOf(dates[i]));
                }

                //call graph
                makeScoresGraph(scores,dates);
                //call graph
                makePuttsGraph(putts,dates);

                x=0;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    public void makeScoresGraph(int[] scores, String[] dates){
        //calculating average club values and putting into an array
        for(int i=0; i<scores.length; i++){
            yEntrys.add(new Entry(i, scores[i]));
        }

        for(int i=0; i<dates.length; i++){
            xEntrys.add(dates[i]);
        }

        LineDataSet lineDataSet = new LineDataSet(yEntrys, "Data Set 1");

        XAxis xAxis = lineChartScores.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        final String weekArr[]={"Week 1 ","Week 2","Week 3","Week 4"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis = lineChartScores.getAxisRight();
        rightAxis.setEnabled(false);


        lineChartScores.getAxisLeft().setAxisMaxValue(150f);
        lineChartScores.getAxisLeft().setAxisMinValue(50f);

        LineData data = new LineData(lineDataSet);
        lineChartScores.setData(data);
        lineChartScores.invalidate();


    }

    public void makePuttsGraph(float[] avgPutts, String[] dates){
        //calculating average club values and putting into an array
        for(int i=0; i<avgPutts.length; i++){
            yEntrys2.add(new Entry(i, avgPutts[i]));
        }

        for(int i=0; i<dates.length; i++){
            xEntrys2.add(dates[i]);
        }

        LineDataSet lineDataSet2 = new LineDataSet(yEntrys2, "Data Set 1");

        XAxis xAxis2 = lineChartPutts.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLines(true);
        xAxis2.setGranularity(1f);
        xAxis2.setGranularityEnabled(true);
        xAxis2.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis2 = lineChartPutts.getAxisRight();
        rightAxis2.setEnabled(false);


        lineChartPutts.getAxisLeft().setAxisMaxValue(5f);
        lineChartPutts.getAxisLeft().setAxisMinValue(0f);

        LineData data = new LineData(lineDataSet2);
        lineChartPutts.setData(data);
        lineChartPutts.invalidate();

        //calling the next query
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Query query2 =  FirebaseDatabase.getInstance().getReference("shots").orderByChild("UserId").equalTo("okwgaFDy6ffFWRh0JBpCO1T2ODJ3");
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot shots : dataSnapshot.getChildren()) {
                        Shots shot = shots.getValue(Shots.class);
                        shotTempList.add(shot);

                        String club = shot.getClub();


                        switch(club){
                            case "Driver":
                                DriverUsage++;
                                break;
                            case "3Wood":
                                Wood3Usage++;
                                break;
                            case "5Wood":
                                Wood5Usage++;
                                break;
                            case "3-iron":
                                iron3Usage++;
                                break;
                            case "4-iron":
                                iron4Usage++;
                                break;
                            case "5-iron":
                                iron5Usage++;
                                break;
                            case "6-iron":
                                iron6Usage++;
                                break;
                            case "7-iron":
                                iron7Usage++;
                                break;
                            case "8-iron":
                                iron8Usage++;
                                break;
                            case "9-iron":
                                iron9Usage++;
                                break;
                            case "Pitching Wedge":
                                PWUsage++;
                                break;
                            case "Sand Wedge":
                                SWUsage++;
                                break;
                            default:
                                break;
                        }


                    }
                }
                
                clubUsage[0] = DriverUsage;
                clubUsage[1] = Wood3Usage;
                clubUsage[2] = Wood5Usage;
                clubUsage[3] = iron3Usage;
                clubUsage[4] = iron4Usage;
                clubUsage[5] = iron5Usage;
                clubUsage[6] = iron6Usage;
                clubUsage[7] = iron7Usage;
                clubUsage[8] = iron8Usage;
                clubUsage[9] = iron9Usage;
                clubUsage[10] = PWUsage;
                clubUsage[11] = SWUsage;

                for(int i=0; i<clubUsage.length; i++){
                    Log.d("USAGE", String.valueOf(clubNames[i]) + " = " + String.valueOf(clubUsage[i]));
                }

                makeClubUsageChart(clubUsage);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void makeClubUsageChart(int[] clubUsage){
        ArrayList<BarEntry> usages = new ArrayList<>();
        ArrayList<BarEntry> yEntrys3 = new ArrayList<>();
        ArrayList<String> xEntrys3 = new ArrayList<>();

        for(int i=0; i<clubUsage.length; i++){
            yEntrys3.add(new BarEntry(i, clubUsage[i]));
        }

        for(int i=0; i<clubNames.length; i++){
            xEntrys3.add(clubNames[i]);
        }

        BarDataSet barDataSet = new BarDataSet(yEntrys3, "Number of times each club was used");

        XAxis xAxis = clubUsageChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(clubNames));
        xAxis.setLabelRotationAngle(30);
        xAxis.setLabelCount(11);
        YAxis rightAxis = clubUsageChart.getAxisRight();
        rightAxis.setEnabled(false);

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        clubUsageChart.setFitBars(true);
        clubUsageChart.setData(barData);
        clubUsageChart.getDescription().setText("Bar Chart Example");
        clubUsageChart.animateY(2000);

    }
}