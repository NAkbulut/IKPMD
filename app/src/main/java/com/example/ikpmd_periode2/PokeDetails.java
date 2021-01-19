package com.example.ikpmd_periode2;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PokeDetails extends Fragment {


    static String PokeStats = "";
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_poke_details, container, false);

        setPokeDetailsFillables(root);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Favo_Button();

    }

    public void setPokeDetailsFillables(View root){
        //find the textview involved and split stats into list, and fin set all text
        TextView pokedetails_name = (TextView) root.findViewById(R.id.textName);
        String[] splittedString = PokeStats.split(", ");
        pokedetails_name.setText(splittedString[0]);

        //Set types
        TextView pokedetails_type1 = (TextView) root.findViewById(R.id.textType);
        pokedetails_type1.setText(splittedString[1]);
        String type1 = (String) pokedetails_type1.getText();

        //Set colors
        if(type1.equals("rock")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.rock)));
        }else if(type1.equals("ground")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.egg_yellow)));
        }else if(type1.equals("normal")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.normal)));
        }else if(type1.equals("fighting")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fighting)));
        }else if(type1.equals("flying")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.flying)));
        }else if(type1.equals("poison")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.poison)));
        }else if(type1.equals("bug")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bug)));
        }else if(type1.equals("ghost")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ghost)));
        }else if(type1.equals("steel")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.steel)));
        }else if(type1.equals("???")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.special)));
        }else if(type1.equals("fire")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fire)));
        }else if(type1.equals("water")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.water)));
        }else if(type1.equals("grass")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grass)));
        }else if(type1.equals("electric")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.electric)));
        }else if(type1.equals("psychic")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.psychic)));
        }else if(type1.equals("ice")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ice)));
        }else if(type1.equals("dragon")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dragon)));
        }else if(type1.equals("dark")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark)));
        }else if(type1.equals("fairy")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fairy)));
        }

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