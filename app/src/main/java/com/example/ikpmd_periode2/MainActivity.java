package com.example.ikpmd_periode2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import java.util.List;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
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

    private void makeClassesFromJSON() {
        PokeApi pokeApi = new PokeApiClient();
        Pokemon bulbasaur = pokeApi.getPokemon(1);
        List<PokemonType> yeet = bulbasaur.getTypes();
        System.out.println(bulbasaur);
        System.out.println(yeet);
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

        try {
            System.out.println("test1");
            makeClassesFromJSON();
            System.out.println("test2");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //System.out.println("yeet");



        //System.out.println(checkIfOnline());


    }



    //onStart is wat moet er gebeuren als de app wordt gestart


    //onResume is wat moet er gebeuren als de app van de achtergrond wordt gehaald en weer terug geopend wordt

    //onPause is wat moet er gebeuren bij een interupt, zoals een phone call

    //onStop is wat moet er gebeuren als de app helemaal afgesloten wordt, dus ook van de achtergrond

    //onDestroy is wat moet er gebeuren als de app verwijdert wordt.




}
