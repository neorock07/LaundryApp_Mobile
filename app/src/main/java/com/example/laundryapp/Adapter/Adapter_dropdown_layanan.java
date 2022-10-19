package com.example.laundryapp.Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.laundryapp.Model.Model_layanan;
import com.example.laundryapp.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_dropdown_layanan {
    Context context;
    ArrayList<Model_layanan> list;
    ArrayAdapter<String> arrayAdapter;
    Model_layanan model;

    Adapter_dropdown_layanan(Context context, ArrayList<Model_layanan> list){
        this.context = context;
        this.list = list;
    }




}
