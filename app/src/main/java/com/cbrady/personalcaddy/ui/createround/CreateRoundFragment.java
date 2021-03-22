package com.cbrady.personalcaddy.ui.createround;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.models.Round;
import com.cbrady.personalcaddy.models.User;
import com.cbrady.personalcaddy.ui.clubChoice.clubChoice;
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
import java.util.ArrayList;
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
    ArrayList<String> pars = new ArrayList<>();
    CreateRoundFragment.SpinnerAdapter adapter;
    String currentPar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_create_round, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        //Practice
        //Log.d("PRACTICE", "Value: " + ((MainActivity)getActivity()).practice.get(0));


        for(int i =65; i<76; i++){
            pars.add(String.valueOf(i));
        }

        adapter = new CreateRoundFragment.SpinnerAdapter(mContext);

        final Spinner spinner = (Spinner)getView().findViewById(R.id.parSpinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(5);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                currentPar = pars.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        submitRound = (FloatingActionButton)getView().findViewById(R.id.submitRound);
        submitRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subRound(currentPar);
            }
        });
    }

    private void subRound(String parCourse) {
        txtgolfCourse = (EditText)getView().findViewById(R.id.golfCourse);
        final String golfCourseName = txtgolfCourse.getText().toString();

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
        int score = 0;
        Round round = new Round(userId, username, golfCourseName, parCourse, currentDate, score, 0, 0,0,0);
        Map<String, Object> roundValues = round.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/rounds/" + key, roundValues);

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

    public class SpinnerAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;

        public SpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return pars.size();
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
            final CreateRoundFragment.ListContent holder;
            View v = convertView;
            if (v == null) {
                mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.row_textview, null);
                holder = new CreateRoundFragment.ListContent();
                holder.text = (TextView) v.findViewById(R.id.textView1);

                v.setTag(holder);
            } else {
                holder = (CreateRoundFragment.ListContent) v.getTag();
            }

            holder.text.setText(pars.get(position));

            return v;
        }
    }
    static class ListContent {
        TextView text;
    }
    // [END write_fan_out]
}
