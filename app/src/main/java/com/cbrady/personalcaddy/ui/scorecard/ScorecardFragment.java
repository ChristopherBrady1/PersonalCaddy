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
    String[] holeNum= {"Hole 1","Hole 2","Hole 3","Hole 4","Hole 5","Hole 6","Hole 7","Hole 8","Hole 9","Hole 10","Hole 11","Hole 12", "Hole 13", "Hole 14", "Hole 15", "Hole 16", "Hole 17", "Hole 18"};
    String[] holeScores = new String[18];
    String[] holeNums = new String[18];
    String[] holePars = new String[18];

    //ArrayList<HashMap<String, String>> data=new ArrayList<HashMap<String,String>>();
    SimpleAdapter adapter;

    private scorecardDetailsAdapter scorecarddetailsadapter;
    ArrayList<scorecardDetails> scorecardDetailsArrayList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_scorecard_new, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        //getting details for scorecard
        holeScores = ((MainActivity)getActivity()).scorecardScores.toArray(holeScores);
        holePars = ((MainActivity)getActivity()).scorecardPars.toArray(holePars);

        for(int i=0; i<holeScores.length; i++){
            scorecardDetailsArrayList.add(new scorecardDetails(String.valueOf(i + 1), holePars[i], holeScores[i]));
        }

        scorecarddetailsadapter = new scorecardDetailsAdapter(getActivity(),scorecardDetailsArrayList);

        /*HashMap<String, String> map=new HashMap<String, String>();
        for(int i=0; i<holeNum.length; i++){
            map = new HashMap<String, String>();
            map.put("HoleNum", holeNum[i]);
            map.put("HoleScore", holeScores[i]);

            data.add(map);
        }
        //KEYS IN MAP
        String[] from={"HoleNum","HoleScore"};

        //IDS OF VIEWS
        int[] to={R.id.hole_number,R.id.hole_score};*/

        ListView listView = (ListView) root.findViewById(R.id.scorecardList);
        listView.setEmptyView(listView);

        //ADAPTER
        //adapter=new CustomAdapter(mContext, R.layout.list_row, data);
        listView.setAdapter(scorecarddetailsadapter);

        return root;
    }

    /*
    public class CustomAdapter extends ArrayAdapter<String> {
        public CustomAdapter(Context context, int rowLayoutId, String[] myArrayData)
        {
            super(context, rowLayoutId, myArrayData);
        }


        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = getLayoutInflater();
            View row=inflater.inflate(R.layout.list_row, parent, false);


            TextView label = (TextView)row.findViewById(R.id.hole_number);
            label.setText(holeNum[position]);

            TextView label2 = (TextView) row.findViewById(R.id.hole_score);
            label2.setText(holeScores[position]);


            return row;
        }
    }*/

}

