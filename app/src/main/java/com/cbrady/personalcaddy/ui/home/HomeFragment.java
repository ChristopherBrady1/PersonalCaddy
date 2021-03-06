package com.cbrady.personalcaddy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.TestDataEntryFragment;
import com.cbrady.personalcaddy.homeRoundDetails;
import com.cbrady.personalcaddy.homeRoundDetailsAdapter;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.ui.AR.ArFragment;
import com.cbrady.personalcaddy.ui.createround.CreateRoundFragment;
import com.cbrady.personalcaddy.ui.holedetails.HoleDetailsFragment;
import com.cbrady.personalcaddy.ui.map.MapFragment;
import com.cbrady.personalcaddy.ui.roundListDetails.RoundListDetailsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    FloatingActionButton createRound;
    Context mContext;

    SimpleAdapter adapter;
    private homeRoundDetailsAdapter homeRoundDetailsAdapter;
    private List<Round> roundTempList1;
    ListView listView;

    int x = 0;

    //com.cbrady.personalcaddy.homeRoundDetails[] homeRoundDetails;
    /*com.cbrady.personalcaddy.homeRoundDetails[] homeRoundDetails = {
            new homeRoundDetails("72", "Craddockstown", "15/10/1998"),
            new homeRoundDetails("68", "Naas", "15/10/1998"),
            new homeRoundDetails("89", "Craddockstown", "15/10/1998"),
            new homeRoundDetails("102", "Naas", "15/10/1998"),
            new homeRoundDetails("75", "Newbridge", "15/10/1998"),
            new homeRoundDetails("75", "Craddockstown", "15/10/1998"),
            new homeRoundDetails("62", "Naas", "15/10/1998"),
            new homeRoundDetails("68", "Craddockstown", "15/10/1998"),
            new homeRoundDetails("98", "Newbridge", "15/10/1998"),
            new homeRoundDetails("105", "Craddockstown", "15/10/1998")
    };*/

    ArrayList<homeRoundDetails> homeRoundDetailsArrayList = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        //View bnv = getActivity().findViewById(R.id.nav_view);
        //bnv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        //((AppCompatActivity) getActivity()).getSupportActionBar().show();
        //View bnv = getActivity().findViewById(R.id.nav_view);
        //bnv.setVisibility(View.VISIBLE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        createRound = root.findViewById(R.id.createRound);
        roundTempList1 = new ArrayList<>();

        Button buttonARNOW = root.findViewById(R.id.buttonARNOW);
        Button testMap = root.findViewById(R.id.testMap);
        Button dataEntry = root.findViewById(R.id.dataEntry);
        //((AppCompatActivity)getActivity()).getSupportActionBar().show();

        dataEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment testDataEntry = new TestDataEntryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, testDataEntry, "mapFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        testMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment map = new MapFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, map, "mapFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        buttonARNOW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment arFragment = new ArFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, arFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        createRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment createRoundFragment = new CreateRoundFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, createRoundFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();


        Query query1 = FirebaseDatabase.getInstance().getReference("rounds").orderByChild("uid").equalTo(uid);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot rounds : snapshot.getChildren()) {
                        Round round = rounds.getValue(Round.class);
                        roundTempList1.add(round);

                        int score = round.getScore();
                        String courseName = round.getGolfCourseName();
                        String date = round.getCurrentDate();
                        String delim = "[ ]+";
                        String[] dateOwn = date.split(delim);
                        String roundID = rounds.getKey();

                        homeRoundDetailsArrayList.add(new homeRoundDetails(String.valueOf(score), courseName, dateOwn[0], roundID));
                    }
                }

                //sorting the rounds by date
                Collections.sort(homeRoundDetailsArrayList);

                homeRoundDetailsAdapter = new homeRoundDetailsAdapter(getActivity(), homeRoundDetailsArrayList);

                listView = (ListView) root.findViewById(R.id.homeList);

                listView.setAdapter(homeRoundDetailsAdapter);
                x = 0;

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //TextView hiddenID = view.findViewById(R.id.hiddenID);
                        View hiddenID = view.findViewById(R.id.layout_home_row);
                        ((MainActivity) getActivity()).setHiddenKey(hiddenID.getContentDescription().toString());

                        Fragment roundListDetailsFragment = new RoundListDetailsFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, roundListDetailsFragment, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return root;
    }
}