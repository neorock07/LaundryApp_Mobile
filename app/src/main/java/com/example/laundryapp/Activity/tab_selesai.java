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

import com.airbnb.lottie.L;
import com.example.laundryapp.API.ApiRequestData;
import com.example.laundryapp.API.Koneksi_Server;
import com.example.laundryapp.Adapter.Adapter_Selesai;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tab_selesai extends Fragment {

    private RecyclerView rc;
    private SwipeRefreshLayout swp;
    private ProgressBar prg;
    private Adapter_Selesai adapter;
    private List<Model_Data> list_data;
    private LinearLayout ln;

    public tab_selesai() {
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab_selesai,container, false);
        rc = v.findViewById(R.id.rc_sls);
        swp = v.findViewById(R.id.swp_sls);
        prg = v.findViewById(R.id.prg_sls);
        ln = v.findViewById(R.id.ln_load);
        //recycle view
        rc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rc.setHasFixedSize(true);

        try{
            swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swp.setRefreshing(true);
                    RetrieveDataSelesai();
                    swp.setRefreshing(false);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "Warning!: \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            RetrieveDataSelesai();
        }catch (Exception e){
            Toast.makeText(getContext(), "Erorr \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //function to get data selesai through retrofit
    public void RetrieveDataSelesai(){
        prg.setVisibility(View.VISIBLE);
        ApiRequestData dataSls = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> responseSLS = dataSls.RetSelesai();
        responseSLS.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {

                if(response.body().getKode() == 0){
                    prg.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                    ln.setVisibility(View.VISIBLE);
                    rc.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getContext(), "Terhubung | kode : " + response.body().getKode(), Toast.LENGTH_SHORT).show();
                    list_data = response.body().getData();
                    adapter = new Adapter_Selesai(getContext(), list_data);
                    rc.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    prg.setVisibility(View.INVISIBLE);
                    ln.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan \n " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}