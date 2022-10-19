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
import com.example.laundryapp.Adapter.Adapter_Proses;
import com.example.laundryapp.Adapter.Adapter_dead;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Frag_deadline extends Fragment {

    private RecyclerView rc;
    private SwipeRefreshLayout swp;
    private ProgressBar bar;
    private Adapter_dead adapter;
    private List<Model_Data> dataList;
    private LinearLayout ln;

    public Frag_deadline() {
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_frag_deadline, container, false);
        rc = v.findViewById(R.id.rc_deadline);
        swp = v.findViewById(R.id.swipe_proses);
        bar = v.findViewById(R.id.prg_proses);
        ln = v.findViewById(R.id.ln_load);

        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //refresh
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swp.setRefreshing(true);
                try{
                    RetrieveDead();
                }catch (Exception e){
                    e.printStackTrace();
                }
                swp.setRefreshing(false);
            }
        });
        return v;
    }
    //function to get data proses through retrofit
    public void RetrieveDead(){
        bar.setVisibility(View.VISIBLE);
        ApiRequestData ap = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> resp = ap.getListDead();
        resp.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {

                if(response.body().getKode() == 0){
                    ln.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                    rc.setVisibility(View.GONE);
                }else{
                    dataList = response.body().getData();
                    adapter = new Adapter_dead(getContext(), dataList);
                    adapter.notifyDataSetChanged();
                    rc.setAdapter(adapter);
                    Toast.makeText(getContext(), "Terhubung | kode : " + response.body().getKode(), Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.INVISIBLE);
                    ln.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Jaringan \n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}