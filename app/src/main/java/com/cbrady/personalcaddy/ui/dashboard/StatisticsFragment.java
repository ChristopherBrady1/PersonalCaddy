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
    String[] clubNames = {"Driver", "3Wood", "5Wood", "3-iron", "4-iron", "5-iron", "6-iron", "7-iron", "8-iron", "9-iron", "Pitching Wedge", "Sand Wedge"};
    float[] avgClub = new float[12];
    //variables to store the average of each club
    float avgDriver, avg3Wood, avg5Wood, avg3iron, avg4iron, avg5iron, avg6iron, avg7iron, avg8iron, avg9iron, avgPW, avgSW = 0;
    float avgDriverTot, avg3WoodTot, avg5WoodTot, avg3ironTot, avg4ironTot, avg5ironTot, avg6ironTot, avg7ironTot, avg8ironTot, avg9ironTot, avgPWTot, avgSWTot = 0;
    float avgDriverNum, avg3WoodNum, avg5WoodNum, avg3ironNum, avg4ironNum, avg5ironNum, avg6ironNum, avg7ironNum, avg8ironNum, avg9ironNum, avgPWNum, avgSWNum = 0;
    String actualDistance = "";
    int x = 0;
    private LineChart lineChartScores;
    private LineChart lineChartPutts;
    ArrayList<Entry> yEntrys = new ArrayList<>();
    ArrayList<String> xEntrys = new ArrayList<>();
    ArrayList<Entry> yEntrys2 = new ArrayList<>();
    ArrayList<String> xEntrys2 = new ArrayList<>();


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
       //Query query2 =  FirebaseDatabase.getInstance().getReference("rounds").orderByChild("uid").equalTo(uid).limitToLast(5);


    }
}