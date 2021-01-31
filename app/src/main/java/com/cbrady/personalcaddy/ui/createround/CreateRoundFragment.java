package com.cbrady.personalcaddy.ui.createround;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.models.User;
import com.cbrady.personalcaddy.ui.holedetails.HoleDetailsFragment;
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

public class CreateRoundFragment extends Fragment {
    public CreateRoundFragment() {}

    Context mContext;
    FloatingActionButton submitRound;
    EditText txtgolfCourse, txtPar;
    private static final String TAG = "NewRound";
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
        View rootView = inflater.inflate(R.layout.fragment_create_round, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        submitRound = (FloatingActionButton)getView().findViewById(R.id.submitRound);
        submitRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subRound();
            }
        });
    }

    private void subRound() {
        txtgolfCourse = (EditText)getView().findViewById(R.id.golfCourse);
        txtPar = (EditText)getView().findViewById(R.id.par);
        final String golfCourseName = txtgolfCourse.getText().toString();
        final String parCourse = txtPar.getText().toString();

        //date parameter
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm:ss", Locale.getDefault());
        String currentDate = df.format(c);

        // Title is required
        if (TextUtils.isEmpty(golfCourseName)) {
            txtgolfCourse.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(parCourse)) {
            txtPar.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-rounds
        setEditingEnabled(false);
        Toast.makeText(mContext, "Creating Round...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(mContext,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new round
                            writeNewRound(userId, user.username, golfCourseName, parCourse, currentDate);
                        }

                        // Finish this Activity, back to the stream
                        Fragment holeDetailsFragment = new HoleDetailsFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, holeDetailsFragment, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        txtgolfCourse.setEnabled(enabled);
        txtPar.setEnabled(enabled);
        if (enabled) {
            submitRound.setVisibility(View.VISIBLE);
        } else {
            submitRound.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewRound(String userId, String username, String golfCourseName, String parCourse, String currentDate) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        key = mDatabase.child("rounds").push().getKey();
        Round round = new Round(userId, username, golfCourseName, parCourse, currentDate);
        Map<String, Object> roundValues = round.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/rounds/" + userId + "/" + key, roundValues);

        mDatabase.updateChildren(childUpdates);

        //saving key as global variable to know it is the current round.
        //setting this
        //TODO SET THIS
        ((MainActivity)getActivity()).setCurrentRoundKey(key);

        String currentKeyID = ((MainActivity)getActivity()).getCurrentRoundKey();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference ref = database.getReference("rounds/" + userId + "/" + key);
        ref.orderByChild("golfCourseName");
        //System.out.println(ref.);

        // Attach a listener to read the data at our rounds reference
        /*ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Round round = dataSnapshot.getValue(Round.class);
                System.out.println(round.golfCourseName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        */
        Log.d("ID_now", "Value: " + key);

        //String currentkey = obj.getCurrentRound();
        Log.d("GET_KEY", "Value: " + currentKeyID);


    }
    // [END write_fan_out]
}
