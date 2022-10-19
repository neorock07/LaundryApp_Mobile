package com.example.laundryapp.Activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laundryapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class Tab_uang extends Fragment {
    private BarChart bar;
    private ArrayList<BarEntry> baru;
    private Legend legend;
    private BarDataSet br_baru;
    public Tab_uang() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab_uang, container,false);
        bar = v.findViewById(R.id.bar_tran);
        bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        legend = new Legend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setDrawInside(true);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setEnabled(true);

        float value = 2f;
        baru = new ArrayList<>();
        for(float i = 0; i< 8; i++){
            baru.add(new BarEntry(i, value+=1));
        }

        br_baru = new BarDataSet(baru, "Baru");
        br_baru.addColor(Color.GREEN);
        bar.animateXY(200, 300);
        bar.setData(new BarData(br_baru));


        return v;
    }
}