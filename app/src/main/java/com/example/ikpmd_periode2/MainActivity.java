package com.example.ikpmd_periode2;

import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.example.ikpmd_periode2.ui.graphs.GraphFragment;
import com.example.ikpmd_periode2.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    Fragment f = new GraphFragment();
    androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();


    ////////////////////////SELFWRITEN

    //Switches view to pokedetails if clicked on a pokemonmodel in the mainactivity


    public void Switcher_main_to_poke(View v) {
        //Get pokecard number
        System.out.println("aka " + v.getResources().getResourceName(v.getId()));
        String word = v.getResources().getResourceName(v.getId());
        String substr = word.substring(word.length() - 3);
        System.out.println("aka2 " + substr);
        String id =  substr.replaceAll("[A-Za-z]","");
        System.out.println("aka3 " + id);

        //Get involved textview
        String textviewID = "pokename" + id;
        int resID = getResources().getIdentifier(textviewID, "id", getPackageName());
        TextView pokenam = (TextView) findViewById(resID);
        System.out.println("Bring it back " + pokenam.getText());
        String cardname = (String) pokenam.getText();


        //Loop through DB Items and find involved information
        String allpkmnstats = "";
        for (int i = 0; i < 151; ++i){
            String name = getAllDBItems().get(i).get(0);
            if(name.equals(cardname)){
                System.out.println("This is in DB: " + name);
                System.out.println("This is in pokecard: " + cardname);

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
                allpkmnstats = name + ", "+ type1 + ", "+ type2 + ", "+ hp +", "+ atk +", "+ spatk +", "+ def +", "+ spdef +", "+ spd +", "+ weight +", "+ height;
            }
        }


        System.out.println("shadowing grows"+allpkmnstats);
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, a);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        PokeDetails.PokeStats = allpkmnstats;





        //setPokeDetailsFillables();

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
                        //System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                        //System.out.println("NAME LOADED");

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
                        //System.out.println("TYPES LOADED");

                        String pokeheight = Integer.toString(poketemp.getHeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Height, pokeheight);
                        //System.out.println("HEIGHT LOADED");

                        String pokeweight = Integer.toString(poketemp.getWeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Weight, pokeweight);
                        //System.out.println("WEIGHT LOADED");

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
                        //System.out.println("STATS LOADED");

                        //System.out.println("INSERTING DATA");
                        dbHelper.insert(DatabaseInfo.PokemonTable.POKEMONTABLE, null, values);
                        //System.out.println("DATA INSERTED");


                    }
                    //System.out.println("All data has been inserted 1");
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
                        //System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                        //System.out.println("NAME LOADED");

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
                        //System.out.println("TYPES LOADED");

                        String pokeheight = Integer.toString(poketemp.getHeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Height, pokeheight);
                        //System.out.println("HEIGHT LOADED");

                        String pokeweight = Integer.toString(poketemp.getWeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Weight, pokeweight);
                        //System.out.println("WEIGHT LOADED");

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
                       // System.out.println("STATS LOADED");

                       // System.out.println("INSERTING DATA");
                        dbHelper.insert(DatabaseInfo.PokemonTable.POKEMONTABLE, null, values);
                       // System.out.println("DATA INSERTED");


                    }
                   // System.out.println("All data has been inserted 1");
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
                      //  System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                       // System.out.println("NAME LOADED");

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
                       // System.out.println("TYPES LOADED");

                        String pokeheight = Integer.toString(poketemp.getHeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Height, pokeheight);
                       // System.out.println("HEIGHT LOADED");

                        String pokeweight = Integer.toString(poketemp.getWeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Weight, pokeweight);
                       // System.out.println("WEIGHT LOADED");

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
                      //  System.out.println("STATS LOADED");

                      //  System.out.println("INSERTING DATA");
                        dbHelper.insert(DatabaseInfo.PokemonTable.POKEMONTABLE, null, values);
                      //  System.out.println("DATA INSERTED");


                    }
                  //  System.out.println("All data has been inserted 1");
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
                      //  System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                      //  System.out.println("NAME LOADED");

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
                      //  System.out.println("TYPES LOADED");

                        String pokeheight = Integer.toString(poketemp.getHeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Height, pokeheight);
                     //   System.out.println("HEIGHT LOADED");

                        String pokeweight = Integer.toString(poketemp.getWeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Weight, pokeweight);
                     //   System.out.println("WEIGHT LOADED");

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
                     //   System.out.println("STATS LOADED");

                      //  System.out.println("INSERTING DATA");
                        dbHelper.insert(DatabaseInfo.PokemonTable.POKEMONTABLE, null, values);
                     //   System.out.println("DATA INSERTED");


                    }
                   // System.out.println("All data has been inserted 1");
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
                       // System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                      //  System.out.println("NAME LOADED");

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
                      //  System.out.println("TYPES LOADED");

                        String pokeheight = Integer.toString(poketemp.getHeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Height, pokeheight);
                      //  System.out.println("HEIGHT LOADED");

                        String pokeweight = Integer.toString(poketemp.getWeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Weight, pokeweight);
                     //   System.out.println("WEIGHT LOADED");

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
                     //   System.out.println("STATS LOADED");

                    //    System.out.println("INSERTING DATA");
                        dbHelper.insert(DatabaseInfo.PokemonTable.POKEMONTABLE, null, values);
                    //    System.out.println("DATA INSERTED");
                    }
                   // System.out.println("All data has been inserted 2");
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
                       // System.out.println(pokename);
                        values.put(DatabaseInfo.PokemonTable_Columns.Name, pokename);
                      //  System.out.println("NAME LOADED");

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
                      //  System.out.println("TYPES LOADED");

                        String pokeheight = Integer.toString(poketemp.getHeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Height, pokeheight);
                      //  System.out.println("HEIGHT LOADED");

                        String pokeweight = Integer.toString(poketemp.getWeight());
                        values.put(DatabaseInfo.PokemonTable_Columns.Weight, pokeweight);
                     //   System.out.println("WEIGHT LOADED");

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
                       // System.out.println("STATS LOADED");

                       // System.out.println("INSERTING DATA");
                        dbHelper.insert(DatabaseInfo.PokemonTable.POKEMONTABLE, null, values);
                      //  System.out.println("DATA INSERTED");
                    }
                   // System.out.println("All data has been inserted 2");
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
        //System.out.println(List_of_all_pokes);
        return List_of_all_pokes;
        //System.out.println(getAllDBItems(dbHelper2));
        //dbHelper2.getProfilesCount(DatabaseInfo.PokemonTable.POKEMONTABLE);
    }


    public void setPokeDetailsFillables(){
        TextView pokedetails_name = (TextView) findViewById(R.id.textName);
        System.out.println("Will this work " +pokedetails_name.getText());
    }



    public void setGridFillables(){
       // System.out.println("mind");

        MainActivity dbhelper = this;
        DatabaseHelper dbHelper2 = DatabaseHelper.getHelper(dbhelper);
       // System.out.println("yeetest");
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

            final int[] pictureArray = {R.drawable.pokemon_1, R.drawable.pokemon_2, R.drawable.pokemon_3, R.drawable.pokemon_4, R.drawable.pokemon_5, R.drawable.pokemon_6, R.drawable.pokemon_7, R.drawable.pokemon_8, R.drawable.pokemon_9, R.drawable.pokemon_10, R.drawable.pokemon_11, R.drawable.pokemon_12, R.drawable.pokemon_13, R.drawable.pokemon_14, R.drawable.pokemon_15, R.drawable.pokemon_16, R.drawable.pokemon_17, R.drawable.pokemon_18, R.drawable.pokemon_19, R.drawable.pokemon_20, R.drawable.pokemon_21, R.drawable.pokemon_22, R.drawable.pokemon_23, R.drawable.pokemon_24, R.drawable.pokemon_25, R.drawable.pokemon_26, R.drawable.pokemon_27, R.drawable.pokemon_28, R.drawable.pokemon_29, R.drawable.pokemon_30, R.drawable.pokemon_31, R.drawable.pokemon_32, R.drawable.pokemon_33, R.drawable.pokemon_34, R.drawable.pokemon_35, R.drawable.pokemon_36, R.drawable.pokemon_37, R.drawable.pokemon_38, R.drawable.pokemon_39, R.drawable.pokemon_40, R.drawable.pokemon_41, R.drawable.pokemon_42, R.drawable.pokemon_43, R.drawable.pokemon_44, R.drawable.pokemon_45, R.drawable.pokemon_46, R.drawable.pokemon_47, R.drawable.pokemon_48, R.drawable.pokemon_49, R.drawable.pokemon_50, R.drawable.pokemon_51, R.drawable.pokemon_52, R.drawable.pokemon_53, R.drawable.pokemon_54, R.drawable.pokemon_55, R.drawable.pokemon_56, R.drawable.pokemon_57, R.drawable.pokemon_58, R.drawable.pokemon_59, R.drawable.pokemon_60, R.drawable.pokemon_61, R.drawable.pokemon_62, R.drawable.pokemon_63, R.drawable.pokemon_64, R.drawable.pokemon_65, R.drawable.pokemon_66, R.drawable.pokemon_67, R.drawable.pokemon_68, R.drawable.pokemon_69, R.drawable.pokemon_70, R.drawable.pokemon_71, R.drawable.pokemon_72, R.drawable.pokemon_73, R.drawable.pokemon_74, R.drawable.pokemon_75, R.drawable.pokemon_76, R.drawable.pokemon_77, R.drawable.pokemon_78, R.drawable.pokemon_79, R.drawable.pokemon_80, R.drawable.pokemon_81, R.drawable.pokemon_82, R.drawable.pokemon_83, R.drawable.pokemon_84, R.drawable.pokemon_85, R.drawable.pokemon_86, R.drawable.pokemon_87, R.drawable.pokemon_88, R.drawable.pokemon_89, R.drawable.pokemon_90, R.drawable.pokemon_91, R.drawable.pokemon_92, R.drawable.pokemon_93, R.drawable.pokemon_94, R.drawable.pokemon_95, R.drawable.pokemon_96, R.drawable.pokemon_97, R.drawable.pokemon_98, R.drawable.pokemon_99, R.drawable.pokemon_100, R.drawable.pokemon_101, R.drawable.pokemon_102, R.drawable.pokemon_103, R.drawable.pokemon_104, R.drawable.pokemon_105, R.drawable.pokemon_106, R.drawable.pokemon_107, R.drawable.pokemon_108, R.drawable.pokemon_109, R.drawable.pokemon_110, R.drawable.pokemon_111, R.drawable.pokemon_112, R.drawable.pokemon_113, R.drawable.pokemon_114, R.drawable.pokemon_115, R.drawable.pokemon_116, R.drawable.pokemon_117, R.drawable.pokemon_118, R.drawable.pokemon_119, R.drawable.pokemon_120, R.drawable.pokemon_121, R.drawable.pokemon_122, R.drawable.pokemon_123, R.drawable.pokemon_124, R.drawable.pokemon_125, R.drawable.pokemon_126, R.drawable.pokemon_127, R.drawable.pokemon_128, R.drawable.pokemon_129, R.drawable.pokemon_130, R.drawable.pokemon_131, R.drawable.pokemon_132, R.drawable.pokemon_133, R.drawable.pokemon_134, R.drawable.pokemon_135, R.drawable.pokemon_136, R.drawable.pokemon_137, R.drawable.pokemon_138, R.drawable.pokemon_139, R.drawable.pokemon_140, R.drawable.pokemon_141, R.drawable.pokemon_142, R.drawable.pokemon_143, R.drawable.pokemon_144, R.drawable.pokemon_145, R.drawable.pokemon_146, R.drawable.pokemon_147, R.drawable.pokemon_148, R.drawable.pokemon_149, R.drawable.pokemon_150, R.drawable.pokemon_151};

            String imgID = "pokeimg" + e;
            int resID_img = this.getResources().getIdentifier(imgID, "id", getPackageName());
            ImageView pokeimg = (ImageView) findViewById(resID_img);

            try {
                pokeimg.setImageResource(pictureArray[PokeIDs.get(pokenam.getText())-1]);
            }catch (Exception t){
                continue;
            }

            //fuck mew, de laatste cardview in de grid doet altijd "SPECIAL" dus ik heb het uit frustratie maar gehardcode.
            String imgID_final = "pokeimg151";
            int resID_img_final = this.getResources().getIdentifier(imgID_final, "id", getPackageName());
            ImageView pokeimg_final = (ImageView) findViewById(resID_img_final);
            pokeimg_final.setImageResource(pictureArray[150]);






            //String type2 = getAllDBItems().get(i).get(2);
        }
        // globally

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        //in your OnCreate() method

    }


    // IF THE ANDROID BACK BUTTON IS CLICKED DO THIS PLEASE
    @Override
    public void onBackPressed() {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, e);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
            GridLayout mainGrid = (GridLayout) findViewById(R.id.mainGrid);
            mainGrid.setClickable(true);
            mainGrid.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            setContentView(R.layout.fragment_loading_d_b);
            messWithFirebase();
        }


    }



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

    }















    // doe dit als de app is gestart, moet als background task omdat je anders geen fragment ziet
    @Override
    protected void onStart() {
        super.onStart();
        //MainActivity dbhelper = this;
        DatabaseHelper dbHelper2 = DatabaseHelper.getHelper(this);

        if (dbHelper2.getProfilesCount(DatabaseInfo.PokemonTable.POKEMONTABLE) == 151) {

            try {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, e);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();


            } catch (Exception e) {
                e.printStackTrace();
            }
            setGridFillables();
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


        BottomNavigationView botnav = (BottomNavigationView) findViewById(R.id.nav_view);
        GridLayout mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        System.out.println("Yes a mattie");
                        fm.beginTransaction().replace(R.id.nav_host_fragment, e, "1").show(e).commit();
                        mainGrid.setClickable(true);
                        mainGrid.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.navigation_dashboard:
                        System.out.println("Yes a mattie");
                        fm.beginTransaction().replace(R.id.nav_host_fragment, b, "2").show(b).commit();
                        mainGrid.setClickable(false);
                        mainGrid.setVisibility(View.INVISIBLE);

                        return true;
                    case R.id.navigation_graph:
                        System.out.println("Yes a mattie");
                        fm.beginTransaction().replace(R.id.nav_host_fragment, f, "3").show(f).commit();
                        mainGrid.setClickable(false);
                        mainGrid.setVisibility(View.INVISIBLE);


                        return true;
                }
                return false;
            }
        });

    }



    //onStart is wat moet er gebeuren als de app wordt gestart


    //onResume is wat moet er gebeuren als de app van de achtergrond wordt gehaald en weer terug geopend wordt

    //onPause is wat moet er gebeuren bij een interupt, zoals een phone call

    //onStop is wat moet er gebeuren als de app helemaal afgesloten wordt, dus ook van de achtergrond

    //onDestroy is wat moet er gebeuren als de app verwijdert wordt.


}

