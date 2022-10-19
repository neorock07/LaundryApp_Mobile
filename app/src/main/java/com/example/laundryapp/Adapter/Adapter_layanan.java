package com.example.laundryapp.Adapter;

import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundryapp.Model.Model_Respon_Layanan;
import com.example.laundryapp.Model.Model_layanan;
import com.example.laundryapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Adapter_layanan extends RecyclerView.Adapter<Adapter_layanan.MyViewHolder> {
    Context context;
    ArrayList<Model_layanan> list;
    Model_layanan model;

    public Adapter_layanan(Context context,ArrayList<Model_layanan> list ){
        this.context = context;
        this.list = list;
    }
    public Adapter_layanan(){

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.container_layanan, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        model = list.get(position);
        holder.id.setText(String.valueOf(model.getId()));
        holder.nama.setText(model.getNama());
        holder.harga.setText(FormatNum(String.valueOf(model.getHarga())));

        if(model.getJenis_waktu().equals("Hari")){
            holder.waktu.setText(model.getEstimasi() + " Hari");
        }else{
            holder.waktu.setText(model.getEstimasi() + " Jam");
        }

        if(model.getSatuan().equals("Kg")){
            holder.jenis.setText("Kiloan");
        }else if(model.getSatuan().equals("Pcs")){
            holder.jenis.setText("Satuan");
        }else{
            holder.jenis.setText("Meteran");
        }

    }
    public String FormatNum(String s){
        Double parsed;
        String current = "";
        String cleanString = s.toString().replaceAll("[,.]", "");
        parsed = Double.parseDouble(cleanString);
        String formatted = NumberFormat.getNumberInstance().format(parsed);
        current = formatted;
        return  current;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, nama, harga, waktu,jenis;
        CardView edit, hapus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_layanan);
            nama = itemView.findViewById(R.id.nama_layanan);
            harga = itemView.findViewById(R.id.harga);
            waktu = itemView.findViewById(R.id.waktu_dead);
            jenis = itemView.findViewById(R.id.jenis_layanan);
            edit = itemView.findViewById(R.id.btn_edit);
            hapus = itemView.findViewById(R.id.btn_hapus_layan);
        }
    }
}
