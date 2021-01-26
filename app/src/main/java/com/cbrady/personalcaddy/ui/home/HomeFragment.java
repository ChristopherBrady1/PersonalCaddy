package com.cbrady.personalcaddy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cbrady.personalcaddy.R;
import com.cbrady.personalcaddy.ui.createround.CreateRoundFragment;
import com.cbrady.personalcaddy.ui.map.MapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    FloatingActionButton createRound;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        createRound = root.findViewById(R.id.createRound);

        createRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment createRoundFragment = new CreateRoundFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, createRoundFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }
}