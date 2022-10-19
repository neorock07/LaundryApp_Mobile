package com.example.laundryapp.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundryapp.API.ApiRequestData;
import com.example.laundryapp.API.Koneksi_Server;
import com.example.laundryapp.Activity.Form_Pembayaran;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Baru extends RecyclerView.Adapter<Adapter_Baru.ViewHolder>{
    Context context;
    List<Model_Data> list;
    Model_Data model;

    public Adapter_Baru(Context context, List<Model_Data> list){
        this.context = context;
        this.list =  list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.container_baru, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    try{

        model = list.get(position);
    holder.tx_berat.setText(model.getBerat() + "Kg");
    holder.tx_dead.setText("Batas waktu sampai, " + FormatTime(model.getDeadline()));
    holder.tx_masuk.setText("Masuk " + FormatTime(model.getTanggal()));
    holder.tx_jenis.setText(model.getJenis());
    holder.tx_nama.setText(model.getNama());
    holder.tx_no.setText(String.valueOf(model.getId()));
    holder.tx_status.setText(model.getTransaksi());

    if(model.getNote() != null){
        holder.tx_note.setText(model.getNote());
    }else{
        holder.ln.setVisibility(View.GONE);
    }

    if(model.getTransaksi().equals("Lunas")){
        holder.st.setBackgroundColor(Color.GREEN);
    }else{
        holder.st.setBackgroundColor(Color.RED);
    }
    //move to detail
        holder.cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log = new Intent(context, Form_Pembayaran.class);
                log.putExtra("nama", model.getNama());
                log.putExtra("masuk", FormatTime(model.getTanggal()));
                log.putExtra("dead", FormatTime(model.getDeadline()));
                log.putExtra("status", model.getStatus());
                log.putExtra("trans", model.getTransaksi());
                log.putExtra("paket",model.getJenis());
                log.putExtra("note",model.getNote());
                log.putExtra("berat",model.getBerat());
                log.putExtra("id", String.valueOf(model.getId()));
                log.putExtra("alamat", model.getAlamat());
                log.putExtra("no_hp", model.getNo_hp());
                startActivity(context, log, null);

            }
        });
    //move to proses
    holder.proses.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog alert = new AlertDialog.Builder(context)
                    .setMessage("Data akan dipindahkan ke label Proses, Pastikan cucian segera diProses ya!")
                    .setIcon(R.drawable.logo)
                    .setTitle("Konfirmasi")
                    .setPositiveButton("Proses", (dialogInterface, i) -> {
                        try{
                            UpdateProses(Integer.parseInt(holder.tx_no.getText().toString()));
                        }catch (Exception e){
                            Toast.makeText(context, "Kesalahan\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Gak dulu", (dialogInterface, i) -> dialogInterface.cancel())
                    .setCancelable(false)
                    .show();

        }
    });
    //batalkan
        holder.batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = new AlertDialog.Builder(context)
                        .setMessage("Data akan dipindahkan ke label Batal.\nYakin ingin membatalkan pesanan ?")
                        .setIcon(R.drawable.logo)
                        .setTitle("Konfirmasi")
                        .setPositiveButton("Batalkan", (dialogInterface, i) -> {
                            try{
                                UpdateBatal(Integer.parseInt(holder.tx_no.getText().toString()));
                            }catch (Exception e){
                                Toast.makeText(context, "Kesalahan\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Gak dulu", (dialogInterface, i) -> dialogInterface.cancel())
                        .setCancelable(false)
                        .show();

            }
        });

    }catch (Exception e){
    Toast.makeText(context, "Kesalahan\n" + e.getMessage(), Toast.LENGTH_SHORT).show();

    }
    }

    private void UpdateProses(int id){
        ApiRequestData data = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> respon = data.UpdateProses(id);
        respon.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                Toast.makeText(context, "Memindahkan data ke Proses", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(context, "Gagal memindahkan data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateBatal(int id){
        ApiRequestData data = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> respon = data.UpdateBatal(id);
        respon.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                Toast.makeText(context, "Membatalkan Pesanan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(context, "Kesalahan\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //function to format date from database
    public String FormatTime(String time){
        String inputform = "yyyy-MM-dd";
        String output = "dd-MMM-yy";
        SimpleDateFormat inp = new SimpleDateFormat(inputform);
        SimpleDateFormat out = new SimpleDateFormat(output);

        Date date = null;
        String str = null;
        try{
            date = inp.parse(time);
            str = out.format(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return str;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tx_nama, tx_jenis, tx_dead, tx_no, tx_berat, tx_status, tx_masuk, tx_note;
        LinearLayout ln;
        Button proses,batal;
        CardView cd,st;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tx_nama = itemView.findViewById(R.id.nama_cus);
            tx_berat = itemView.findViewById(R.id.berat_brg);
            tx_dead = itemView.findViewById(R.id.dead_time);
            tx_jenis = itemView.findViewById(R.id.jenis_brg);
            tx_no = itemView.findViewById(R.id.no_cus);
            tx_masuk = itemView.findViewById(R.id.in_time);
            tx_status = itemView.findViewById(R.id.status_cus);
            tx_note = itemView.findViewById(R.id.note);
            ln = itemView.findViewById(R.id.ery);
            cd = itemView.findViewById(R.id.card_con);
            st = itemView.findViewById(R.id.card_status);
            proses = itemView.findViewById(R.id.btn_proses);
            batal = itemView.findViewById(R.id.btn_batal);
        }
    }
}
