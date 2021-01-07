package com.example.ikpmd_periode2;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ikpmd_periode2.database.DatabaseHelper;
import com.example.ikpmd_periode2.database.DatabaseInfo;
import com.example.ikpmd_periode2.ui.LoadingDBFragment;
import com.example.ikpmd_periode2.ui.dashboard.DashboardFragment;
import com.example.ikpmd_periode2.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import me.sargunvohra.lib.pokekotlin.model.PokemonStat;
import me.sargunvohra.lib.pokekotlin.model.PokemonType;



public class MainActivity extends AppCompatActivity {
    Fragment a = new PokeDetails();
    Fragment b = new DashboardFragment();
    Fragment c = new HomeFragment();
    Fragment d = new LoadingDBFragment();
    Fragment e = new NavHostFragment();


    ////////////////////////SELFWRITEN

    //Switches view to pokedetails if clicked on a pokemonmodel in the mainactivity


    public void Switcher_main_to_poke(View v) {
        List allDB = getAllDBItems();
        String pokid = v.getResources().getResourceName(v.getId());
        int pid = 0;
        for(int i = 0; i < 151; ++i) {
            String pokidbuf = "card"+i;
            if (pokid.endsWith(pokidbuf)){
                pid = i;
                break;
            }
        }
        for(int i = 0; i < 151; ++i) {
            String name = getAllDBItems().get(i).get(0);
            String textviewID = "pokename" + pid;
            int resID = getResources().getIdentifier(textviewID, "id", getPackageName());
            TextView pokenam = (TextView) findViewById(resID);

            if (name.equals(pokenam.getText())) {
                System.out.println(getAllDBItems().get(i));
                String type1 = getAllDBItems().get(i).get(1);
                String type2 = getAllDBItems().get(i).get(2);
                String hp = getAllDBItems().get(i).get(3);
                String atk = getAllDBItems().get(i).get(4);
                String spatk = getAllDBItems().get(i).get(5);
                String def = getAllDBItems().get(i).get(6);
                String spdef = getAllDBItems().get(i).get(7);
                String spd = getAllDBItems().get(i).get(8);
                String weight = getAllDBItems().get(i).get(9);
                String height = getAllDBItems().get(i).get(10);
                break;
            }
        }
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, a);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void valuesToDB() {
        MainActivity dbhelper = this;

        Runnable APICALL_run = new Runnable() {
            public void run() {
                try {
                    PokeApi pokeApi = new PokeApiClient();
                    for (int i = 0; i < 25; ++i) {
                        int k = i + 1;
                        int j = 0;
                        int l = 0;

                        Pokemon poketemp = pokeApi.getPokemon(k);
                        DatabaseHelper dbHelper = DatabaseHelper.getHelper(dbhelper);
                        ContentValues values = new ContentValues();

                        String pokename = poketemp.getName();
                        System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                        System.out.println("NAME LOADED");

                        List<PokemonType> types = poketemp.getTypes();
                        for (PokemonType temp : types) {
                            if (j == 0) {
                                values.put(DatabaseInfo.PokemonTable_Columns.type1, temp.component2().component1());
                                if (types.size() == 2) {
                                    j = 1;
                                }
                            }
                            if (j == 1) {
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
                        for (PokemonStat temp : stats) {
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
                    System.out.println("All data has been inserted 1");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        };
        Thread APICALL = new Thread(APICALL_run);
        APICALL.start();

        Runnable APICALL1_run = new Runnable() {
            public void run() {
                try {
                    PokeApi pokeApi1 = new PokeApiClient();
                    for (int i = 25; i < 50; ++i) {
                        int k = i + 1;
                        int j = 0;
                        int l = 0;

                        Pokemon poketemp = pokeApi1.getPokemon(k);
                        DatabaseHelper dbHelper = DatabaseHelper.getHelper(dbhelper);
                        ContentValues values = new ContentValues();

                        String pokename = poketemp.getName();
                        System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                        System.out.println("NAME LOADED");

                        List<PokemonType> types = poketemp.getTypes();
                        for (PokemonType temp : types) {
                            if (j == 0) {
                                values.put(DatabaseInfo.PokemonTable_Columns.type1, temp.component2().component1());
                                if (types.size() == 2) {
                                    j = 1;
                                }
                            }
                            if (j == 1) {
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
                        for (PokemonStat temp : stats) {
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
                    System.out.println("All data has been inserted 1");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        };
        Thread APICALL1 = new Thread(APICALL1_run);
        APICALL1.start();

        Runnable APICALL2_run = new Runnable() {
            public void run() {
                try {
                    PokeApi pokeApi2 = new PokeApiClient();
                    for (int i = 50; i < 74; ++i) {
                        int k = i + 1;
                        int j = 0;
                        int l = 0;

                        Pokemon poketemp = pokeApi2.getPokemon(k);
                        DatabaseHelper dbHelper = DatabaseHelper.getHelper(dbhelper);
                        ContentValues values = new ContentValues();

                        String pokename = poketemp.getName();
                        System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                        System.out.println("NAME LOADED");

                        List<PokemonType> types = poketemp.getTypes();
                        for (PokemonType temp : types) {
                            if (j == 0) {
                                values.put(DatabaseInfo.PokemonTable_Columns.type1, temp.component2().component1());
                                if (types.size() == 2) {
                                    j = 1;
                                }
                            }
                            if (j == 1) {
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
                        for (PokemonStat temp : stats) {
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
                    System.out.println("All data has been inserted 1");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        };
        Thread APICALL2 = new Thread(APICALL2_run);
        APICALL2.start();

        // Its a bit confusing I know
        Runnable APICALL5_run = new Runnable() {
            public void run() {
                try {
                    PokeApi pokeApi = new PokeApiClient();
                    for (int i = 74; i < 99; ++i) {
                        int k = i + 1;
                        int j = 0;
                        int l = 0;

                        Pokemon poketemp = pokeApi.getPokemon(k);
                        DatabaseHelper dbHelper = DatabaseHelper.getHelper(dbhelper);
                        ContentValues values = new ContentValues();

                        String pokename = poketemp.getName();
                        System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                        System.out.println("NAME LOADED");

                        List<PokemonType> types = poketemp.getTypes();
                        for (PokemonType temp : types) {
                            if (j == 0) {
                                values.put(DatabaseInfo.PokemonTable_Columns.type1, temp.component2().component1());
                                if (types.size() == 2) {
                                    j = 1;
                                }
                            }
                            if (j == 1) {
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
                        for (PokemonStat temp : stats) {
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
                    System.out.println("All data has been inserted 1");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        };
        Thread APICALL5 = new Thread(APICALL5_run);
        APICALL5.start();

        Runnable APICALL3_run = new Runnable() {
            public void run() {
                try {
                    PokeApi pokeApi3 = new PokeApiClient();
                    for (int i = 99; i < 113; ++i) {
                        int k = i + 1;
                        int j = 0;
                        int l = 0;

                        Pokemon poketemp = pokeApi3.getPokemon(k);
                        DatabaseHelper dbHelper = DatabaseHelper.getHelper(dbhelper);
                        ContentValues values = new ContentValues();

                        String pokename = poketemp.getName();
                        System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                        System.out.println("NAME LOADED");

                        List<PokemonType> types = poketemp.getTypes();
                        for (PokemonType temp : types) {
                            if (j == 0) {
                                values.put(DatabaseInfo.PokemonTable_Columns.type1, temp.component2().component1());
                                if (types.size() == 2) {
                                    j = 1;
                                }
                            }
                            if (j == 1) {
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
                        for (PokemonStat temp : stats) {
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
                    System.out.println("All data has been inserted 2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        Thread APICALL3 = new Thread(APICALL3_run);
        APICALL3.start();

        Runnable APICALL4_run = new Runnable() {
            public void run() {
                try {
                    boolean isdone4 = false;
                    PokeApi pokeApi4 = new PokeApiClient();
                    for (int i = 113; i < 151; ++i) {
                        int k = i + 1;
                        int j = 0;
                        int l = 0;

                        Pokemon poketemp = pokeApi4.getPokemon(k);
                        DatabaseHelper dbHelper = DatabaseHelper.getHelper(dbhelper);
                        ContentValues values = new ContentValues();

                        String pokename = poketemp.getName();
                        System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                        System.out.println("NAME LOADED");

                        List<PokemonType> types = poketemp.getTypes();
                        for (PokemonType temp : types) {
                            if (j == 0) {
                                values.put(DatabaseInfo.PokemonTable_Columns.type1, temp.component2().component1());
                                if (types.size() == 2) {
                                    j = 1;
                                }
                            }
                            if (j == 1) {
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
                        for (PokemonStat temp : stats) {
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
                    System.out.println("All data has been inserted 2");
                    isdone4 = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        Thread APICALL4 = new Thread(APICALL4_run);
        APICALL4.start();

        try {
            Thread.sleep(30000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }


        //checks how many rows in db table
        DatabaseHelper dbHelper2 = DatabaseHelper.getHelper(dbhelper);
        dbHelper2.getProfilesCount(DatabaseInfo.PokemonTable.POKEMONTABLE);
        //getAllDBItems();
        // switches back to normal start screen instead of DB loading fragment
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, e);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }






    }


    public void Switcher_poke(View v) {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            //fm.popBackStack();
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            //fm.popBackStack();
            super.onBackPressed();
        }

    }


    public boolean checkIfOnline() {
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


    //////////////////////ANDROID FRAMEWORK

    private void messWithFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://ikpmd-aba46-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello world!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                System.out.println("New Value =" + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    ;
    //onCreate is wat moet er gebeuren bij buildtime





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_graph)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //makes sure app starts with loading DB screen
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, d);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }




            //MainGridAdapter.notifyDataSetChanged();
    }



    public List<List<String>> getAllDBItems(){
        MainActivity dbhelper = this;
        DatabaseHelper dbHelper2 = DatabaseHelper.getHelper(dbhelper);

        Cursor c = dbHelper2.query(DatabaseInfo.PokemonTable.POKEMONTABLE, new String[]{
                        DatabaseInfo.PokemonTable_Columns.Name,
                        DatabaseInfo.PokemonTable_Columns.SP_DEF,
                        DatabaseInfo.PokemonTable_Columns.DEF,
                        DatabaseInfo.PokemonTable_Columns.SP_ATK,
                        DatabaseInfo.PokemonTable_Columns.ATK,
                        DatabaseInfo.PokemonTable_Columns.type1,
                        DatabaseInfo.PokemonTable_Columns.type2,
                        DatabaseInfo.PokemonTable_Columns.HP,
                        DatabaseInfo.PokemonTable_Columns.SPD,
                        DatabaseInfo.PokemonTable_Columns.Weight,
                        DatabaseInfo.PokemonTable_Columns.Height},
                null,
                null,
                null,
                null,
                null);
        c.moveToFirst();
        List<List<String>> List_of_all_pokes = new ArrayList<>();
        for(int i = 0; i < 151; ++i){
            c.moveToPosition(i);
            String name = (String) c.getString(c.getColumnIndex("Name"));
            String type1 = (String) c.getString(c.getColumnIndex("Type1"));
            String type2 = (String) c.getString(c.getColumnIndex("Type2"));
            String HP = (String) c.getString(c.getColumnIndex("HP"));
            String ATK = (String) c.getString(c.getColumnIndex("ATK"));
            String SP_ATK = (String) c.getString(c.getColumnIndex("SP_ATK"));
            String DEF = (String) c.getString(c.getColumnIndex("DEF"));
            String SP_DEF = (String) c.getString(c.getColumnIndex("SP_DEF"));
            String SPD = (String) c.getString(c.getColumnIndex("SPD"));
            String Weight = (String) c.getString(c.getColumnIndex("Weight"));
            String Height = (String) c.getString(c.getColumnIndex("Height"));
            List<String> pk_list = new ArrayList<>();
            pk_list.add(name);
            pk_list.add(type1);
            pk_list.add(type2);
            pk_list.add(HP);
            pk_list.add(ATK);
            pk_list.add(SP_ATK);
            pk_list.add(DEF);
            pk_list.add(SP_DEF);
            pk_list.add(SPD);
            pk_list.add(Weight);
            pk_list.add(Height);
            List_of_all_pokes.add(pk_list);
            //System.out.println(pk_list);
        }
        System.out.println(List_of_all_pokes);
        return List_of_all_pokes;
        //System.out.println(getAllDBItems(dbHelper2));
        //dbHelper2.getProfilesCount(DatabaseInfo.PokemonTable.POKEMONTABLE);
    }


    public void setGridFillables(){
        System.out.println("mind");

        MainActivity dbhelper = this;
        DatabaseHelper dbHelper2 = DatabaseHelper.getHelper(dbhelper);
        System.out.println("yeetest");
        for(int i = 0; i < 151; ++i){

            //set name
            String name = getAllDBItems().get(i).get(0);
            int e = i + 1;
            String textviewID = "pokename" + e;
            int resID = this.getResources().getIdentifier(textviewID, "id", getPackageName());
            TextView pokenam = (TextView) findViewById(resID);
            pokenam.setText(name);
            pokenam.getText();

            //set color
            String type1 = getAllDBItems().get(i).get(1);
            int q = i + 1;
            String cardviewID = "pokecard" + q;
            int resID_card = this.getResources().getIdentifier(cardviewID, "id", getPackageName());
            CardView pokecard = (CardView) findViewById(resID_card);
            if(type1.equals("rock")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.rock));
            }else if(type1.equals("ground")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.egg_yellow));
            }else if(type1.equals("normal")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.normal));
            }else if(type1.equals("fighting")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.fighting));
            }else if(type1.equals("flying")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.flying));
            }else if(type1.equals("poison")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.poison));
            }else if(type1.equals("bug")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.bug));
            }else if(type1.equals("ghost")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.ghost));
            }else if(type1.equals("steel")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.steel));
            }else if(type1.equals("???")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.special));
            }else if(type1.equals("fire")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.fire));
            }else if(type1.equals("water")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.water));
            }else if(type1.equals("grass")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.grass));
            }else if(type1.equals("electric")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.electric));
            }else if(type1.equals("psychic")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.psychic));
            }else if(type1.equals("ice")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.ice));
            }else if(type1.equals("dragon")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.dragon));
            }else if(type1.equals("dark")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.dark));
            }else if(type1.equals("fairy")){
                pokecard.setCardBackgroundColor(getResources().getColor(R.color.fairy));
            }

            //change text color
            pokenam.setTextColor(getResources().getColor(R.color.white));
            pokenam.setShadowLayer(5.8f, 1.5f, 1.3f, getResources().getColor(R.color.kloteding));

            //set images
            HashMap<String, Integer> PokeIDs = new HashMap<String, Integer>();
            PokeIDs.put("bulbasaur", 1);
            PokeIDs.put("ivysaur", 2);
            PokeIDs.put("venusaur", 3);
            PokeIDs.put("charmander", 4);
            PokeIDs.put("charmeleon", 5);
            PokeIDs.put("charizard", 6);
            PokeIDs.put("squirtle", 7);
            PokeIDs.put("wartortle", 8);
            PokeIDs.put("blastoise", 9);
            PokeIDs.put("caterpie", 10);
            PokeIDs.put("metapod", 11);
            PokeIDs.put("butterfree", 12);
            PokeIDs.put("weedle", 13);
            PokeIDs.put("kakuna", 14);
            PokeIDs.put("beedrill", 15);
            PokeIDs.put("pidgey", 16);
            PokeIDs.put("pidgeotto", 17);
            PokeIDs.put("pidgeot", 18);
            PokeIDs.put("rattata", 19);
            PokeIDs.put("raticate", 20);
            PokeIDs.put("spearow", 21);
            PokeIDs.put("fearow", 22);
            PokeIDs.put("ekans", 23);
            PokeIDs.put("arbok", 24);
            PokeIDs.put("pikachu", 25);
            PokeIDs.put("raichu", 26);
            PokeIDs.put("sandshrew", 27);
            PokeIDs.put("sandslash", 28);
            PokeIDs.put("nidoran-f", 29);
            PokeIDs.put("nidorina", 30);
            PokeIDs.put("nidoqueen", 31);
            PokeIDs.put("nidoran-m", 32);
            PokeIDs.put("nidorino", 33);
            PokeIDs.put("nidoking", 34);
            PokeIDs.put("clefairy", 35);
            PokeIDs.put("clefable", 36);
            PokeIDs.put("vulpix", 37);
            PokeIDs.put("ninetales", 38);
            PokeIDs.put("jigglypuff", 39);
            PokeIDs.put("wigglytuff", 40);
            PokeIDs.put("zubat", 41);
            PokeIDs.put("golbat", 42);
            PokeIDs.put("oddish", 43);
            PokeIDs.put("gloom", 44);
            PokeIDs.put("vileplume", 45);
            PokeIDs.put("paras", 46);
            PokeIDs.put("parasect", 47);
            PokeIDs.put("venonat", 48);
            PokeIDs.put("venomoth", 49);
            PokeIDs.put("diglett", 50);
            PokeIDs.put("dugtrio", 51);
            PokeIDs.put("meowth", 52);
            PokeIDs.put("persian", 53);
            PokeIDs.put("psyduck", 54);
            PokeIDs.put("golduck", 55);
            PokeIDs.put("mankey", 56);
            PokeIDs.put("primeape", 57);
            PokeIDs.put("growlithe", 58);
            PokeIDs.put("arcanine", 59);
            PokeIDs.put("poliwag", 60);
            PokeIDs.put("poliwhirl", 61);
            PokeIDs.put("poliwrath", 62);
            PokeIDs.put("abra", 63);
            PokeIDs.put("kadabra", 64);
            PokeIDs.put("alakazam", 65);
            PokeIDs.put("machop", 66);
            PokeIDs.put("machoke", 67);
            PokeIDs.put("machamp", 68);
            PokeIDs.put("bellsprout", 69);
            PokeIDs.put("weepinbell", 70);
            PokeIDs.put("victreebel", 71);
            PokeIDs.put("tentacool", 72);
            PokeIDs.put("tentacruel", 73);
            PokeIDs.put("geodude", 74);
            PokeIDs.put("graveler", 75);
            PokeIDs.put("golem", 76);
            PokeIDs.put("ponyta", 77);
            PokeIDs.put("rapidash", 78);
            PokeIDs.put("slowpoke", 79);
            PokeIDs.put("slowbro", 80);
            PokeIDs.put("magnemite", 81);
            PokeIDs.put("magneton", 82);
            PokeIDs.put("farfetch'd", 83);
            PokeIDs.put("doduo", 84);
            PokeIDs.put("dodrio", 85);
            PokeIDs.put("seel", 86);
            PokeIDs.put("dewgong", 87);
            PokeIDs.put("grimer", 88);
            PokeIDs.put("muk", 89);
            PokeIDs.put("shellder", 90);
            PokeIDs.put("cloyster", 91);
            PokeIDs.put("gastly", 92);
            PokeIDs.put("haunter", 93);
            PokeIDs.put("gengar", 94);
            PokeIDs.put("onix", 95);
            PokeIDs.put("drowzee", 96);
            PokeIDs.put("hypno", 97);
            PokeIDs.put("krabby", 98);
            PokeIDs.put("kingler", 99);
            PokeIDs.put("voltorb", 100);
            PokeIDs.put("electrode", 101);
            PokeIDs.put("exeggcute", 102);
            PokeIDs.put("exeggutor", 103);
            PokeIDs.put("cubone", 104);
            PokeIDs.put("marowak", 105);
            PokeIDs.put("hitmonlee", 106);
            PokeIDs.put("hitmonchan", 107);
            PokeIDs.put("lickitung", 108);
            PokeIDs.put("koffing", 109);
            PokeIDs.put("weezing", 110);
            PokeIDs.put("rhyhorn", 111);
            PokeIDs.put("rhydon", 112);
            PokeIDs.put("chansey", 113);
            PokeIDs.put("tangela", 114);
            PokeIDs.put("kangaskhan", 115);
            PokeIDs.put("horsea", 116);
            PokeIDs.put("seadra", 117);
            PokeIDs.put("goldeen", 118);
            PokeIDs.put("seaking", 119);
            PokeIDs.put("staryu", 120);
            PokeIDs.put("starmie", 121);
            PokeIDs.put("mr. mime", 122);
            PokeIDs.put("scyther", 123);
            PokeIDs.put("jynx", 124);
            PokeIDs.put("electabuzz", 125);
            PokeIDs.put("magmar", 126);
            PokeIDs.put("pinsir", 127);
            PokeIDs.put("tauros", 128);
            PokeIDs.put("magikarp", 129);
            PokeIDs.put("gyarados", 130);
            PokeIDs.put("lapras", 131);
            PokeIDs.put("ditto", 132);
            PokeIDs.put("eevee", 133);
            PokeIDs.put("vaporeon", 134);
            PokeIDs.put("jolteon", 135);
            PokeIDs.put("flareon", 136);
            PokeIDs.put("porygon", 137);
            PokeIDs.put("omanyte", 138);
            PokeIDs.put("omastar", 139);
            PokeIDs.put("kabuto", 140);
            PokeIDs.put("kabutops", 141);
            PokeIDs.put("aerodactyl", 142);
            PokeIDs.put("snorlax", 143);
            PokeIDs.put("articuno", 144);
            PokeIDs.put("zapdos", 145);
            PokeIDs.put("moltres", 146);
            PokeIDs.put("dratini", 147);
            PokeIDs.put("dragonair", 148);
            PokeIDs.put("dragonite", 149);
            PokeIDs.put("mewtwo", 150);
            PokeIDs.put("mew", 151);
            System.out.println("Printing ID");

            /*
            String imgID = "pokeimg" + e;
            int resID_img = this.getResources().getIdentifier(imgID, "id", getPackageName());
            ImageView pokeimg = (ImageView) findViewById(resID_img);
            int PokeID = PokeIDs.get(pokenam.getText());
            String imgname = "@drawable/pokename_" + PokeID;
            pokeimg.setImageDrawable(Drawable.createFromPath(imgname));
            */



            //String type2 = getAllDBItems().get(i).get(2);
        }
        // globally


        //in your OnCreate() method

    }


    // doe dit als de app is gestart, moet als background task omdat je anders geen fragment ziet
    @Override
    protected void onStart() {
        super.onStart();
        MainActivity dbhelper = this;
        DatabaseHelper dbHelper2 = DatabaseHelper.getHelper(dbhelper);

        if (dbHelper2.getProfilesCount(DatabaseInfo.PokemonTable.POKEMONTABLE) == 151) {
            setGridFillables();
            try {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, e);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(e.getClass().getName());
                ft.commit();


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (d.isAdded()) {
                Runnable backGroundRunnable = new Runnable() {
                    public void run() {
                        try {
                            valuesToDB();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //Thread.sleep(30000);
                                        setGridFillables();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread sampleThread = new Thread(backGroundRunnable);
                sampleThread.start();
            }
        }

        if(e.isAdded()){
            setGridFillables();
        }
    }


    // IF THE ANDROID BACK BUTTON IS CLICKED DO THIS PLEASE
    @Override
    public void onBackPressed() {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, e);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(e.getClass().getName());
            ft.commit();
            setGridFillables();
        } catch (Exception e) {
            setContentView(R.layout.fragment_loading_d_b);
            messWithFirebase();
        }


    }


    //onStart is wat moet er gebeuren als de app wordt gestart


    //onResume is wat moet er gebeuren als de app van de achtergrond wordt gehaald en weer terug geopend wordt

    //onPause is wat moet er gebeuren bij een interupt, zoals een phone call

    //onStop is wat moet er gebeuren als de app helemaal afgesloten wordt, dus ook van de achtergrond

    //onDestroy is wat moet er gebeuren als de app verwijdert wordt.


}

