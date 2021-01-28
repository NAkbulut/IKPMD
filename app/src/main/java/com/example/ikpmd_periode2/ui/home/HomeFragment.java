package com.example.ikpmd_periode2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ikpmd_periode2.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    public HomeFragment(){
        //yeet
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pokedex, container, false);


        return root;
    }

}



