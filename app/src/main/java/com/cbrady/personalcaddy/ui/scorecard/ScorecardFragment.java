package com.cbrady.personalcaddy.ui.scorecard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;

import com.cbrady.personalcaddy.MainActivity;
import com.cbrady.personalcaddy.R;

public class ScorecardFragment extends ListFragment {

    Context mContext;
    String[] holeNum= {"01","02","03","04","05","06","07","08","09","10","11","12", "13", "14", "15", "16", "17", "18"};
    String[] holeScores = new String[((MainActivity)getActivity()).scorecard.size()];

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_scorecard, container, false);

        setListAdapter(new CustomAdapter(mContext, R.id.hole_number, holeNum));


        holeScores = ((MainActivity)getActivity()).scorecard.toArray(holeScores);

        setListAdapter(new CustomAdapter(mContext, R.id.hole_score, holeScores));

        return rootView;
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

