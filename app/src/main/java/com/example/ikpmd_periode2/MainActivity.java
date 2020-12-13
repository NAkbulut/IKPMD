package com.example.ikpmd_periode2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.example.ikpmd_periode2.database.DatabaseHelper;
import com.example.ikpmd_periode2.database.DatabaseInfo;
import com.example.ikpmd_periode2.ui.LoadingDBFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import java.util.List;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import me.sargunvohra.lib.pokekotlin.model.PokemonStat;
import me.sargunvohra.lib.pokekotlin.model.PokemonType;
import me.sargunvohra.lib.pokekotlin.model.Type;


public class MainActivity extends AppCompatActivity {


    ////////////////////////SELFWRITEN

    //Switches view to pokedetails if clicked on a pokemonmodel in the mainactivity
    public void Switcher(View v){
        setContentView(R.layout.fragment_poke_details);
        //startActivity(new Intent(MainActivity.this,PokeDetails.class));
    }


    public boolean checkIfOnline(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void valuesToDB() {
        PokeApi pokeApi = new PokeApiClient();
        for (int i = 0; i < 151; ++i){
            int k = i + 1;
            int j = 0;
            int l = 0;

            Pokemon poketemp = pokeApi.getPokemon(k);
            DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);
            ContentValues values = new ContentValues();

            String pokename = poketemp.getName();
            System.out.println(pokename);
            values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
            System.out.println("NAME LOADED");

            List<PokemonType> types = poketemp.getTypes();
            for (PokemonType temp : types){
                if (j == 0){
                    values.put(DatabaseInfo.PokemonTable_Columns.type1, temp.component2().component1());
                    if (types.size() == 2) {
                        j = 1;
                    }
                }
                if (j == 1){
                    values.put(DatabaseInfo.PokemonTable_Columns.type2, temp.component2().component1());

                }
            }
            System.out.println("TYPES LOADED");

            String pokeheight = Integer.toString(poketemp.getHeight());
            values.put(DatabaseInfo.PokemonTable_Columns.Height, pokeheight);
            System.out.println("HEIGHT LOADED");

            String pokeweight = Integer.toString(poketemp.getWeight());
            values.put(DatabaseInfo.PokemonTable_Columns.Weight, pokeweight);
            System.out.println("WEIGHT LOADED");

            List<PokemonStat> stats = poketemp.getStats();
            for (PokemonStat temp : stats){
                if (l == 0) {
                    values.put(DatabaseInfo.PokemonTable_Columns.HP, temp.component3());
                }
                if (l == 1) {
                    values.put(DatabaseInfo.PokemonTable_Columns.ATK, temp.component3());
                }
                if (l == 2) {
                    values.put(DatabaseInfo.PokemonTable_Columns.DEF, temp.component3());
                }
                if (l == 3) {
                    values.put(DatabaseInfo.PokemonTable_Columns.SP_ATK, temp.component3());
                }
                if (l == 4) {
                    values.put(DatabaseInfo.PokemonTable_Columns.SP_DEF, temp.component3());
                }
                if (l == 5) {
                    values.put(DatabaseInfo.PokemonTable_Columns.SPD, temp.component3());
                }
                l = l + 1;
            }
            System.out.println("STATS LOADED");

            System.out.println("INSERTING DATA");
            dbHelper.insert(DatabaseInfo.PokemonTable.POKEMONTABLE, null, values);
            System.out.println("DATA INSERTED");
        }
    }





    //////////////////////ANDROID FRAMEWORK

    //onCreate is wat moet er gebeuren bij buildtime

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);






        //System.out.println(checkIfOnline());


    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.fragment_loading_d_b);
        try {
            System.out.println("Start DB");
            valuesToDB();
            System.out.println("End DB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //onStart is wat moet er gebeuren als de app wordt gestart


    //onResume is wat moet er gebeuren als de app van de achtergrond wordt gehaald en weer terug geopend wordt

    //onPause is wat moet er gebeuren bij een interupt, zoals een phone call

    //onStop is wat moet er gebeuren als de app helemaal afgesloten wordt, dus ook van de achtergrond

    //onDestroy is wat moet er gebeuren als de app verwijdert wordt.




}
