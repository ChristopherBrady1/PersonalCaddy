package com.cbrady.personalcaddy.ui.settings;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import com.cbrady.personalcaddy.R;

public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //addPreferencesFromResource(R.xml.preferences);
    }
}