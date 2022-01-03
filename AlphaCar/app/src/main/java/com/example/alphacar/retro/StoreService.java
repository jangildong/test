package com.example.alphacar.retro;

import com.example.alphacar.DTOS.MemberVO;
import com.example.alphacar.DTOS.StoreDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StoreService {


    @POST("anSelectFile")
    Call<ArrayList<StoreDTO>> getGson_List();

    @POST("anSelectDetail")
    Call<ArrayList<StoreDTO>> getDetail_List(@Query("store_number")int store_number);

    @POST("anFavoriteDel")
    Call<String> getFavCheck(@Query("fav_number") int fav_number);


    @POST("and_login")
    Call<MemberVO> getAnLogin(@Query("customer_email") String customer_email,
                              @Query("customer_pw") String customer_pw);
}
