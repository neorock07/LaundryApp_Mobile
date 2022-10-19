package com.example.laundryapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundryapp.API.ApiRequestData;
import com.example.laundryapp.API.Koneksi_Server;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Form_Pembayaran extends AppCompatActivity {

    private TextView tv_nama, tv_status, tv_trans, tv_id, tv_note, tv_berat, tv_paket, tv_masuk, tv_dead, tv_bayar, tv_alamat, tv_wa;
    private Switch btn_switch;
    private LinearLayout ln, ln_note;
    private Button btn_proses, btn_nota;
    private CircleImageView circle;
    private ImageView btn_ok;
    private CardView cd_wa;
    private double parsed;
    private TextInputEditText ed_bayar;
    CardView btn_cetak, btn_tagih;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pembayaran);

        cd_wa = findViewById(R.id.call_wa);
        btn_switch = findViewById(R.id.btn_deliver);
        tv_nama = findViewById(R.id.tx_nama);
        tv_berat = findViewById(R.id.tx_berat);
        tv_status = findViewById(R.id.tx_status);
        tv_trans = findViewById(R.id.tx_trans);
        tv_id = findViewById(R.id.tx_nomer);
        tv_note = findViewById(R.id.tx_note);
        tv_paket = findViewById(R.id.tx_paket);
        tv_masuk = findViewById(R.id.tx_waktu);
        tv_dead = findViewById(R.id.tx_batas);
        ln = findViewById(R.id.ln_ongkir);
        btn_proses = findViewById(R.id.btn_proses);
        btn_ok = findViewById(R.id.ok);
        tv_bayar = findViewById(R.id.tx_bayar);
        ed_bayar = findViewById(R.id.ed_bayar);
        circle = findViewById(R.id.btn_balik);
        tv_alamat = findViewById(R.id.tx_alamat);
        tv_wa = findViewById(R.id.tx_wa);
        ln_note = findViewById(R.id.ln_note);
        btn_nota = findViewById(R.id.nota_btn);

        try{
            String nama = getIntent().getStringExtra("nama");
            String status = getIntent().getStringExtra("status");
            String berat = getIntent().getStringExtra("berat");
            String trans = getIntent().getStringExtra("trans");
            String id  = getIntent().getStringExtra("id");
            String note = getIntent().getStringExtra("note");
            String masuk = getIntent().getStringExtra("masuk");
            String dead = getIntent().getStringExtra("dead");
            String paket = getIntent().getStringExtra("paket");
            String alamat = getIntent().getStringExtra("alamat");
            String no_hp = getIntent().getStringExtra("no_hp");

            tv_nama.setText(nama);
            tv_berat.setText(berat);
            tv_status.setText(status);

            if(alamat == null){
                tv_alamat.setText("-");
            }else{
                tv_alamat.setText(alamat);
            }
            if(no_hp == null){
                tv_wa.setText("-");
            }else{
                tv_wa.setText(no_hp);
            }
            if(note == null){
                ln_note.setVisibility(View.GONE);
            }else{
                tv_note.setText(note);
            }

            if(trans.equals("Lunas")){
                tv_trans.setText(trans);
                tv_trans.setTextColor(Color.GREEN);
            }else{
                tv_trans.setText(trans);
                tv_trans.setTextColor(Color.RED);
            }

            ed_bayar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    String current = "";
                    if(!s.toString().equals(current)){
                         ed_bayar.removeTextChangedListener(this);
                        String cleanString = s.toString().replaceAll("[,.]", "");
                        parsed = Double.parseDouble(cleanString);
                        String formatted = NumberFormat.getNumberInstance().format(parsed);
                        current = formatted;
                        ed_bayar.setText(formatted);
                        ed_bayar.setSelection(formatted.length());
                        ed_bayar.addTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            tv_id.setText(id);
            tv_paket.setText(paket);
            tv_masuk.setText(masuk);
            tv_dead.setText(dead);

        }catch (Exception e){
            e.printStackTrace();
        }

        btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(btn_switch.isChecked()){
                        ln.setVisibility(View.VISIBLE);
                }else{
                    ln.setVisibility(View.GONE);
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(ed_bayar.getText().toString().isEmpty()){
                   ed_bayar.setError("Mohon masukkan data yang benar!");
               }else{
                   String uang = ed_bayar.getText().toString();
                   tv_bayar.setText("Rp. "+uang);
               }
            }
        });

        cd_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    Intent send = new Intent(Intent.ACTION_SEND);
                    send.setType("text/plain");
                    send.putExtra(Intent.EXTRA_SUBJECT,"Bagikan lewat");
                    send.putExtra(Intent.EXTRA_TEXT,"Hai cucian mu baru dicuci ya\nini tagihan mu");
                    startActivity(Intent.createChooser(send,"Bagikan lewat"));

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Form_Pembayaran.this, MainActivity.class));
                finish();
            }
        });

        btn_proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog alert = new AlertDialog.Builder(Form_Pembayaran.this)
                        .setMessage("Data akan dipindahkan ke label Proses, Pastikan cucian segera diProses ya!")
                        .setIcon(R.drawable.logo)
                        .setTitle("Konfirmasi")
                        .setPositiveButton("Proses", (dialogInterface, i) -> {
                            try{
                                UpdateProses(Integer.parseInt(tv_id.getText().toString()));
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(), "Kesalahan\n"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Gak dulu", (dialogInterface, i) -> dialogInterface.cancel())
                        .setCancelable(false)
                        .show();

            }
        });

        //btm sheet for option nota
        btn_nota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final BottomSheetDialog btmDialog = new BottomSheetDialog(Form_Pembayaran.this, R.style.BottomsheetdialogTheme);
                View btmView = LayoutInflater.from(Form_Pembayaran.this).inflate(R.layout.container_nota, findViewById(R.id.form_nota));
                btn_cetak = btmView.findViewById(R.id.btn_cetak_nota);
                btn_tagih = btmView.findViewById(R.id.btn_tagih);

                btmDialog.setContentView(btmView);
                btmDialog.show();
            }
        });


    }

    private void UpdateProses(int id){
        ApiRequestData data = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> respon = data.UpdateProses(id);
        respon.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                Toast.makeText(getApplicationContext(), "Memindahkan data ke Proses", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal memindahkan data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateBatal(int id){
        ApiRequestData data = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> respon = data.UpdateBatal(id);
        respon.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                Toast.makeText(getApplicationContext(), "Membatalkan Pesanan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Kesalahan\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}