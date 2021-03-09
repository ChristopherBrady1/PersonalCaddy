package com.cbrady.personalcaddy;


import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class homeRoundDetailsAdapter extends ArrayAdapter<homeRoundDetails> {
    private static final String LOG_TAG = homeRoundDetailsAdapter.class.getSimpleName();


    public homeRoundDetailsAdapter(Activity context, ArrayList<homeRoundDetails> homeRoundDetailsvar) {

        super(context, 0, homeRoundDetailsvar);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        homeRoundDetails homeRoundDetailsvar = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_list_row, parent, false);
        }

        TextView score_label = (TextView)convertView.findViewById(R.id.score);
        score_label.setText(homeRoundDetailsvar.score);

        TextView courseName_label = (TextView) convertView.findViewById(R.id.CourseName);
        courseName_label.setText(homeRoundDetailsvar.courseName);

        TextView date_label = (TextView) convertView.findViewById(R.id.datePlayed);
        date_label.setText(homeRoundDetailsvar.date);

        View home_layout = (View) convertView.findViewById(R.id.layout_home_row);
        home_layout.setContentDescription(homeRoundDetailsvar.hiddenID);

        //TextView hidden_label = (TextView) convertView.findViewById(R.id.hiddenID);
        //hidden_label.setText(homeRoundDetailsvar.hiddenID);

        return convertView;
    }
}