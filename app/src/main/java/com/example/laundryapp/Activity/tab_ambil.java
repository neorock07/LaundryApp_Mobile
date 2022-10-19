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
import com.example.laundryapp.Adapter.Adapter_Ambil;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class tab_ambil extends Fragment {

    private RecyclerView rc;
    private Adapter_Ambil adapter;
    private SwipeRefreshLayout swp;
    private ProgressBar bar;
    private List<Model_Data> daftar;
    private LinearLayout ln;
    public tab_ambil() {
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab_ambil, container, false);
        rc = v.findViewById(R.id.rc_ambil);
        swp = v.findViewById(R.id.swp_ambil);
        bar = v.findViewById(R.id.prg_ambil);
        ln = v.findViewById(R.id.ln_load);

        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    swp.setRefreshing(true);
                    RetrieveDiambil();
                    swp.setRefreshing(false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            RetrieveDiambil();
        }catch (Exception e){
            Toast.makeText(getContext(), "Kesalahan \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //function get data diambil through retrofit
    public void RetrieveDiambil(){
        bar.setVisibility(View.VISIBLE);
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> response = api.RetAmbil();
        response.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {

                if(response.body().getKode() == 0){
                    ln.setVisibility(View.VISIBLE);
                    rc.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                }else{
                    daftar = response.body().getData();
                    adapter = new Adapter_Ambil(getContext(),daftar);
                    rc.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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