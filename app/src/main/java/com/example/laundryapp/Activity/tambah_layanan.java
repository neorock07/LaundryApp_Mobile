package com.example.laundryapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.laundryapp.API.ApiRequestData;
import com.example.laundryapp.API.Koneksi_Server;
import com.example.laundryapp.Adapter.Adapter_layanan;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Respon_Layanan;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.Model.Model_layanan;
import com.example.laundryapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tambah_layanan extends AppCompatActivity {

    private RecyclerView rc;
    private ArrayList<Model_layanan> list;
    private SwipeRefreshLayout swp;
    private ProgressBar bar;
    private LinearLayout ln;
    Adapter_layanan adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_layanan);
        //inisialisasi
        rc = findViewById(R.id.rc_layanan);
        swp = findViewById(R.id.swp_layanan);
        bar = findViewById(R.id.prg_layanan);
        ln = findViewById(R.id.ln_layanan);
        //rc
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc.setVisibility(View.VISIBLE);
        //swipe
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swp.setRefreshing(true);
                RetrieveLayanan();
                swp.setRefreshing(false);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            RetrieveLayanan();
        }catch (Exception e){
            Toast.makeText(this, "Kesalahan\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void RetrieveLayanan(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Respon_Layanan> respon = api.RetLayanan();
        respon.enqueue(new Callback<Model_Respon_Layanan>() {
           @Override
           public void onResponse(Call<Model_Respon_Layanan> call, Response<Model_Respon_Layanan> response) {

               if(response.body().getKode() == 0){
                   ln.setVisibility(View.VISIBLE);
               }else{
                   list = response.body().getData();
                   adapter = new Adapter_layanan(tambah_layanan.this, list);
                   rc.setAdapter(adapter);
                   adapter.notifyDataSetChanged();
                   bar.setVisibility(View.INVISIBLE);
                   ln.setVisibility(View.GONE);
                   Toast.makeText(tambah_layanan.this, "Terhubung | kode : " + response.body().getKode(), Toast.LENGTH_SHORT).show();
               }
           }
           @Override
           public void onFailure(Call<Model_Respon_Layanan> call, Throwable t) {
               Toast.makeText(tambah_layanan.this, "Gagal Parsing data..\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

}