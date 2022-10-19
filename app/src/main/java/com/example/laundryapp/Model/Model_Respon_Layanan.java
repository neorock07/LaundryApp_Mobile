package com.example.laundryapp.Model;

import java.util.ArrayList;
import java.util.List;

public class Model_Respon_Layanan {

    private int kode;
    private String pesan;
    private ArrayList<Model_layanan> data;


    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public ArrayList<Model_layanan> getData() {
        return data;
    }

    public void setData(ArrayList<Model_layanan> data) {
        this.data = data;
    }
}
