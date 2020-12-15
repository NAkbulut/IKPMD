package com.example.ikpmd_periode2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

public class PokeDetails extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_poke_details, container, false);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Favo_Button();
    }


    public void Favo_Button(){
        MaterialFavoriteButton favorite = (MaterialFavoriteButton) getView().findViewById(R.id.Favorite_Button);

        favorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if(favorite){
                            Snackbar.make(buttonView, "Added to favorites", Snackbar.LENGTH_SHORT).show();

                        }else{
                            Snackbar.make(buttonView, "Removed from favorites", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}