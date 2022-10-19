package com.example.laundryapp.Activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.laundryapp.API.ApiRequestData;
import com.example.laundryapp.API.Koneksi_Server;
import com.example.laundryapp.Adapter.Adapter_Baru;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.R;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class tab_bar extends Fragment {

    private RecyclerView rc;
    List<Model_Data> list;
    private Adapter_Baru adapter;
    private ProgressBar prg;
    private SwipeRefreshLayout swipe;
    LinearLayout ln;
    public tab_bar() {
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab_bar, container, false);
        prg = v.findViewById(R.id.prg_baru);
        swipe = v.findViewById(R.id.swipe_data_baru);
        ln = v.findViewById(R.id.ln_load);
        //recycle view
        rc = v.findViewById(R.id.rc_baru);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false ));
        try{
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipe.setRefreshing(true);
                    RetrieveDataRetro();
                    swipe.setRefreshing(false);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "Kesalahan \n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            RetrieveDataRetro();
        }catch (Exception e){
            Toast.makeText(getContext(), "Kesalahan \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //function to get data with retrofit
    public void RetrieveDataRetro(){
        prg.setVisibility(View.VISIBLE);
        ApiRequestData apiData = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> responseCall = apiData.RetrieveData();
        responseCall.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {

                if(response.body().getKode() == 0){
                    Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                    ln.setVisibility(View.VISIBLE);
                    rc.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getContext(), "Berhasil Terhubung | Kode : " + response.body().getKode(), Toast.LENGTH_SHORT).show();
                    list = response.body().getData();
                    adapter = new Adapter_Baru(getContext(), list);
                    rc.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    prg.setVisibility(View.INVISIBLE);
                    ln.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Jaringan | Error : \n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}