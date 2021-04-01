package com.cbrady.personalcaddy.ui.clubChoice;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.models.ShotTemp;
import com.cbrady.personalcaddy.models.Shots;
import com.cbrady.personalcaddy.ui.map.MapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
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

public class clubChoice extends Fragment {
    public clubChoice() {}

    Context mContext;
    FloatingActionButton submitShotDetails;
    private DatabaseReference mDatabase;
    String[] club;
    clubChoice.SpinnerAdapter adapter;
    private Button btnDisplay;
    String currentClub;
    private List<Shots> shotTempList;
    Boolean sorted = false;
    //club names Array list
    ArrayList<String> club_namesAL = new ArrayList<>();
    ArrayList<String> club_namesALTemp = new ArrayList<>();
    //usage arrayList
    ArrayList<Float> club_usageAL = new ArrayList<>();
    //total arrayList
    ArrayList<Float> club_totalAL = new ArrayList<>();
    //average arrayList
    ArrayList<Float> club_avgAL = new ArrayList<>();

    String actualDistance = "";
    float desired_distance, adjusted_desired_distance;
    String lie_ball = "";
    String wind = "";
    String hill = "";
    TextView clubSuggestion;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_clubchoice, container, false);



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
        //call club suggestion stuff here

        shotTempList = new ArrayList<>();
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference("shots");
        // [END create_database_reference]

        //setting club names arrayList
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String[] arrayString = getResources().getStringArray(R.array.clubs);
        Set<String> sets = new HashSet<>(Arrays.asList(arrayString));
        Set<String> setNew = sharedPrefs.getStringSet("club_List",sets);
        for (String str : setNew)
            club_namesALTemp.add(str);

        //only sort once

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
            club_usageAL.add(zero);
            club_totalAL.add(zero);
            club_avgAL.add(zero);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        Query query1 =  FirebaseDatabase.getInstance().getReference("shots").orderByChild("userId").equalTo(uid);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String prevClub = "";
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
                adjust_distance();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Set the text to the suggested club
        clubSuggestion = (TextView) getView().findViewById(R.id.clubSuggestion);

        club = getResources().getStringArray(R.array.clubs);

        for(int i =0; i<club.length; i++){
            String[] str = club[i].split(" ");
            club[i] = str[1];
        }

        adapter = new clubChoice.SpinnerAdapter(mContext);

        final Spinner spinner = (Spinner)getView().findViewById(R.id.clubSpinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                currentClub = club[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        submitShotDetails = (FloatingActionButton)getView().findViewById(R.id.submitShotDetails);
        submitShotDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDetails(currentClub);
            }
        });
    }

    private void setDetails(String currentClub){

        ((MainActivity)getActivity()).setCurrentClub1(currentClub);

        Fragment fragment = getFragmentManager().findFragmentByTag("mapFragment");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment, "mapFragment")
                .addToBackStack(null)
                .commit();

    }

    public class SpinnerAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;

        public SpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return club.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ListContent holder;
            View v = convertView;
            if (v == null) {
                mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.row_textview, null);
                holder = new ListContent();
                holder.text = (TextView) v.findViewById(R.id.textView1);

                v.setTag(holder);
            } else {
                holder = (ListContent) v.getTag();
            }

            holder.text.setText(club[position]);

            return v;
        }
    }
    static class ListContent {
        TextView text;
    }

    public void adjust_distance(){
        //get desired distance
        desired_distance = ((MainActivity)getActivity()).getDesired_distance();

        //get lie of ball
        lie_ball = ((MainActivity)getActivity()).getCurrentLie();
        wind = ((MainActivity)getActivity()).getCurrentWind();
        hill = ((MainActivity)getActivity()).getCurrentHill();
       // Log.d("LieBall",lie_ball);
        //Log.d("Retrievals"," Lie: " + lie_ball + " Wind: " + wind + " Hill: " + hill);
        int adjustment = 0;

        //switch statement to apply based on lie of ball
        //Log.d("AdjustDistance",String.valueOf(desired_distance));
        switch(lie_ball){
            case "Rough":
                adjustment = adjustment - 1;
                Log.d("AdjustDistance","Should change here" + String.valueOf(adjusted_desired_distance));
                break;
            case "Sand":
                clubSuggestion.setText("Maybe chip out");
                break;
            case "Green":
                //set club suggestion to putter
                clubSuggestion.setText("Putter");
                break;
            default:
                //no adjustment
                break;
        }
        switch (hill){
            case "Uphill":
                adjustment = adjustment - 1;
                break;
            case "Downhill":
                adjustment = adjustment + 1;
                break;
            default:
                //no adjustment
                break;
        }
        switch (wind){
            case "Against Wind":
                adjustment = adjustment - 1;
                break;
            case "With Wind":
                adjustment = adjustment + 1;
                break;
            default:
                //no adjustment
                break;
        }

        //log
        calculate_suggestion(desired_distance,adjustment);
        Log.d("adjustment",String.valueOf(adjustment));

    }

    public void calculate_suggestion(float calculated_distance, int adjustment){
        //find the closest distance in the array to the adjusted desired distance
        float closest_distance =  Math.abs(club_avgAL.get(0) - calculated_distance);
        int index =0;

        for(int i=0; i<club_avgAL.size(); i++){
            Log.d("CHECK_AVG", club_namesAL.get(i) + ": "+ club_avgAL.get(i));
        }

        for(int i=1; i<club_avgAL.size(); i++){

            //get the closest and store this
            float tempdist = Math.abs(club_avgAL.get(i) - calculated_distance);
            if(tempdist < closest_distance){
                index = i;
                closest_distance = tempdist;
            }
        }

        index = index + adjustment;
        if(index <= 0){
            clubSuggestion.setText(club_namesAL.get(0));
        }
        else if(index >= club_namesAL.size()){
            //setting it to the lowest possible club
            clubSuggestion.setText(club_namesAL.get((club_namesAL.size())-1));
        }
        else{

            clubSuggestion.setText(club_namesAL.get(index));
        }

    }

}



