package com.cbrady.personalcaddy.ui.ShotDetails;

import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.ui.clubChoice.clubChoice;
import com.cbrady.personalcaddy.ui.map.MapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShotDetailsFragment extends Fragment {
    public ShotDetailsFragment() {}

    Context mContext;
    FloatingActionButton submitShotDetails;
    private DatabaseReference mDatabase;
    String lie_ball;
    private RadioGroup radioGroup;
    private MaterialRadioButton radio_fairway,radio_rough,radio_green,radio_teebox,radio_sand;
    private Button btnDisplay;
    String currentLieBall;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_shot_details, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        radioGroup=(RadioGroup)getView().findViewById(R.id.radioGroup);


        int currentShot = ((MainActivity)getActivity()).getCurrentShot();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_fairway){
                   currentLieBall = getResources().getString(R.string.fairway);
                   //incrementing the FIR variable if the second shot is on the fairway
                   int fir = ((MainActivity)getActivity()).getFIR();
                   fir++;
                   if(currentShot == 1){
                       ((MainActivity)getActivity()).setFIR(fir);
                   }
                }
                if(checkedId == R.id.radio_green){
                    currentLieBall = getResources().getString(R.string.green);
                }
                if(checkedId == R.id.radio_rough){
                    currentLieBall = getResources().getString(R.string.rough);
                }
                if(checkedId == R.id.radio_sand){
                    currentLieBall = getResources().getString(R.string.sand);
                }
                if(checkedId == R.id.radio_teebox){
                    currentLieBall = getResources().getString(R.string.tee_box);
                }
            }
        });


        submitShotDetails = (FloatingActionButton)getView().findViewById(R.id.submitShotDetails);
        submitShotDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDetails(currentLieBall);
            }
        });
    }

    private void setDetails(String currentLieBall){
        /*
        if(((MainActivity)getActivity()).getCurrentClub1() == null && ((MainActivity)getActivity()).getCurrentLie1() == null){
            ((MainActivity)getActivity()).setCurrentClub1(currentClub);
            ((MainActivity)getActivity()).setCurrentLie1(currentLieBall);
        }
        else{
            if(((MainActivity)getActivity()).getCurrentClub2() == null && ((MainActivity)getActivity()).getCurrentLie2() == null){
                ((MainActivity)getActivity()).setCurrentClub2(currentClub);
                ((MainActivity)getActivity()).setCurrentLie2(currentLieBall);
            }
            else{
                //set current club 2 to club 1
                ((MainActivity)getActivity()).setCurrentClub1(((MainActivity)getActivity()).getCurrentClub2());
                ((MainActivity)getActivity()).setCurrentLie1(((MainActivity)getActivity()).getCurrentLie2());

                ((MainActivity)getActivity()).setCurrentClub2(currentClub);
                ((MainActivity)getActivity()).setCurrentLie2(currentLieBall);

                //TODO push these at the point where distance is gotten
            }
        }*/
        //((MainActivity)getActivity()).setCurrentClub1(currentClub);
        ((MainActivity)getActivity()).setCurrentLie1(currentLieBall);

        //call clubChoice fragment



        getActivity().onBackPressed();

    }



}
