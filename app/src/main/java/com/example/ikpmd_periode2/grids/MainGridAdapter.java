package com.example.ikpmd_periode2.grids;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ikpmd_periode2.MainActivity;

import java.util.ArrayList;

public class MainGridAdapter extends BaseAdapter {
    ArrayList<MainGridData> myList = new ArrayList<MainGridData>();
    Context context;

    public MainGridAdapter(MainActivity mainActivity, ArrayList<MainGridData> myList) {
        this.myList = myList;
        this.context = mainActivity;
    }


    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public MainGridData getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MainGridViewHolder view = (MainGridViewHolder) convertView;
        if (view == null) {
            view = new MainGridViewHolder(context);
        }
        MainGridData log = getItem(i);
        //view.setLog(log);
        return view;
    }
}
