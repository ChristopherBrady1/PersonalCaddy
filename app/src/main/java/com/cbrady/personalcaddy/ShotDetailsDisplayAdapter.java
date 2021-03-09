package com.cbrady.personalcaddy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShotDetailsDisplayAdapter extends ArrayAdapter<ShotDetailsDisplay> {
    private static final String LOG_TAG = ShotDetailsDisplayAdapter.class.getSimpleName();

    public ShotDetailsDisplayAdapter(Activity context, ArrayList<ShotDetailsDisplay> shotDetailsDisplay){
        super(context, 0 , shotDetailsDisplay);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShotDetailsDisplay shotDetailsD = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shot_details_row, parent, false);
        }

        TextView shotNum = (TextView)convertView.findViewById(R.id.shotNumLabel);
        shotNum.setText(shotDetailsD.shotNum);

        TextView des_dist = (TextView) convertView.findViewById(R.id.desired_dist);
        des_dist.setText(shotDetailsD.desiredDistance);

        TextView act_dist = (TextView) convertView.findViewById(R.id.actual_dist);
        act_dist.setText(shotDetailsD.actualDistance);

        TextView club = (TextView) convertView.findViewById(R.id.clubUsed);
        club.setText(shotDetailsD.club);

        TextView lieBall = (TextView) convertView.findViewById(R.id.lieBall);
        lieBall.setText(shotDetailsD.lieBall);


        return convertView;
    }
}
