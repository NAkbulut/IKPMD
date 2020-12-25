package com.example.ikpmd_periode2.grids;

import android.graphics.drawable.Drawable;

public class MainGridData {
    String PokemonName;
    Drawable PokePicture;

    public String getPokemonName() {
        return PokemonName;
    }

    public void setPokemonName(String pokemonName) {
        PokemonName = pokemonName;
    }

    public Drawable getPokePicture() {
        return PokePicture;
    }

    public void setPokePicture(Drawable pokePicture) {
        PokePicture = pokePicture;
    }
}
