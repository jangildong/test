package com.example.alphacar.ATask;

import android.os.AsyncTask;

import com.example.alphacar.Common.AtaskCommon;
import com.example.alphacar.DTOS.ReviewDTO;
import com.example.alphacar.DTOS.StoreDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DetailAtask extends AsyncTask<Object,Object,Object> {

    private ArrayList<StoreDTO> storeDTOArrayList;
    private InputStream is;
    private  int store_number;
    private  ArrayList<ReviewDTO> reviewDTOArrayList;
    ReviewDTO dto;

    public DetailAtask(int store_number ,ArrayList<StoreDTO> storeDTOArrayList) {
        this.store_number = store_number;
        this.storeDTOArrayList = storeDTOArrayList;
    }



    @Override
    protected Object doInBackground(Object... objects) {
        if (storeDTOArrayList != null) {//main 뷰페이저 조회
            ArrayList<StoreDTO> storeDTOArrayList = new ArrayList<>();
            try {
                AtaskCommon common = new AtaskCommon("anSelectDetail");
                is = common.sendSpring(store_number);
                Gson gson = new Gson();
                storeDTOArrayList = gson.fromJson(new InputStreamReader(is), new TypeToken<List<StoreDTO>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return storeDTOArrayList;
        }
        else{
            return null;
        }
    }
}
