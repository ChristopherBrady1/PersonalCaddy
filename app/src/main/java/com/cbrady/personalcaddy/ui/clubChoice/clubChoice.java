package com.cbrady.personalcaddy.ui.clubChoice;



import android.content.Context;
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

import androidx.fragment.app.Fragment;

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
import java.util.List;

public class clubChoice extends Fragment {
    public clubChoice() {}

    Context mContext;
    FloatingActionButton submitShotDetails;
    private DatabaseReference mDatabase;
    String[] club;
    clubChoice.SpinnerAdapter adapter;
    private Button btnDisplay;
    String currentClub;

    //array for club names
    String[] clubNames = {"Driver", "3Wood", "5Wood", "3-iron", "4-iron", "5-iron", "6-iron", "7-iron", "8-iron", "9-iron", "Pitching Wedge", "Sand Wedge"};
    float[] avgClub = new float[12];
    private List<Shots> shotTempList;

    //variables to store the average of each club
    float avgDriver, avg3Wood, avg5Wood, avg3iron, avg4iron, avg5iron, avg6iron, avg7iron, avg8iron, avg9iron, avgPW, avgSW = 0;
    float avgDriverTot, avg3WoodTot, avg5WoodTot, avg3ironTot, avg4ironTot, avg5ironTot, avg6ironTot, avg7ironTot, avg8ironTot, avg9ironTot, avgPWTot, avgSWTot = 0;
    float avgDriverNum, avg3WoodNum, avg5WoodNum, avg3ironNum, avg4ironNum, avg5ironNum, avg6ironNum, avg7ironNum, avg8ironNum, avg9ironNum, avgPWNum, avgSWNum = 0;
    String actualDistance = "";
    float desired_distance, adjusted_desired_distance;
    String lie_ball = "";
    TextView clubSuggestion;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_clubchoice, container, false);


        shotTempList = new ArrayList<>();
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference("shots");
        // [END create_database_reference]

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        Query query1 =  FirebaseDatabase.getInstance().getReference("shots").orderByChild("UserId").equalTo(uid);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String prevClub = "";
                    for (DataSnapshot shots : dataSnapshot.getChildren()) {
                        Shots shot = shots.getValue(Shots.class);
                        shotTempList.add(shot);

                        String club = shot.getClub();


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


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Set the text to the suggested club
        clubSuggestion = (TextView) rootView.findViewById(R.id.clubSuggestion);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
        //call club suggestion stuff here

        adjust_distance();

        club = getResources().getStringArray(R.array.clubs);
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

        getActivity().onBackPressed();

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
        lie_ball = ((MainActivity)getActivity()).getCurrentLie1();
        Log.d("LieBall",lie_ball);

        //switch statement to apply based on lie of ball
        Log.d("AdjustDistance",String.valueOf(desired_distance));
        switch(lie_ball){
            case "Rough":
                adjusted_desired_distance = desired_distance + 10;
                Log.d("AdjustDistance","Should change here" + String.valueOf(adjusted_desired_distance));
                calculate_suggestion(adjusted_desired_distance);
            case "Sand":
                adjusted_desired_distance = desired_distance + 20;
                calculate_suggestion(adjusted_desired_distance);
            case "Green":
                //set club suggestion to putter
            default:
                adjusted_desired_distance = desired_distance;
                calculate_suggestion(adjusted_desired_distance);
        }

        //log
        Log.d("AdjustDistance",String.valueOf(adjusted_desired_distance));


    }

    public void calculate_suggestion(float calculated_distance){
        //find the closest distance in the array to the adjusted desired distance
        float closest_distance =  Math.abs(avgClub[0] - calculated_distance);;
        int index =0;
        for(int i=1; i<avgClub.length; i++){

            //get the closest and store this
            float tempdist = Math.abs(avgClub[i] - calculated_distance);
            if(tempdist < closest_distance){
                index = i;
                closest_distance = tempdist;
            }
        }

        Log.d("CLUB_SUGGESTION", clubNames[index]);
        clubSuggestion.setText(clubNames[index]);
    }

}



