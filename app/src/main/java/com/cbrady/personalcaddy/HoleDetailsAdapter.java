package com.cbrady.personalcaddy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HoleDetailsAdapter extends ArrayAdapter<HoleDetails> {
    private static final String LOG_TAG = HoleDetailsAdapter.class.getSimpleName();


    public HoleDetailsAdapter(Activity context, ArrayList<HoleDetails> holeDetails) {

        super(context, 0, holeDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoleDetails holeDetails = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.round_details_row, parent, false);
        }

        TextView holeNumLabel = (TextView)convertView.findViewById(R.id.holeNumLabel);
        holeNumLabel.setText(holeDetails.numHole);

        TextView parLabel = (TextView) convertView.findViewById(R.id.parActual);
        parLabel.setText(holeDetails.parHole);

        TextView distLabel = (TextView) convertView.findViewById(R.id.distActual);
        distLabel.setText(holeDetails.distHole);

        TextView scoreLabel = (TextView) convertView.findViewById(R.id.scoreActual);
        scoreLabel.setText(holeDetails.scoreHole);

        View hole_layout = (View) convertView.findViewById(R.id.layout_hole_row);
        hole_layout.setContentDescription(holeDetails.hiddenHoleID);


        return convertView;
    }
}