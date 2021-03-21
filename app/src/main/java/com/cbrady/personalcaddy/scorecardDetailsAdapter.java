package com.cbrady.personalcaddy;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class scorecardDetailsAdapter extends ArrayAdapter<scorecardDetails> {
    private static final String LOG_TAG = ShotDetailsDisplayAdapter.class.getSimpleName();

    public scorecardDetailsAdapter(Activity context, ArrayList<scorecardDetails> scorecardDetails){
        super(context, 0 , scorecardDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        scorecardDetails scorecardetails = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        TextView holeNum = (TextView)convertView.findViewById(R.id.hole_number);
        holeNum.setText(scorecardetails.holeNum);

        TextView parHole = (TextView) convertView.findViewById(R.id.hole_par);
        parHole.setText(scorecardetails.parHole);

        TextView holeScore = (TextView) convertView.findViewById(R.id.hole_score);
        holeScore.setText(scorecardetails.score);




        return convertView;
    }
}