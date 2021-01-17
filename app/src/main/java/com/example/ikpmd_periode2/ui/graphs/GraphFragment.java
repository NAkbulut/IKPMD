package com.example.ikpmd_periode2.ui.graphs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ikpmd_periode2.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;


public class GraphFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        GraphView graph2 = (GraphView) view.findViewById(R.id.graph2);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 10),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 4)
        });
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 10),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 6),
                new DataPoint(4, 0),
                new DataPoint(5, 4)
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
        staticLabelsFormatter2.setHorizontalLabels(new String[] {"Grass", "Fire", "Water", "Electric", "Poison", "Other"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter2);

        return view;
}}