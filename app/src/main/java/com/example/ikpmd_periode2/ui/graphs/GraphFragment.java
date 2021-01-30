package com.example.ikpmd_periode2.ui.graphs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ikpmd_periode2.PokeDetails;
import com.example.ikpmd_periode2.R;
import com.example.ikpmd_periode2.ui.favorites.FavoritesFragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;


public class GraphFragment extends Fragment {
    int totalHP;
    int totalATK;
    int totalSPATK;
    int totalDEF;
    int totalSPDEF;
    int totalSPD;

    int totalTypeGrass;
    int totalTypeFire;
    int totalTypeWater;
    int totalTypeElectric;
    int totalTypePoison;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initiateVars();
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        GraphView graph2 = view.findViewById(R.id.graph2);
        GraphView graph = view.findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, totalHP),
                new DataPoint(1, totalATK),
                new DataPoint(2, totalSPATK),
                new DataPoint(3, totalDEF),
                new DataPoint(4, totalSPDEF),
                new DataPoint(5, totalSPD)
        });
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, totalTypeGrass),
                new DataPoint(1, totalTypeFire),
                new DataPoint(2, totalTypeWater),
                new DataPoint(3, totalTypeElectric),
                new DataPoint(4, totalTypePoison)
        });
        graph2.addSeries(series2);
        graph.addSeries(series);

        series2.setSpacing(40);
        series.setSpacing(40);
        graph2.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph2.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph2.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        series2.setColor(Color.rgb(149, 23, 34));
        series.setColor(Color.rgb(149, 23, 34));

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph2);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"HP", "ATK", "SP. ATK", "DEF", "SP. DEF", "SPD"});
        graph2.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        StaticLabelsFormatter staticLabelsFormatter2 = new StaticLabelsFormatter(graph);
        staticLabelsFormatter2.setHorizontalLabels(new String[] {"Grass", "Fire", "Water", "Electric", "Poison"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter2);

        return view;
    }

    public void initiateVars() {
        totalHP = PokeDetails.totalHP;
        totalATK = PokeDetails.totalATK;
        totalSPATK = PokeDetails.totalSPATK;
        totalDEF = PokeDetails.totalDEF;
        totalSPDEF = PokeDetails.totalSPDEF;
        totalSPD = PokeDetails.totalSPD;

        totalTypeGrass = PokeDetails.totalTypeGrass;
        totalTypeFire = PokeDetails.totalTypeFire;
        totalTypeWater = PokeDetails.totalTypeWater;
        totalTypeElectric = PokeDetails.totalTypeElectric;
        totalTypePoison = PokeDetails.totalTypePoison;
    }
}