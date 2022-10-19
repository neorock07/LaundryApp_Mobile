package com.example.laundryapp.API;

import com.example.laundryapp.Model.Model_Income;
import com.example.laundryapp.Model.Model_Respon_Layanan;
import com.example.laundryapp.Model.Model_Response;
import com.example.laundryapp.Model.Model_Total;
import com.example.laundryapp.Model.Model_layanan;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiRequestData {
    @GET("retrieve.php")
    Call<Model_Response> RetrieveData();
    @GET("ret_selesai.php")
    Call<Model_Response> RetSelesai();
    @GET("ret_ambil.php")
    Call<Model_Response> RetAmbil();
    @GET("ret_batal.php")
    Call<Model_Response> RetBatal();
    @GET("ret_proses.php")
    Call<Model_Response> RetProses();
    @FormUrlEncoded
    @POST("insert_terima.php")
    Call<Model_Response> InsertData(
            @Field("jenis") String jenis,
            @Field("berat") String berat,
            @Field("note") String note,
            @Field("status") String status,
            @Field("transaksi") String transaksi,
            @Field("nama") String nama,
            @Field("tanggal") String tanggal,
            @Field("deadline") String deadline,
            @Field("alamat") String alamat,
            @Field("no_hp") String no_hp
    );
    @FormUrlEncoded
    @POST("update_proses.php")
    Call<Model_Response> UpdateProses(
            @Field("id") int id
    );
    @FormUrlEncoded
    @POST("update_selesai.php")
    Call<Model_Response> UpdateSelesai(
            @Field("id") int id
    );
    @FormUrlEncoded
    @POST("update_ambil.php")
    Call<Model_Response> UpdateAmbil(
            @Field("id") int id
    );
    @FormUrlEncoded
    @POST("update_batal.php")
    Call<Model_Response> UpdateBatal(
            @Field("id") int id
    );
    @FormUrlEncoded
    @POST("delete_data.php")
    Call<Model_Response> DeleteData(
            @Field("id") int id
    );
    @FormUrlEncoded
    @POST("insert_income.php")
    Call<Model_Response> InsertIncome(
            @Field("jumlah_uang") int jumlah_uang,
            @Field("nama") String nama,
            @Field("tanggal") String tgl
    );
    @FormUrlEncoded
    @POST("update_lunas.php")
    Call<Model_Response> UpdateLunas(
            @Field("id") int id
    );
    @GET("hitung_baru.php")
    Call<Model_Total> getJmlBaru();
    @GET("hitung_proses.php")
    Call<Model_Total> getJmlProses();
    @GET("hitung_selesai.php")
    Call<Model_Total> getJmlSelesai();
    @GET("hitung_ambil.php")
    Call<Model_Total> getJmlAmbil();
    @GET("hitung_batal.php")
    Call<Model_Total> getJmlBatal();
    @GET("hitung_trans_today.php")
    Call<Model_Total> getJmlTransToday();
    @GET("hitung_dead.php")
    Call<Model_Total> getJmlDead();
    @GET("ret_income.php")
    Call<Model_Income> getIncome();
    @GET("ret_dead.php")
    Call<Model_Response> getListDead();
    @GET("hitung_tran.php")
    Call<Model_Total> getJmlTran();
    @GET("ret_serv.php")
    Call<Model_Respon_Layanan> RetrieveLayanan();
    @GET("ret_layanan.php")
    Call<Model_Respon_Layanan> RetLayanan();

}
