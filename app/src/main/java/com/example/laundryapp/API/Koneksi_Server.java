package com.example.laundryapp.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Koneksi_Server {
    private static final String BASE_URL = "http://10.0.2.2/laundry/";
    private static Retrofit retro;

    public static Retrofit koneksi(){
        if(retro == null){
            retro = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retro;

    }

}
