package com.cbrady.personalcaddy.ui.scorecard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ScorecardFragment extends ListFragment {

    Context mContext;
    String[] holeNum= {"Hole 1","Hole 2","Hole 3","Hole 4","Hole 5","Hole 6","Hole 7","Hole 8","Hole 9","Hole 10","Hole 11","Hole 12", "Hole 13", "Hole 14", "Hole 15", "Hole 16", "Hole 17", "Hole 18"};
    String[] holeScores = new String[18];
    //String[] holeScoreSample = {"1","2","3","4","5","6","7","8","9","10","11","12", "13", "14", "15", "16", "17", "18"};

    ArrayList<HashMap<String, String>> data=new ArrayList<HashMap<String,String>>();
    SimpleAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View rootView = inflater.inflate(R.layout.fragment_scorecard, container, false);

        holeScores = ((MainActivity)getActivity()).scorecard.toArray(holeScores);

        HashMap<String, String> map=new HashMap<String, String>();
        for(int i=0; i<holeNum.length; i++){
            map = new HashMap<String, String>();
            map.put("HoleNum", holeNum[i]);
            map.put("HoleScore", holeScores[i]);

            data.add(map);
        }

        //KEYS IN MAP
        String[] from={"HoleNum","HoleScore"};

        //IDS OF VIEWS
        int[] to={R.id.hole_number,R.id.hole_score};

        //ADAPTER
        adapter=new SimpleAdapter(getActivity(), data, R.layout.list_row, from, to);
        setListAdapter(adapter);


        //setListAdapter(new CustomAdapter(mContext, R.id.hole_number, holeNum));

        //setListAdapter(new CustomAdapter(mContext, R.id.hole_score, holeScores));

        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                // TODO Auto-generated method stub

                Toast.makeText(getActivity(), data.get(pos).get("Player"), Toast.LENGTH_SHORT).show();

            }
        });
    }

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
    }

}

