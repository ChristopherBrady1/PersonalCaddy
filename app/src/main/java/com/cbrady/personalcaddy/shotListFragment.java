package com.cbrady.personalcaddy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cbrady.personalcaddy.models.Holes;
import com.cbrady.personalcaddy.models.Shots;
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

public class shotListFragment extends Fragment {
    public shotListFragment() {}

    Context mContext;
    private DatabaseReference mDatabase;

    private com.cbrady.personalcaddy.ShotDetailsDisplayAdapter shotDetailsDisplayAdapter;
    ArrayList<ShotDetailsDisplay> shotDetailsDisplayArrayList = new ArrayList<>();
    private List<Holes> holeTempList;
    private List<Shots> shotTempList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_shot_list, container, false);

        String hiddenHoleKey =  ((MainActivity)getActivity()).getHiddenHoleKey();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        //setting text views
        TextView holeNumTitle = (TextView)rootView.findViewById(R.id.holeNumHeading);


        Query query1 =  FirebaseDatabase.getInstance().getReference("holes").orderByKey().equalTo(hiddenHoleKey);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot holes : snapshot.getChildren()) {
                        Holes hole = holes.getValue(Holes.class);
                        //roundTempList1.add(round);

                        String holeNum = hole.getHoleNum();
                        holeNumTitle.append(holeNum);


                        String parHole= hole.getPar();


                    }
                }

                Query query2 =  FirebaseDatabase.getInstance().getReference("shots").orderByChild("holeid").equalTo(hiddenHoleKey);

                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            for (DataSnapshot shots : snapshot.getChildren()) {
                                Shots shot = shots.getValue(Shots.class);

                                String desiredDistance = shot.getDesiredDistance();
                                String actualDistance = shot.getActualDistance();
                                String club = shot.getClub();
                                String shotNum = shot.getShotNum();
                                String lieBall = shot.getLieBall();


                                shotDetailsDisplayArrayList.add(new ShotDetailsDisplay(desiredDistance, actualDistance, club,  shotNum, lieBall));


                            }
                        }

                        shotDetailsDisplayAdapter = new ShotDetailsDisplayAdapter(getActivity(),shotDetailsDisplayArrayList);



                        ListView listView = (ListView) rootView.findViewById(R.id.shotList);
                        listView.setEmptyView(listView);
                        listView.setAdapter(shotDetailsDisplayAdapter);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]


    }
}
