package com.cbrady.personalcaddy.ui.roundListDetails;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cbrady.personalcaddy.HoleDetails;
import com.cbrady.personalcaddy.HoleDetailsAdapter;
import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.models.Holes;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.shotListFragment;
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

public class RoundListDetailsFragment extends Fragment {
    public RoundListDetailsFragment() {}

    Context mContext;
    private DatabaseReference mDatabase;

    private com.cbrady.personalcaddy.HoleDetailsAdapter holeDetailsAdapter;
    ArrayList<HoleDetails> holeDetailsArrayList = new ArrayList<>();
    private List<Holes> holeTempList;
    private List<Round> roundTempList1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_round_list_details, container, false);

        //TextView roundID = rootView.findViewById(R.id.roundIDclicked);
        String hiddenKey =  ((MainActivity)getActivity()).getHiddenKey();
        //roundID.setText(hiddenKey);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        //setting text views
        TextView scoreShotLabel = (TextView)rootView.findViewById(R.id.scoreShot);
        TextView courseNameLabel = (TextView)rootView.findViewById(R.id.courseNameHeading);
        TextView dateCoursePlayedLabel = (TextView)rootView.findViewById(R.id.dateCoursePlayed);
        TextView parCourseLabel = (TextView)rootView.findViewById(R.id.parHoleLabel);
        TextView totalPuttsLabel = (TextView)rootView.findViewById(R.id.totalPutts);
        TextView girLabel = (TextView)rootView.findViewById(R.id.GIR);
        TextView firLabel = (TextView)rootView.findViewById(R.id.FIR);


        Query query1 =  FirebaseDatabase.getInstance().getReference("rounds").orderByKey().equalTo(hiddenKey);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot rounds : snapshot.getChildren()) {
                        Round round = rounds.getValue(Round.class);
                        //roundTempList1.add(round);

                        int score = round.getScore();
                        scoreShotLabel.append(String.valueOf(score));

                        String courseName = round.getGolfCourseName();
                        courseNameLabel.append(courseName);

                        String date = round.getCurrentDate();
                        String delim = "[ ]+";
                        String[] dateOwn = date.split(delim);
                        dateCoursePlayedLabel.append(dateOwn[0]);

                        String parRound = round.getParCourse();
                        parCourseLabel.append(parRound);

                        int totalPutts = round.getTotalPutts();
                        totalPuttsLabel.append(String.valueOf(totalPutts));

                        int gir = round.getTotalGIR();
                        girLabel.append(String.valueOf(gir));

                        int fir = round.getTotalFIR();
                        firLabel.append(String.valueOf(fir));




                    }
                }

                Query query2 =  FirebaseDatabase.getInstance().getReference("holes").orderByChild("roundid").equalTo(hiddenKey);

                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            for (DataSnapshot holes : snapshot.getChildren()) {
                                Holes hole = holes.getValue(Holes.class);

                                String holeNum = hole.getHoleNum();
                                String distHole = hole.getDistance();
                                String parHole = hole.getPar();
                                String scoreHole = hole.getScore();
                                String holeID = holes.getKey();

                                if(holeNum != null) {
                                    holeDetailsArrayList.add(new HoleDetails(parHole, scoreHole, holeNum,  distHole, holeID));
                                }

                            }
                        }

                        holeDetailsAdapter = new HoleDetailsAdapter(getActivity(),holeDetailsArrayList);

                        ListView listView = (ListView) rootView.findViewById(R.id.holeList);
                        listView.setAdapter(holeDetailsAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //TextView hiddenID = view.findViewById(R.id.hiddenID);
                                View hiddenID = view.findViewById(R.id.layout_hole_row);
                                ((MainActivity)getActivity()).setHiddenHoleKey(hiddenID.getContentDescription().toString());

                                Fragment shotListFrag = new shotListFragment();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.nav_host_fragment, shotListFrag, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });


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
