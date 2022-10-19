package com.example.laundryapp.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_dead extends RecyclerView.Adapter<Adapter_dead.MyViewHolder> {
    Context context;
    List<Model_Data> data;

    public Adapter_dead(Context context, List<Model_Data> data){
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public Adapter_dead.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.container_proses, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_dead.MyViewHolder holder, int position) {
        try{
            Model_Data model = data.get(position);
            holder.tx_berat.setText(model.getBerat() + "Kg");
            holder.tx_dead.setText(model.getDeadline()+" (Hari ini)");
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
                holder.st.setBackgroundColor(Color.GREEN);
            }else{
                holder.st.setBackgroundColor(Color.RED);
            }
            //move to detail
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

            holder.selesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alert = new AlertDialog.Builder(context)
                            .setMessage("Data akan dipindahkan ke label Selesai, Pastikan cucian sudah Selesai ya!")
                            .setIcon(R.drawable.logo)
                            .setTitle("Konfirmasi")
                            .setPositiveButton("Selesaikan", (dialogInterface, i) -> {
                                try{
                                    UpdateSelesai(Integer.parseInt(holder.tx_no.getText().toString()));
                                }catch (Exception e){
                                    Toast.makeText(context, "Kesalahan\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Gak dulu", (dialogInterface, i) -> dialogInterface.cancel())
                            .setCancelable(false)
                            .show();

                }
            });
            holder.batal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alert = new AlertDialog.Builder(context)
                            .setMessage("Data akan dipindahkan ke label Batal\nYakin ingin Batalkan Pesanan ?")
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
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    private void UpdateSelesai(int id){
        ApiRequestData data = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> respon = data.UpdateSelesai(id);
        respon.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                Toast.makeText(context, "Memindahkan data ke Selesai", Toast.LENGTH_SHORT).show();
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
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tx_nama, tx_jenis, tx_dead, tx_no, tx_berat, tx_status, tx_masuk, tx_note;
        CardView cd, st;
        LinearLayout ln;
        Button selesai,batal;

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
            selesai = itemView.findViewById(R.id.btn_proses);
            cd = itemView.findViewById(R.id.card_con);
            st = itemView.findViewById(R.id.card_status);
            batal = itemView.findViewById(R.id.btn_batal);

        }
    }
}
