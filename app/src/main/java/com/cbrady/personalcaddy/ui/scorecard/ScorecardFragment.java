package com.cbrady.personalcaddy.ui.scorecard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.homeRoundDetails;
import com.cbrady.personalcaddy.scorecardDetails;
import com.cbrady.personalcaddy.scorecardDetailsAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ScorecardFragment extends Fragment {

    Context mContext;
    String[] holeScores = new String[18];
    String[] holePars = new String[18];

    SimpleAdapter adapter;

    private scorecardDetailsAdapter scorecarddetailsadapter;
    ArrayList<scorecardDetails> scorecardDetailsArrayList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_scorecard_new, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        //getting details for scorecard
        holeScores = ((MainActivity)getActivity()).scorecardScores.toArray(holeScores);
        holePars = ((MainActivity)getActivity()).scorecardPars.toArray(holePars);

        for(int i=0; i<holeScores.length; i++){
            scorecardDetailsArrayList.add(new scorecardDetails(String.valueOf(i + 1), holePars[i], holeScores[i]));
        }

        scorecarddetailsadapter = new scorecardDetailsAdapter(getActivity(),scorecardDetailsArrayList);



        ListView listView = (ListView) root.findViewById(R.id.scorecardList);
        listView.setEmptyView(listView);

        listView.setAdapter(scorecarddetailsadapter);

        return root;
    }


}

