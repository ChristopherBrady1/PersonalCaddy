package com.cbrady.personalcaddy.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.cbrady.personalcaddy.R;

public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        addPreferencesFromResource(R.xml.preferences);


    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //addPreferencesFromResource(R.xml.preferences);
    }
}