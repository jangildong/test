package com.example.alphacar.Common;

import com.example.alphacar.retro.StoreService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCommon {
    private final static String BASE_URL = "http://192.168.0.122:8080/alphacar/android/";




    public static StoreService getClient(){


           Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        StoreService storeService = retrofit.create(StoreService.class);
        return storeService;
    }
}
