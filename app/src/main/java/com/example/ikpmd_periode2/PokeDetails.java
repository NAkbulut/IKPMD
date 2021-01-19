package com.example.ikpmd_periode2;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;

public class PokeDetails extends Fragment {


    static String PokeStats = "";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_poke_details, container, false);

        try{
            setPokeDetailsFillables(root);
        }catch(Exception e){
            System.out.println();
        }



        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Favo_Button();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setPokeDetailsFillables(View root){
        //setname
        TextView pokedetails_name = (TextView) root.findViewById(R.id.textName);
        String[] splittedString = PokeStats.split(", ");
        pokedetails_name.setText(splittedString[0]);

        //Set HP
        ProgressBar pokedetails_hp = (ProgressBar) root.findViewById(R.id.progressHP);
        System.out.println("Yeeee " +Integer.parseInt(splittedString[3]));
        pokedetails_hp.setProgress(Integer.parseInt(splittedString[3]),true);

        //Set ATK
        ProgressBar pokedetails_atk = (ProgressBar) root.findViewById(R.id.progressATK);
        System.out.println("Yeeee " +Integer.parseInt(splittedString[4]));
        pokedetails_atk.setProgress(Integer.parseInt(splittedString[4]),true);

        //Set SP_ATK
        ProgressBar pokedetails_sp_atk = (ProgressBar) root.findViewById(R.id.progressSPATK);
        System.out.println("Yeeee " +Integer.parseInt(splittedString[5]));
        pokedetails_sp_atk.setProgress(Integer.parseInt(splittedString[5]),true);

        //Set DEF
        ProgressBar pokedetails_def = (ProgressBar) root.findViewById(R.id.progressDEF);
        System.out.println("Yeeee " +Integer.parseInt(splittedString[6]));
        pokedetails_def.setProgress(Integer.parseInt(splittedString[6]),true);

        //Set SPDEF
        ProgressBar pokedetails_sp_def = (ProgressBar) root.findViewById(R.id.progressSPDEF);
        System.out.println("Yeeee " +Integer.parseInt(splittedString[7]));
        pokedetails_sp_def.setProgress(Integer.parseInt(splittedString[7]),true);

        //Set SPD
        ProgressBar pokedetails_spd = (ProgressBar) root.findViewById(R.id.progressSPD);
        System.out.println("Yeeee " +Integer.parseInt(splittedString[8]));
        pokedetails_spd.setProgress(Integer.parseInt(splittedString[8]),true);

        //Set weight
        TextView pokedetails_weight = (TextView) root.findViewById(R.id.textWeightValue);
        System.out.println("Yeeee " +Integer.parseInt(splittedString[9]));
        Float realweight = Float.valueOf(splittedString[9])/10;
        pokedetails_weight.setText(realweight + " KG");

        //Set weight
        TextView pokedetails_height = (TextView) root.findViewById(R.id.textHeightValue);
        System.out.println("Yeeee " +Integer.parseInt(splittedString[10]));
        Float realheight = Float.valueOf(splittedString[10])/10;
        pokedetails_height.setText(realheight + " M");

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
        PokeIDs.put("farfetchd", 83);
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
        PokeIDs.put("mr-mime", 122);
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
        ImageView imageView = (ImageView) root.findViewById(R.id.imageView);
        imageView.setImageResource(pictureArray[PokeIDs.get(pokedetails_name.getText())-1]);




        //Set types
        TextView pokedetails_type1 = (TextView) root.findViewById(R.id.textType);
        pokedetails_type1.setText(splittedString[1]);
        String type1 = (String) pokedetails_type1.getText();

        //Set colors
        if(type1.equals("rock")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.rock)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.rock)));
        }else if(type1.equals("ground")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.egg_yellow)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.egg_yellow)));
            pokedetails_type1.setTextColor(getResources().getColor(R.color.black));
        }else if(type1.equals("normal")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.normal)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.normal)));
        }else if(type1.equals("fighting")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fighting)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fighting)));
        }else if(type1.equals("flying")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.flying)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.flying)));
        }else if(type1.equals("poison")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.poison)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.poison)));
        }else if(type1.equals("bug")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bug)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bug)));
            pokedetails_type1.setTextColor(getResources().getColor(R.color.black));
        }else if(type1.equals("ghost")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ghost)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ghost)));
        }else if(type1.equals("steel")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.steel)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.steel)));
        }else if(type1.equals("???")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.special)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.special)));
        }else if(type1.equals("fire")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fire)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fire)));
            pokedetails_type1.setTextColor(getResources().getColor(R.color.black));
        }else if(type1.equals("water")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.water)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.water)));
        }else if(type1.equals("grass")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grass)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grass)));
        }else if(type1.equals("electric")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.electric)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.electric)));
            pokedetails_type1.setTextColor(getResources().getColor(R.color.black));
        }else if(type1.equals("psychic")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.psychic)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.psychic)));
        }else if(type1.equals("ice")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ice)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ice)));
            pokedetails_type1.setTextColor(getResources().getColor(R.color.black));
        }else if(type1.equals("dragon")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dragon)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dragon)));
        }else if(type1.equals("dark")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark)));
        }else if(type1.equals("fairy")){
            pokedetails_type1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fairy)));
            imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fairy)));
            pokedetails_type1.setTextColor(getResources().getColor(R.color.black));
        }

        TextView pokedetails_type2 = (TextView) root.findViewById(R.id.textType2);
        pokedetails_type2.setText(splittedString[2]);
        String type2 = (String) pokedetails_type2.getText();
        if(splittedString[2].equals("null")){
            pokedetails_type2.setVisibility(View.INVISIBLE);
            pokedetails_type1.setTranslationX(225);
        }else{
            pokedetails_type2.setVisibility(View.VISIBLE);
            pokedetails_type1.setTranslationX(0);
        }

        if(type2.equals("rock")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.rock)));
        }else if(type2.equals("ground")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.egg_yellow)));
            pokedetails_type2.setTextColor(getResources().getColor(R.color.black));
        }else if(type2.equals("normal")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.normal)));
        }else if(type2.equals("fighting")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fighting)));
        }else if(type2.equals("flying")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.flying)));
        }else if(type2.equals("poison")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.poison)));
        }else if(type2.equals("bug")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bug)));
            pokedetails_type2.setTextColor(getResources().getColor(R.color.black));
        }else if(type2.equals("ghost")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ghost)));
        }else if(type2.equals("steel")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.steel)));
        }else if(type2.equals("???")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.special)));
        }else if(type2.equals("fire")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fire)));
            pokedetails_type2.setTextColor(getResources().getColor(R.color.black));
        }else if(type2.equals("water")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.water)));
        }else if(type2.equals("grass")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grass)));
        }else if(type2.equals("electric")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.electric)));
            pokedetails_type2.setTextColor(getResources().getColor(R.color.black));
        }else if(type2.equals("psychic")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.psychic)));
        }else if(type2.equals("ice")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ice)));
            pokedetails_type2.setTextColor(getResources().getColor(R.color.black));
        }else if(type2.equals("dragon")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dragon)));
        }else if(type2.equals("dark")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark)));
        }else if(type2.equals("fairy")){
            pokedetails_type2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fairy)));
            pokedetails_type2.setTextColor(getResources().getColor(R.color.black));
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