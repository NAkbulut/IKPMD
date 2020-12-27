package com.example.ikpmd_periode2;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ikpmd_periode2.grids.MainGridAdapter;
import com.example.ikpmd_periode2.grids.MainGridData;
import com.example.ikpmd_periode2.ui.dashboard.DashboardFragment;
import com.example.ikpmd_periode2.ui.home.HomeFragment;
import com.example.ikpmd_periode2.database.DatabaseHelper;
import com.example.ikpmd_periode2.database.DatabaseInfo;
import com.example.ikpmd_periode2.ui.LoadingDBFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
        System.out.println(pokid);
        int pid = 0;
        for(int i = 0; i < 151; ++i) {
            String pokidbuf = "card"+i;
            if (pokid.contains(pokidbuf)){
                pid = i;
                break;
            }
        }
        for(int i = 0; i < 151; ++i) {
            String name = getAllDBItems().get(i).get(0);
            String textviewID = "pokename" + pid;
            int resID = getResources().getIdentifier(textviewID, "id", getPackageName());
            TextView pokenam = (TextView) findViewById(resID);

            System.out.println(pokenam.getText());
            System.out.println(name);
            if (name.equals(pokenam.getText())) {
                System.out.println("START TEST");
                System.out.println(getAllDBItems().get(i));
                System.out.println("END TEST");
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
            TimeUnit.SECONDS.sleep(30);
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
                R.id.navigation_home, R.id.navigation_dashboard)
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
        for(int i = 0; i < dbHelper2.getProfilesCount(DatabaseInfo.PokemonTable.POKEMONTABLE); ++i){
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
            c.moveToPosition(i);
        }
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
            String name = getAllDBItems().get(i).get(0);
            int e = i+1;
            String textviewID = "pokename" + e;
            int resID = getResources().getIdentifier(textviewID, "id", getPackageName());
            TextView pokenam = (TextView) findViewById(resID);
            pokenam.setText(name);
            pokenam.getText();
            //String type1 = getAllDBItems().get(i).get(1);
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

        if(dbHelper2.getProfilesCount(DatabaseInfo.PokemonTable.POKEMONTABLE) == 151){
            setGridFillables();
            try {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, e);
                ft.addToBackStack(null);
                ft.commit();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            if (d.isAdded()) {
                Runnable backGroundRunnable = new Runnable() {
                    public void run() {
                        try {
                            valuesToDB();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread sampleThread = new Thread(backGroundRunnable);
                sampleThread.start();
                //setGridFillables();
            }

        }





            /*
            if (sampleThread.isAlive() == true){
                for(int i=1; i< 151; i++){
                    int resID = getResources().getIdentifier("pokename"+i,"id", getPackageName());
                    TextView textView = (TextView) findViewById(resID);
                    //System.out.println();
                    System.out.println("midget");
                    textView.setText(getAllDBItems().get(i).get(0));
                }
            }

            */

    }


    // IF THE ANDROID BACK BUTTON IS CLICKED DO THIS PLEASE
    @Override
    public void onBackPressed() {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, e);
            ft.addToBackStack(null);
            ft.commit();

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

