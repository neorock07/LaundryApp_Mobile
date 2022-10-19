package com.example.laundryapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Batal extends RecyclerView.Adapter<Adapter_Batal.MyViewHolder> {
    Context context;
    List<Model_Data> data;

    public Adapter_Batal(Context context, List<Model_Data> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public Adapter_Batal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.container_batal, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Batal.MyViewHolder holder, int position) {
        try{
            Model_Data model = data.get(position);
            holder.tx_berat.setText(model.getBerat() + "Kg");
            holder.tx_dead.setText(model.getTanggal());
            holder.tx_masuk.setText(model.getTanggal());
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
                holder.cd.setBackgroundColor(Color.GREEN);
            }else{
                holder.cd.setBackgroundColor(Color.RED);
            }

            //tombol hapus data
            holder.hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alert = new AlertDialog.Builder(context)
                            .setTitle("Konfirmasi")
                            .setIcon(R.drawable.logo)
                            .setCancelable(false)
                            .setMessage("Data ini akan dihapus dari label Diambil\nYakin ingin hapus data ?")
                            .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try{
                                        DeleteData(Integer.parseInt(holder.tx_no.getText().toString()));
                                    }catch (Exception e){
                                        Toast.makeText(context, "Kesalahan\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Gak Dulu", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void DeleteData(int id){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> data = api.DeleteData(id);
        data.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(context, "Kesalahan Parsing data...\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tx_nama, tx_jenis, tx_dead, tx_no, tx_berat, tx_status, tx_masuk,tx_note;
        CardView cd;
        LinearLayout ln;
        Button hapus;
        public MyViewHolder(@NonNull View itemView) {
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
            cd = itemView.findViewById(R.id.card_status);
            hapus = itemView.findViewById(R.id.btn_hapus);
        }
    }
}
