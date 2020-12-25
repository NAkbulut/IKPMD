package com.example.ikpmd_periode2.grids;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ikpmd_periode2.R;

public class MainGridViewHolder extends LinearLayout {
    Context mContext;
    MainGridData mLog;

    public MainGridViewHolder(Context context) {
        super(context);
        mContext = context;
        setup();
    }

    public MainGridViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setup();
    }

    private void setup() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_pokedex, this);
    }


    /*
    public void setLog(MainGridData log) {
        mLog = log;
        TextView PokemonName = (TextView) findViewById(R.id.pokename1);
        PokemonName.setText(mLog.getPokemonName() + "");
        ImageView PokePicture = (ImageView) findViewById(R.id.pokemonImage1);
        PokePicture.setImageDrawable(Drawable.createFromPath(mLog.getPokePicture() + ""));
    }

     */
}
