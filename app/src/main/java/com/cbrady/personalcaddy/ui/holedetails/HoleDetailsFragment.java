package com.cbrady.personalcaddy.ui.holedetails;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.models.User;
import com.cbrady.personalcaddy.ui.map.MapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HoleDetailsFragment extends Fragment{
    public HoleDetailsFragment() {}

    Context mContext;
    FloatingActionButton submitHoleDetails;
    EditText txtparHole, txtdistanceHole;
    private static final String TAG = "NewHole";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    String key;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_hole_details, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        submitHoleDetails = (FloatingActionButton)getView().findViewById(R.id.submitHoleDetails);
        submitHoleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subHoleDetails();
            }
        });
    }

    private void subHoleDetails() {
        txtparHole = (EditText)getView().findViewById(R.id.parHole);
        txtdistanceHole = (EditText)getView().findViewById(R.id.distanceHole);
        final String distanceHole = txtdistanceHole.getText().toString();
        final String parHole = txtparHole.getText().toString();

        ((MainActivity)getActivity()).setHolePar(parHole);
        ((MainActivity)getActivity()).setHoleDistance(distanceHole);

        int counter = ((MainActivity)getActivity()).getCounter();

        if(counter == 2){
            getActivity().onBackPressed();
        }
        else {
            Fragment mapFragment = new MapFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, mapFragment, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }


}
