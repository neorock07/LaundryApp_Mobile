package com.example.laundryapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundryapp.API.ApiRequestData;
import com.example.laundryapp.API.Koneksi_Server;
import com.example.laundryapp.Adapter.Adapter_Baru;
import com.example.laundryapp.Adapter.Adapter_layanan;
import com.example.laundryapp.Model.Model_Data;
import com.example.laundryapp.Model.Model_Income;
import com.example.laundryapp.Model.Model_Respon_Layanan;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.Model.Model_Total;
import com.example.laundryapp.Model.Model_layanan;
import com.example.laundryapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tab_default extends Fragment {


    private Button scan, detail_tran, print;
    private int day, year, jam;
    private int data_baru;
    private TextView greet, bru, sls,jml_baru,jml_proses,jml_sls,jml_ambil, jml_batal,tran_today,jml_dead, tv_uang, tv_jmlTran, tv_harga;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private Button btn_print, tambah_tran, view_all;
    private CardView cardDead;
    private SwipeRefreshLayout swp;
    private AutoCompleteTextView autDp, dropStatus;
    private TextInputEditText ed_nama, ed_note, ed_wa, ed_address;
    private ArrayAdapter<String> adapter, adapter2;
    private String[] arr = {"Paket Gembel(Cuci doang Rp.5000/kg)", "Paket Rapi(10000/kg)","Paket Rapi-Amat(15000/kg)"};
    private String[] arr_st = {"Baru", "Proses","Selesai","Diambil", "Batal"};
    private TextInputEditText ed_berat;
    private ImageView add, sub;
    double i = 1;
    private Model_layanan model;
    private ArrayList<Model_layanan> list_layanan = new ArrayList<>();
    private ArrayList<Model_layanan> harga_layanan = new ArrayList<>();
    private ArrayAdapter<String> adapter3;
    private Adapter_layanan adpLayanan = new Adapter_layanan();
    private ArrayList<String> data_layanan = new ArrayList<>();
    private ArrayList<String> data_harga = new ArrayList<>();
    String berat;

    public Tab_default() {
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab_default, container, false);
        scan =  v.findViewById(R.id.scanner_btn);
        print = v.findViewById(R.id.print_btn);
        greet = v.findViewById(R.id.greeting);
        tambah_tran = v.findViewById(R.id.tambah_tran);
        jml_baru = v.findViewById(R.id.num_baru);
        jml_proses = v.findViewById(R.id.num_proses);
        jml_sls = v.findViewById(R.id.num_selesai);
        jml_ambil = v.findViewById(R.id.num_ambil);
        jml_batal = v.findViewById(R.id.num_batal);
        swp = v.findViewById(R.id.swp_deflaut);
        detail_tran = v.findViewById(R.id.btn_tran);
        tran_today = v.findViewById(R.id.trans_today);
        jml_dead = v.findViewById(R.id.jml_dead);
        view_all = v.findViewById(R.id.view_all);
        tv_uang = v.findViewById(R.id.revenue);
        cardDead = v.findViewById(R.id.card_dead);
        tv_jmlTran = v.findViewById(R.id.num_transaksi);
        list_layanan = new ArrayList<>();


        //go to transaksi
        detail_tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new Tab_transaksi());
            }
        });
        //go to dead list
        cardDead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Deadline_activity.class));
            }
        });
        //coba
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Form_Pembayaran.class));
            }
        });

        //greetings
        Calendar calendar = Calendar.getInstance();
        jam = calendar.get(Calendar.HOUR_OF_DAY);
        if(jam > 5 && jam < 9){
            greet.setText("Selamat Pagi");
        }else if(jam >= 9 && jam <= 15){
            greet.setText("Selamat Siang");
        }else{
            greet.setText("Selamat Malam");
        }
        //get income data

        //refreshing data
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swp.setRefreshing(true);
                getJmlBaru();
                getJmlProses();
                getJmlSelesai();
                getJmlAmbil();
                getJmlBatal();
                getIncome();
                swp.setRefreshing(false);
            }
        });

        //button to print act
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Scanner.class));
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),Printer.class));
            }
        });

        //bottomsheet
        tambah_tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomsheetdialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(
                        R.layout.bottom_transaksi, v.findViewById(R.id.btm_form));
                ed_nama = bottomSheetView.findViewById(R.id.et_name);
                ed_note = bottomSheetView.findViewById(R.id.et_note);
                ed_berat = bottomSheetView.findViewById(R.id.et_berat);
                dropStatus = bottomSheetView.findViewById(R.id.et_status);
                autDp = bottomSheetView.findViewById(R.id.et_jenis);
                ed_address = bottomSheetView.findViewById(R.id.et_alamat);
                ed_wa = bottomSheetView.findViewById(R.id.et_wa);
                tv_harga = bottomSheetView.findViewById(R.id.tx_harga);
                CardView tambah_tran = bottomSheetView.findViewById(R.id.btn_tambah_tran);

                //Dropdown menu paket
                //get data paket layanan from api
                getLayanan();
                //clear array to avoid duplicated data
                data_layanan.clear();
                //Dropdown menu status
                adapter2 = new ArrayAdapter<String>(getContext(), R.layout.dropdown_paket, arr_st);
                dropStatus.setAdapter(adapter2);
                dropStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String items = adapterView.getItemAtPosition(i).toString();
                    }
                });
                //berat
                add = bottomSheetView.findViewById(R.id.btn_add);
                sub = bottomSheetView.findViewById(R.id.btn_sub);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        i += 0.5;
                        ed_berat.setText(i + " Kg");
                        int money = (int) (i * Integer.parseInt(data_harga.get(0)));
                        tv_harga.setText("Rp " + adpLayanan.FormatNum(String.valueOf(money)));
                    }
                });
                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        i -= 0.5;
                        if(i <= 1){
                            ed_berat.setText(1 + " Kg");
                            tv_harga.setText("Rp " + adpLayanan.FormatNum(data_harga.get(0)));

                        }else{
                            ed_berat.setText(i + " Kg");
                            int money = (int) (i * Integer.parseInt(data_harga.get(0)));
                            tv_harga.setText("Rp " + adpLayanan.FormatNum(String.valueOf(money)));

                        }

                    }
                });

                tambah_tran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ed_nama.getText().toString().isEmpty() || ed_berat.getText().toString().isEmpty()
                                || autDp.getText().toString().isEmpty() || dropStatus.getText().toString().isEmpty()
                        ){
                            ed_nama.setError("tidak boleh kosong!");
                            ed_berat.setError("tidak boleh kosong!");
                            autDp.setError("tidak boleh kosong");
                            dropStatus.setError("tidak boleh kosong!");
                        }else{
                            String brt = ed_berat.getText().toString();

                            String jenis = autDp.getText().toString();
                            String status = dropStatus.getText().toString();
                            String nama = ed_nama.getText().toString();
                            String note = ed_note.getText().toString();
                            String no_wa = ed_wa.getText().toString();
                            String alamat = ed_address.getText().toString();
                            String tran = "Belum Lunas";
                            Calendar cal = Calendar.getInstance();

                            int year = cal.get(Calendar.YEAR);
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            int month = cal.get(Calendar.MONTH);
                            String date_masuk = year + "-" + "0"+(month+1) +"-" + day;
                            String date_dead = year + "-" + "0"+(month+1) +"-" + (day + 2);

                            char[] arr = brt.toCharArray();
                            StringBuilder sb = new StringBuilder();
                            for(int i=0; i<arr.length;i++){
                                if(arr[i] == ' '){
                                    break;
                                }
                                sb.append(arr[i]);
                            }
                            berat = sb.toString();
                            try{
                                InsertDataBaru(berat,jenis, nama, date_masuk,date_dead,tran,status,note, alamat,no_wa);
                                swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                    @Override
                                    public void onRefresh() {
                                        swp.setRefreshing(true);
                                        getJmlBaru();
                                        getJmlProses();
                                        getJmlSelesai();
                                        getJmlAmbil();
                                        getJmlBatal();
                                        getJmlTranToday();
                                        getJmlDead();
                                        getTotalTran();
                                        swp.setRefreshing(false);
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Gagal Menyimpan \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });


        return v;
    }

    private void InsertDataBaru(String berat, String jenis, String nama, String tgl, String dead, String transasksi, String status, String note, String alamat, String no_hp ){
        ApiRequestData apidata = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Response> respon = apidata.InsertData(jenis,berat,note,status,transasksi, nama,tgl,dead,alamat, no_hp);
        respon.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                Toast.makeText(getContext(), "Menyimpan Data | Kode : " + response.body().getKode(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Erorr : \n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            getJmlBaru();
            getJmlProses();
            getJmlSelesai();
            getJmlAmbil();
            getJmlBatal();
            getJmlTranToday();
            getJmlDead();
            getIncome();
            getTotalTran();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //function to retrieve baru label
    private void getJmlBaru(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlBaru();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                jml_baru.setText(response.body().getData());
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
}
    //function to retrieve proses label
    private void getJmlProses(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlProses();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                jml_proses.setText(response.body().getData());
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //function to retrieve selesai label
    private void getJmlSelesai(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlSelesai();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                jml_sls.setText(response.body().getData());
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //function to retrieve ambil label
    private void getJmlAmbil(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlAmbil();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                jml_ambil.setText(response.body().getData());
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //function to retrieve batal label
    private void getJmlBatal(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlBatal();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                jml_batal.setText(response.body().getData());
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //function to retrieve total transaksi today
    private void getJmlTranToday(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlTransToday();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                tran_today.setText( "+"+ response.body().getData());
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //function to retrieve deadline list
    private void getJmlDead(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> respon = api.getJmlDead();
        respon.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {

                if(response.body().getData().equals("0")){
                    jml_dead.setText("Tidak ada");
                }else{
                    jml_dead.setText( response.body().getData());
                }
            }
            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //function to retrieve income total
    private void getIncome(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Income> respon = api.getIncome();
        respon.enqueue(new Callback<Model_Income>() {
            @Override
            public void onResponse(Call<Model_Income> call, Response<Model_Income> response) {

                if(response.body().getData() == 0){
                    tv_uang.setText("0");
                }else{
                    NumberFormat num = NumberFormat.getNumberInstance();
                    String angka = num.format(response.body().getData());
                    tv_uang.setText( angka);
                }
            }
            @Override
            public void onFailure(Call<Model_Income> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal parsing data..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //function to retrieve total transaksi had been saved
    private void getTotalTran(){
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Total> data = api.getJmlTran();
        data.enqueue(new Callback<Model_Total>() {
            @Override
            public void onResponse(Call<Model_Total> call, Response<Model_Total> response) {
                tv_jmlTran.setText(String.valueOf(response.body().getData()) + " Transaksi");
            }

            @Override
            public void onFailure(Call<Model_Total> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Parsing data\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

///function to get data layanan
    public void getLayanan(){
        adpLayanan = new Adapter_layanan();
        ApiRequestData api = Koneksi_Server.koneksi().create(ApiRequestData.class);
        Call<Model_Respon_Layanan> responModel = api.RetrieveLayanan();
        responModel.enqueue(new Callback<Model_Respon_Layanan>() {
            @Override
            public void onResponse(Call<Model_Respon_Layanan> call, Response<Model_Respon_Layanan> response) {
                list_layanan = response.body().getData();
                for(int i=0;i<list_layanan.size();i++){
                    model = list_layanan.get(i);
                    data_layanan.add(model.getNama());
                    data_harga.add(model.getHarga());
                }
                adapter3 = new ArrayAdapter<String>(getContext(), R.layout.dropdown_paket,data_layanan);
                autDp.setAdapter(adapter3);
                autDp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tv_harga.setText("Rp " + adpLayanan.FormatNum(data_harga.get(i)));
                    }
                });

            }

            @Override
            public void onFailure(Call<Model_Respon_Layanan> call, Throwable t) {

            }
        });

    }


    //function to set fragment
    private void setFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null).replace(R.id.frame_lay,fragment )
                .commit();

    }
}