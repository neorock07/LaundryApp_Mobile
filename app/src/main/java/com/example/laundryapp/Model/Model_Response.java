package com.example.laundryapp.Model;

import java.util.List;

public class Model_Response {
    private int kode;
    private String pesan;
    private List<Model_Data> data;

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

    public List<Model_Data> getData() {
        return data;
    }

    public void setData(List<Model_Data> data) {
        this.data = data;
    }
}
