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
    private RadioGroup radioGroupLie, radioGroupWind, radioGroupHill;
    private MaterialRadioButton radio_fairway,radio_rough,radio_green,radio_teebox,radio_sand;
    private Button btnDisplay;
    String currentLieBall, currentWind, currentHill;
    RadioButton lastRadioBtnLie, lastRadioBtnWind, lastRadioBtnHill ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_shot_details, container, false);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        radioGroupLie=(RadioGroup)getView().findViewById(R.id.radioGroupLie);
        radioGroupWind=(RadioGroup)getView().findViewById(R.id.radioGroupWind);
        radioGroupHill=(RadioGroup)getView().findViewById(R.id.radioGroupHill);

        lastRadioBtnLie = (RadioButton) getView().findViewById(R.id.radio_teebox);
        lastRadioBtnHill = (RadioButton) getView().findViewById(R.id.radio_flat);
        lastRadioBtnWind = (RadioButton) getView().findViewById(R.id.radio_no_wind);

        int currentShot = ((MainActivity)getActivity()).getCurrentShot();


        radioGroupLie.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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



        radioGroupHill.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_downhill){
                    currentHill = getResources().getString(R.string.downhill);
                }
                if(checkedId == R.id.radio_uphill){
                    currentHill = getResources().getString(R.string.uphill);
                }
                if(checkedId == R.id.radio_flat){
                    currentHill = getResources().getString(R.string.flat);
                }
            }
        });

        radioGroupWind.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_with_wind){
                    currentWind = getResources().getString(R.string.with_wind);
                }
                if(checkedId == R.id.radio_against_wind){
                    currentWind = getResources().getString(R.string.against_wind);
                }
                if(checkedId == R.id.radio_no_wind){
                    currentWind = getResources().getString(R.string.no_wind);
                }
            }
        });


        submitShotDetails = (FloatingActionButton)getView().findViewById(R.id.submitShotDetails);
        submitShotDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDetails(currentLieBall, currentHill, currentWind);
            }
        });
    }

    private void setDetails(String currentLieBall, String currentHill, String currentWind){

        ((MainActivity)getActivity()).setCurrentLie(currentLieBall);
        ((MainActivity)getActivity()).setCurrentHill(currentHill);
        ((MainActivity)getActivity()).setCurrentWind(currentWind);

        if (radioGroupLie.getCheckedRadioButtonId() == -1)
        {
            lastRadioBtnLie.setError("Select Lie");
            return;
        }
        if (radioGroupHill.getCheckedRadioButtonId() == -1)
        {
            lastRadioBtnHill.setError("Select Gradient");
            return;
        }
        if (radioGroupWind.getCheckedRadioButtonId() == -1)
        {
            lastRadioBtnWind.setError("Select Wind");
            return;
        }

        //calling club choice
        Fragment clubChoice = new clubChoice();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, clubChoice, "clubChoice")
                .addToBackStack(null)
                .commit();



    }



}
