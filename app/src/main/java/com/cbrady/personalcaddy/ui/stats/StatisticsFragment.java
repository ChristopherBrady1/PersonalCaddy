package com.cbrady.personalcaddy.ui.stats;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.models.Shots;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatisticsFragment extends Fragment {

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private RecyclerView mRecycler;
    private List<Shots> shotTempList;
    private List<Round>  roundTempList1, roundTempList2;
    int[] scores = new int[5];
    float[] putts = new float[5];
    float[] firPercentage = new float[5];
    float[] girPercentage = new float[5];
    int[] girTotals = new int[5];
    float[] avgPutts = new float[5];
    String[] dates = new String[5];
    Context mContext;


    //club names Array list
    ArrayList<String> club_namesAL = new ArrayList<>();

    ArrayList<String> club_namesALTemp = new ArrayList<>();
    //usage arrayList
    ArrayList<Integer> club_usageAL = new ArrayList<>();
    //total arrayList
    ArrayList<Float> club_totalAL = new ArrayList<>();
    //average arrayList
    ArrayList<Float> club_avgAL = new ArrayList<>();



    String actualDistance = "";
    int x = 0;
    private LineChart lineChartScores;
    private LineChart lineChartPutts;
    private LineChart lineChartFir;
    private LineChart lineChartGir;
    ArrayList<Entry> yEntrys = new ArrayList<>();
    ArrayList<String> xEntrys = new ArrayList<>();
    ArrayList<Entry> yEntrys2 = new ArrayList<>();
    ArrayList<String> xEntrys2 = new ArrayList<>();
    BarChart clubUsageChart;


    public StatisticsFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
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
        lineChartGir = root.findViewById(R.id.lineChartGir);
        lineChartGir.setTouchEnabled(true);
        lineChartGir.setPinchZoom(true);
        lineChartFir = root.findViewById(R.id.lineChartFir);
        lineChartFir.setTouchEnabled(true);
        lineChartFir.setPinchZoom(true);
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
                        int totalFIR = round.getTotalFIR();
                        int par3s = round.getNumPar3s();
                        int totalGIR = round.getTotalGIR();
                        String date = round.getCurrentDate();
                        String delim = "[ ]+";
                        String[] dateOwn = date.split(delim);

                        scores[x] = score;
                        girTotals[x] = totalGIR;
                        int numHolesFir = 18-par3s;
                        firPercentage[x] = (((float)totalFIR/(float)numHolesFir)*(float)100);
                        girPercentage[x] = (((float)totalGIR/(float)18)*(float)100);

                        putts[x] = (float)total_putts/(float)18;
                        dates[x] = dateOwn[0];
                        Log.d("SCORES", String.valueOf(score) + " date: " + dateOwn[0] + " x = " + String.valueOf(scores[x]));
                        x++;

                    }
                }
                for(int i=0; i<scores.length; i++){
                    Log.d("SCORES", "Score = " + String.valueOf(scores[i]) + " date =  " + String.valueOf(dates[i]));
                }
                //call graphs
                makeScoresGraph(scores,dates);
                makePuttsGraph(putts,dates);
                makeGirGraph(girPercentage,dates);
                makeFirGraph(firPercentage,dates);
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

        LineDataSet lineDataSet = new LineDataSet(yEntrys, "Last 5 Scores");
        int color = ContextCompat.getColor(mContext, R.color.purple_500);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setColor(color);
        lineDataSet.setLineWidth(2);

        XAxis xAxis = lineChartScores.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(90);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis = lineChartScores.getAxisRight();
        YAxis leftAxis = lineChartScores.getAxisLeft();
        rightAxis.setEnabled(false);
        leftAxis.setDrawLabels(false);

        Legend legend = lineChartScores.getLegend();
        legend.setTextSize(15);

        lineChartScores.getDescription().setEnabled(false);
        lineChartScores.getAxisLeft().setAxisMaxValue(150f);
        lineChartScores.getAxisLeft().setAxisMinValue(50f);

        LineData data = new LineData(lineDataSet);
        lineChartScores.setExtraBottomOffset(5);
        lineChartScores.setData(data);
        lineChartScores.invalidate();


    }

    public void makeFirGraph(float[] firPercentages, String[] dates){
        //calculating average club values and putting into an array

        ArrayList<Entry> yEntrysFir = new ArrayList<>();
        ArrayList<String> xEntrysFir = new ArrayList<>();

        for(int i=0; i<firPercentages.length; i++){
            yEntrysFir.add(new Entry(i, firPercentages[i]));
        }

        for(int i=0; i<dates.length; i++){
            xEntrysFir.add(dates[i]);
        }

        LineDataSet lineDataSetFir = new LineDataSet(yEntrysFir, "Fairways in Regulation (%)");
        int color = ContextCompat.getColor(mContext, R.color.teal_700);
        lineDataSetFir.setValueTextSize(12);
        lineDataSetFir.setColor(color);
        lineDataSetFir.setLineWidth(2);

        XAxis xAxis = lineChartFir.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setLabelRotationAngle(90);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis = lineChartFir.getAxisRight();
        YAxis leftAxis = lineChartFir.getAxisLeft();
        rightAxis.setEnabled(false);
        leftAxis.setDrawLabels(false);

        Legend legend = lineChartFir.getLegend();
        legend.setTextSize(15);

        lineChartFir.getDescription().setEnabled(false);
        lineChartFir.getAxisLeft().setAxisMaxValue(100f);
        lineChartFir.getAxisLeft().setAxisMinValue(0f);

        LineData data = new LineData(lineDataSetFir);
        lineChartFir.setData(data);
        lineChartFir.invalidate();


    }

    public void makeGirGraph(float[] girPercentage, String[] dates){
        //calculating average club values and putting into an array

        ArrayList<Entry> yEntrysGir = new ArrayList<>();
        ArrayList<String> xEntrysGir = new ArrayList<>();

        for(int i=0; i<girPercentage.length; i++){
            yEntrysGir.add(new Entry(i, girPercentage[i]));
        }

        for(int i=0; i<dates.length; i++){
            xEntrysGir.add(dates[i]);
        }

        LineDataSet lineDataSetGir = new LineDataSet(yEntrysGir, "Greens in Regulation (%)");
        int color = ContextCompat.getColor(mContext, R.color.orange);
        lineDataSetGir.setValueTextSize(12);
        lineDataSetGir.setColor(color);
        lineDataSetGir.setLineWidth(2);

        XAxis xAxis = lineChartGir.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(90);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis = lineChartGir.getAxisRight();
        YAxis leftAxis = lineChartGir.getAxisLeft();
        rightAxis.setEnabled(false);
        leftAxis.setDrawLabels(false);


        Legend legend = lineChartGir.getLegend();
        legend.setTextSize(15);

        lineChartGir.getDescription().setEnabled(false);
        lineChartGir.getAxisLeft().setAxisMaxValue(100f);
        lineChartGir.getAxisLeft().setAxisMinValue(0f);

        LineData data = new LineData(lineDataSetGir);
        lineChartGir.setData(data);
        lineChartGir.invalidate();


    }

    public void makePuttsGraph(float[] avgPutts, String[] dates){
        //calculating average club values and putting into an array
        for(int i=0; i<avgPutts.length; i++){
            yEntrys2.add(new Entry(i, avgPutts[i]));
        }

        for(int i=0; i<dates.length; i++){
            xEntrys2.add(dates[i]);
        }

        LineDataSet lineDataSet2 = new LineDataSet(yEntrys2, "Average Putts per Round");
        int color = ContextCompat.getColor(mContext, R.color.green);
        lineDataSet2.setValueTextSize(12);
        lineDataSet2.setColor(color);
        lineDataSet2.setLineWidth(2);

        XAxis xAxis2 = lineChartPutts.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLines(true);
        xAxis2.setGranularity(1f);
        xAxis2.setLabelRotationAngle(90);
        xAxis2.setGranularityEnabled(true);
        xAxis2.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis2 = lineChartPutts.getAxisRight();
        YAxis leftAxis = lineChartPutts.getAxisLeft();
        rightAxis2.setEnabled(false);
        leftAxis.setDrawLabels(false);


        Legend legend = lineChartPutts.getLegend();
        legend.setTextSize(15);

        lineChartPutts.getDescription().setEnabled(false);
        lineChartPutts.getAxisLeft().setAxisMaxValue(5f);
        lineChartPutts.getAxisLeft().setAxisMinValue(0f);

        LineData data = new LineData(lineDataSet2);
        lineChartPutts.setData(data);
        lineChartPutts.invalidate();

        //setting club names arrayList
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String[] arrayString = getResources().getStringArray(R.array.clubs);
        Set<String> sets = new HashSet<>(Arrays.asList(arrayString));
        Set<String> setNew = sharedPrefs.getStringSet("club_List",sets);
        for (String str : setNew)
            club_namesALTemp.add(str);


        Collections.sort(club_namesALTemp);
        club_namesAL = club_namesALTemp;

        for (int i = 0; i < club_namesAL.size(); i++) {
            String[] str = club_namesAL.get(i).split(" ");
            if (str.length > 1) {
                club_namesAL.set(i, str[1]);
            }
            Log.d("Order:", club_namesAL.get(i));
        }

        float zero =0;
        //setting club usage and total Array List
        for(int i=0; i< setNew.size(); i++){
            club_usageAL.add(0);
            club_totalAL.add(zero);
            club_avgAL.add(zero);
        }

        //calling the next query
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Query query2 =  FirebaseDatabase.getInstance().getReference("shots").orderByChild("userId").equalTo(uid);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot shots : dataSnapshot.getChildren()) {
                        Shots shot = shots.getValue(Shots.class);
                        shotTempList.add(shot);
                        String club = shot.getClub();

                        //incrementing the amount of times each club is used
                        for(int i =0; i<club_namesAL.size(); i++){
                            if(club_namesAL.get(i).equals(club)){
                                actualDistance = shot.getActualDistance();
                                float dist = Float.parseFloat(actualDistance);
                                club_usageAL.set(i,club_usageAL.get(i) + 1);
                                club_totalAL.set(i,club_totalAL.get(i) + dist);
                            }

                        }

                    }
                }
                for(int i =0; i<club_avgAL.size(); i++){
                    club_avgAL.set(i,(club_totalAL.get(i)/club_usageAL.get(i)));
                    Log.d("Calc", club_namesAL.get(i) + ": "+ club_avgAL.get(i));

                }
                int y=0;
                for(String str: club_namesAL){
                    Log.d("USAGE", str + ": " + club_usageAL.get(y));
                    y++;
                }
                makeClubAvgChart();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void makeClubAvgChart(){
        ArrayList<BarEntry> usages = new ArrayList<>();
        ArrayList<BarEntry> yEntrys3 = new ArrayList<>();
        ArrayList<String> xEntrys3 = new ArrayList<>();

        for(int i=0; i<club_avgAL.size(); i++){
            yEntrys3.add(new BarEntry(i, club_avgAL.get(i)));
        }

        for(String str: club_namesAL){
            xEntrys3.add(str);
        }


        BarDataSet barDataSet = new BarDataSet(yEntrys3, "Average Distance for each club");
        barDataSet.setValueTextSize(12);


        XAxis xAxis = clubUsageChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(club_namesAL));
        xAxis.setLabelRotationAngle(90);
        xAxis.setLabelCount(club_namesAL.size());
        YAxis rightAxis = clubUsageChart.getAxisRight();
        rightAxis.setEnabled(false);

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);

        Legend legend = clubUsageChart.getLegend();
        legend.setTextSize(15);

        clubUsageChart.setFitBars(true);
        clubUsageChart.setData(barData);
        clubUsageChart.animateY(2000);

    }
}