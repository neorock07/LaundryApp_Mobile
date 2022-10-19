package com.example.laundryapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.laundryapp.Adapter.Adapter_dead;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Deadline_activity extends AppCompatActivity {

    private RecyclerView rc;
    private SwipeRefreshLayout swp;
    private LinearLayout ln;
    private List<Model_Data> list;
    private Adapter_dead adapter;
    private ProgressBar bar;
    private Toolbar tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline);
        tool = findViewById(R.id.toolbar_dead);
        rc = findViewById(R.id.rc_dead);
        swp = findViewById(R.id.swipe_dead);
        ln = findViewById(R.id.ln_load_dead);
        bar = findViewById(R.id.prg_dead);
        //recycle view
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //swipe layout
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swp.setRefreshing(true);
                RetrieveDeadline();
                swp.setRefreshing(false);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            RetrieveDeadline();
        }catch (Exception e){
            Toast.makeText(this, "Kesalahan\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void RetrieveDeadline(){
        bar.setVisibility(View.VISIBLE);
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> respon = api.getListDead();
        respon.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                if(response.body().getKode() == 0){
                    ln.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                    rc.setVisibility(View.GONE);
                }else{
                    list = response.body().getData();
                    adapter = new Adapter_dead(getApplicationContext(), list);
                    adapter.notifyDataSetChanged();
                    rc.setAdapter(adapter);
                    bar.setVisibility(View.INVISIBLE);
                    ln.setVisibility(View.GONE);
                    Toast.makeText(Deadline_activity.this, "Terhubung | kode : " +response.body().getKode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(Deadline_activity.this, "Kesalahan\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}