package com.example.laundryapp.Model;

public class Model_layanan {

    private String pesan;
    private String Nama;
    private String Estimasi;
    private String Jenis_waktu;
    private String Satuan;
    private String Deskripsi;

    public String getHarga() {
        return Harga;
    }

    public void setHarga(String harga) {
        Harga = harga;
    }

    private String Harga;
    private int Id,kode;

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    private int Min_order;


    Model_layanan(){

    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getEstimasi() {
        return Estimasi;
    }

    public void setEstimasi(String estimasi) {
        Estimasi = estimasi;
    }

    public String getJenis_waktu() {
        return Jenis_waktu;
    }

    public void setJenis_waktu(String jenis_waktu) {
        Jenis_waktu = jenis_waktu;
    }

    public String getSatuan() {
        return Satuan;
    }

    public void setSatuan(String satuan) {
        Satuan = satuan;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        Deskripsi = deskripsi;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getMin_order() {
        return Min_order;
    }

    public void setMin_order(int min_order) {
        Min_order = min_order;
    }


}
