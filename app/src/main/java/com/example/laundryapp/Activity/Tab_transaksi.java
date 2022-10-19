package com.example.laundryapp.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laundryapp.API.ApiRequestData;
import com.example.laundryapp.API.Koneksi_Server;
import com.example.laundryapp.Model.Model_Total;
import com.example.laundryapp.R;
import com.example.laundryapp.Adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tab_transaksi extends Fragment {

    private TabLayout tabLayout;
    private ViewPager pager;
    private TabAdapter adapter;
    private String baru, proses, selesai, ambil,batal;
    public Tab_transaksi() {
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab_transaksi, container, false);
        tabLayout = v.findViewById(R.id.tab_tran);
        pager = v.findViewById(R.id.v_pager);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.AddFragment(new tab_bar(),"Baru");
        adapter.AddFragment(new Tab_Proses(), "Proses");
        adapter.AddFragment(new tab_selesai(),"Selesai");
        adapter.AddFragment(new tab_ambil(),"Diambil");
        adapter.AddFragment(new tab_batal(),"Batal");
//nama tab layout
//        getJmlBaru();
//        getJmlAmbil();
//        getJmlBatal();
//        getJmlProses();
//        getJmlSelesai();
        pager.setAdapter(adapter);
////        //connecto tabL with vpager
        tabLayout.setupWithViewPager(pager);

        return v;
    }


    private String getJmlBaru(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlBaru();
        final int total;
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(@NonNull Call<Model_Total> call, Response<Model_Total> response) {
                baru = response.body().getData();
                adapter.AddFragment(new tab_bar(),"Baru (" + baru + ")");
                pager.setAdapter(adapter);
//        //connecto tabL with vpager
                tabLayout.setupWithViewPager(pager);
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return baru;
    }

    private String getJmlProses(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlProses();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                proses = response.body().getData();
                adapter.AddFragment(new Tab_Proses(), "Proses (" + proses + ")");
                pager.setAdapter(adapter);
//        //connecto tabL with vpager
                tabLayout.setupWithViewPager(pager);
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return proses;
    }
    private String getJmlSelesai(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlSelesai();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(@NonNull Call<Model_Total> call, Response<Model_Total> response) {
                selesai = response.body().getData();
                adapter.AddFragment(new tab_selesai(),"Selesai (" + selesai + ")");
                pager.setAdapter(adapter);
//        //connecto tabL with vpager
                tabLayout.setupWithViewPager(pager);
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return selesai;
    }
    private String getJmlAmbil(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlAmbil();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                ambil = response.body().getData();
                adapter.AddFragment(new tab_ambil(),"Diambil (" + ambil + ")");
                pager.setAdapter(adapter);
//        //connecto tabL with vpager
                tabLayout.setupWithViewPager(pager);
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return ambil;
    }
    private String getJmlBatal(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlBatal();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                batal = response.body().getData();
                adapter.AddFragment(new tab_batal(),"Batal (" + batal + ")");
                pager.setAdapter(adapter);
//        //connecto tabL with vpager
                tabLayout.setupWithViewPager(pager);
                           }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return batal;
    }
}