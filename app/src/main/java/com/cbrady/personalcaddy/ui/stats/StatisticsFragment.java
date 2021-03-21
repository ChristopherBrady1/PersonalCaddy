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
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.models.Shots;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
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
    int[] girTotals = new int[5];
    float[] avgPutts = new float[5];
    String[] dates = new String[5];
    Context mContext;


    //club names Array list
    ArrayList<String> club_namesAL = new ArrayList<>();
    //usage arrayList
    ArrayList<Integer> club_usageAL = new ArrayList<>();


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



        //setting club names arrayList
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String[] arrayString = getResources().getStringArray(R.array.clubs);
        Set<String> sets = new HashSet<>(Arrays.asList(arrayString));
        Set<String> setNew = sharedPrefs.getStringSet("club_List",sets);
        for (String str : setNew)
            club_namesAL.add(str);

        //setting club usage Array List
        for(int i=0; i< setNew.size(); i++){
            club_usageAL.add(0);
        }


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

                        putts[x] = (float)total_putts/(float)18;
                        dates[x] = dateOwn[0];
                        Log.d("SCORES", String.valueOf(score) + " date: " + dateOwn[0] + " x = " + String.valueOf(scores[x]));
                        x++;

                    }
                }

                for(int i=0; i<scores.length; i++){
                    Log.d("SCORES", "Score = " + String.valueOf(scores[i]) + " date =  " + String.valueOf(dates[i]));
                }
                for(int i=0; i<girTotals.length; i++){
                    Log.d("FIR", "Fir = " + String.valueOf(firPercentage[i]) + " Gir =  " + String.valueOf(girTotals[i]));
                }

                //call graphs
                makeScoresGraph(scores,dates);
                makePuttsGraph(putts,dates);
                makeGirGraph(girTotals,dates);
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

        XAxis xAxis = lineChartScores.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis = lineChartScores.getAxisRight();
        rightAxis.setEnabled(false);


        lineChartScores.getAxisLeft().setAxisMaxValue(150f);
        lineChartScores.getAxisLeft().setAxisMinValue(50f);

        LineData data = new LineData(lineDataSet);
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

        LineDataSet lineDataSetFir = new LineDataSet(yEntrysFir, "Fairways in Regulation");

        XAxis xAxis = lineChartFir.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis = lineChartFir.getAxisRight();
        rightAxis.setEnabled(false);


        lineChartFir.getAxisLeft().setAxisMaxValue(100f);
        lineChartFir.getAxisLeft().setAxisMinValue(0f);

        LineData data = new LineData(lineDataSetFir);
        lineChartFir.setData(data);
        lineChartFir.invalidate();


    }

    public void makeGirGraph(int[] girTotals, String[] dates){
        //calculating average club values and putting into an array

        ArrayList<Entry> yEntrysGir = new ArrayList<>();
        ArrayList<String> xEntrysGir = new ArrayList<>();

        for(int i=0; i<girTotals.length; i++){
            yEntrysGir.add(new Entry(i, girTotals[i]));
        }

        for(int i=0; i<dates.length; i++){
            xEntrysGir.add(dates[i]);
        }

        LineDataSet lineDataSetGir = new LineDataSet(yEntrysGir, "Greens in Regulation");

        XAxis xAxis = lineChartGir.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        YAxis rightAxis = lineChartGir.getAxisRight();
        rightAxis.setEnabled(false);


        lineChartGir.getAxisLeft().setAxisMaxValue(18f);
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
        Query query2 =  FirebaseDatabase.getInstance().getReference("shots").orderByChild("userId").equalTo(uid);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot shots : dataSnapshot.getChildren()) {
                        Shots shot = shots.getValue(Shots.class);
                        shotTempList.add(shot);

                        String club = shot.getClub();
                        int x =0;



                        //incrementing the amount of times each club is used
                        for(String str: club_namesAL){
                            if(club.equals(str)){
                                club_usageAL.set(x,club_usageAL.get(x) + 1);
                            }
                            x++;
                        }

                    }
                }


                int y=0;
                for(String str: club_namesAL){
                    Log.d("USAGE", str + ": " + club_usageAL.get(y));
                    y++;
                }

                makeClubUsageChart();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void makeClubUsageChart(){
        ArrayList<BarEntry> usages = new ArrayList<>();
        ArrayList<BarEntry> yEntrys3 = new ArrayList<>();
        ArrayList<String> xEntrys3 = new ArrayList<>();

        for(int i=0; i<club_usageAL.size(); i++){
            yEntrys3.add(new BarEntry(i, club_usageAL.get(i)));
        }

        for(String str: club_namesAL){
            xEntrys3.add(str);
        }


        BarDataSet barDataSet = new BarDataSet(yEntrys3, "Number of times each club was used");

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

        clubUsageChart.setFitBars(true);
        clubUsageChart.setData(barData);
        clubUsageChart.getDescription().setText("Bar Chart Example");
        clubUsageChart.animateY(2000);

    }
}