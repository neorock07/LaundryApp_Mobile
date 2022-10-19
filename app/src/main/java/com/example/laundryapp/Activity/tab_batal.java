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
import com.example.laundryapp.Adapter.Adapter_Batal;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class tab_batal extends Fragment {

    private RecyclerView rc;
    private Adapter_Batal adapter;
    private List<Model_Data> data;
    private SwipeRefreshLayout swp;
    private ProgressBar bar;
    private LinearLayout ln;

    public tab_batal() {
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab_batal, container, false);
        swp = v.findViewById(R.id.swipe_batal);
        bar = v.findViewById(R.id.prg_batal);
        ln = v.findViewById(R.id.ln_load);
        rc = v.findViewById(R.id.rc_batal);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        try{
            swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swp.setRefreshing(true);
                    RetrieveBatal();
                    swp.setRefreshing(false);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(),"Kesalahan\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            RetrieveBatal();
        }catch (Exception e){
            Toast.makeText(getContext(), "Kesalahan \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //function to get data through retrofit
    public void RetrieveBatal(){
        bar.setVisibility(View.VISIBLE);
        ApiRequestData apiData = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> respon = apiData.RetBatal();
        respon.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {

                if(response.body().getKode() == 0){
                    bar.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                    ln.setVisibility(View.VISIBLE);
                    rc.setVisibility(View.GONE);
                }else{
                    data = response.body().getData();
                    adapter = new Adapter_Batal(getContext(), data);
                    rc.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Terhubung | kode : " + response.body().getKode(), Toast.LENGTH_SHORT).show();
                    ln.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Jaringan\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}