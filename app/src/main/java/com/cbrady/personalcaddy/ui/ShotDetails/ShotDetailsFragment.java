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

import androidx.fragment.app.Fragment;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
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
    String[] club;
    ShotDetailsFragment.SpinnerAdapter adapter;
    private RadioGroup radioGroup;
    private MaterialRadioButton radio_fairway,radio_rough,radio_green,radio_teebox,radio_sand;
    private Button btnDisplay;
    String currentLieBall,currentClub;

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

        radioGroup=(RadioGroup)getView().findViewById(R.id.radioGroup);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_fairway){
                   currentLieBall = getResources().getString(R.string.fairway);
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


        club = getResources().getStringArray(R.array.clubs);
        adapter = new ShotDetailsFragment.SpinnerAdapter(mContext);

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
                setDetails(currentLieBall, currentClub);
            }
        });
    }

    private void setDetails(String currentLieBall, String currentClub){
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
        ((MainActivity)getActivity()).setCurrentClub1(currentClub);
        ((MainActivity)getActivity()).setCurrentLie1(currentLieBall);

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

}
