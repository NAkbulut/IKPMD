package com.example.ikpmd_periode2.ui.dashboard;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ikpmd_periode2.PokeDetails;
import com.example.ikpmd_periode2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.graphics.Color.parseColor;
import static java.lang.Integer.parseInt;

public class DashboardFragment extends Fragment {

    public static int FavCounter;
    private DashboardViewModel dashboardViewModel;
    Fragment a = new PokeDetails();
    List allFavs = PokeDetails.AllFavs;

    int totalHP;
    int totalATK;
    int totalSPATK;
    int totalDEF;
    int totalSPDEF;
    int totalSPD;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        LinearLayout yeet = getView().findViewById(R.id.yeet);
        setFavFillables();


    }

    public void setFavFillables(){
        GridLayout layout = getView().findViewById(R.id.mainGrid);
        int count = layout.getChildCount();
        View v = null;
        for(Object entry : allFavs){
            for(int i=0; i<FavCounter; i++) {
                v = layout.getChildAt(i);
                System.out.println("Visibility count: " + count);
                System.out.println("Visibility: " + v.getVisibility());
                v.setVisibility(View.VISIBLE);

                int e = i+1;
                String newEntry = allFavs.get(i).toString().replace("[","").replace("]","");
                String[] SplittedString = newEntry.split(", ");

                //String[] SplittedString = PokeDetails.PokeStats.split(", ");


                //set name
                String textviewID = "favname" + e;
                int resID = getResources().getIdentifier(textviewID, "id", getActivity().getPackageName());
                TextView pokenam = v.findViewById(resID);
                String name= SplittedString[0];
                pokenam.setText(name);
                pokenam.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat("20"));

                //set HP
                String HP = "hp" + e;
                int hpID = getResources().getIdentifier(HP, "id", getActivity().getPackageName());
                ProgressBar pokehp = v.findViewById(hpID);
                String hp = SplittedString[1];
                System.out.println("HP: " + name + hp);
                pokehp.setMax(250);
                pokehp.setProgress(parseInt(hp));


                //set ATK
                String ATK = "atk" + e;
                int atkID = getResources().getIdentifier(ATK, "id", getActivity().getPackageName());
                ProgressBar pokeatk = v.findViewById(atkID);
                String atk = SplittedString[2];
                System.out.println("ATK: " + name + atk);
                pokeatk.setMax(134);
                pokeatk.setProgress(parseInt(atk));
                pokeatk.setProgressTintList(ColorStateList.valueOf(parseColor("#FF9800")));

                //set spatk
                String SPATK = "spatk" + e;
                int spatkID = getResources().getIdentifier(SPATK, "id", getActivity().getPackageName());
                ProgressBar pokespatk = v.findViewById(spatkID);
                String spatk = SplittedString[3];
                System.out.println("SPATK: " + name + spatk);
                pokespatk.setMax(194);
                pokespatk.setProgress(parseInt(spatk));
                pokespatk.setProgressTintList(ColorStateList.valueOf(parseColor("#2196F3")));


                //set def
                String DEF = "def" + e;
                int defID = getResources().getIdentifier(DEF, "id", getActivity().getPackageName());
                ProgressBar pokedef = v.findViewById(defID);
                String def = SplittedString[4];
                System.out.println("DEF: " + name + def);
                pokedef.setMax(180);
                pokedef.setProgress(parseInt(def));
                pokedef.setProgressTintList(ColorStateList.valueOf(parseColor("#FFEB3B")));



                //set spdef
                String SPDEF = "spdef" + e;
                int spdefID = getResources().getIdentifier(SPDEF, "id", getActivity().getPackageName());
                ProgressBar pokespdef = v.findViewById(spdefID);
                String spdef = SplittedString[5];
                System.out.println("SPDEF: " + name + spdef);
                pokespdef.setMax(255);
                pokespdef.setProgress(parseInt(spdef));
                pokespdef.setProgressTintList(ColorStateList.valueOf(parseColor("#4CAF50")));

                //set image
                String IMG = "favimg" + e;
                int imgID = getResources().getIdentifier(IMG, "id", getActivity().getPackageName());
                ImageView pokeimg = v.findViewById(imgID);

                //System.out.println("Fatboy Slim " + );
                pokeimg.setImageResource(PokeDetails.pictureArray[PokeDetails.PokeIDs.get(pokenam.getText())-1]);

                totalHP = totalHP + parseInt(hp);
                totalATK = totalATK + parseInt(atk);
                totalSPATK = totalSPATK + parseInt(spatk);
                totalDEF = totalDEF + parseInt(def);
                totalSPDEF = totalSPDEF + parseInt(spdef);
                //totalSPD = totalSPD + parseInt(spd);

                System.out.println("Totaal HP: " + totalHP);
                System.out.println("Totaal ATK: " + totalATK);
                System.out.println("Totaal SPATK: " + totalSPATK);
                System.out.println("Totaal DEF: " + totalDEF);
                System.out.println("Totaal SPDEF: " + totalSPDEF);
                //System.out.println("Totaal SPD: " + totalSPD);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(name);

                myRef.setValue(totalHP + ", " + totalATK + ", " + totalSPATK + ", " + totalDEF + ", " + totalSPDEF);
            }

        }
    }



}