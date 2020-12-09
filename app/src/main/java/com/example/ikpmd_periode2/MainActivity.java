package com.example.ikpmd_periode2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.StringBuffer;


public class MainActivity extends AppCompatActivity {
    CardView pokemon;
    CardView pokemon2;
    CardView pokemon3;
    CardView pokemon4;

    ////////////////////////SELFWRITEN

    //Switches view to pokedetails if clicked on a pokemon in the mainactivity
    public void Switcher(View v){
        setContentView(R.layout.fragment_poke_details);
        //startActivity(new Intent(MainActivity.this,PokeDetails.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        String base;
        System.out.println("test");
        try {
            URL url = new URL("https://pokeapi.co/api/v2/generation/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            //System.out.println(content.toString());
            base = content.toString();
            System.out.println(base);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }





    //////////////////////ANDROID FRAMEWORK

    //onCreate is wat moet er gebeuren bij buildtime
    @RequiresApi(api = Build.VERSION_CODES.M)
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
