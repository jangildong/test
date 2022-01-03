package com.example.alphacar.Common;

import android.net.http.AndroidHttpClient;

import com.example.alphacar.DTOS.StoreDTO;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.InputStream;
import java.util.ArrayList;

public class AtaskCommon {

    HttpClient httpClient;
    HttpPost httpPost;
    HttpResponse httpResponse;
    HttpEntity httpEntity;
    MultipartEntityBuilder builder;

    InputStream inputStream;
    final static String HTTPIP = "http://192.168.0.122:8080";
    final static String SPRPATH = "/alphacar/android/";
    private String postURL ;


    public AtaskCommon(String mapping){
        postURL = HTTPIP + SPRPATH +mapping ;
        // MultipartEntityBuild 생성
        builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    }

    public void initHttp(){
        try {

            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            inputStream = httpClient.execute(httpPost).getEntity().getContent();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpEntity != null) httpEntity = null;
            if (httpResponse != null)  httpResponse = null;
            if (httpPost != null) httpPost = null;
            if (httpClient != null) httpClient = null;
        }

    }

    public InputStream sendSpring() {
        initHttp();
        return inputStream;
    }

    public void initParam(String key , ArrayList<StoreDTO> dto){
        Gson gson = new Gson();
        builder.addTextBody(key, gson.toJson(dto), ContentType.create("Multipart/related", "UTF-8"));
    }


    public InputStream sendSpring(int store_number) {
        initParam(store_number);
        initHttp();
        return inputStream;
    }

    public InputStream sendSpring(String customer_email, String customer_pw) {
        initParam(customer_email, customer_pw);
        initHttp();
        return inputStream;
    }

    public void initParam(int store_number){
        Gson gson = new Gson();
        builder.addTextBody("store_number",String.valueOf(store_number), ContentType.create("Multipart/related", "UTF-8"));
    }

    public void initParam(String customer_email, String customer_pw){
        Gson gson = new Gson();
        builder.addTextBody("customer_email", customer_email, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("customer_pw", customer_pw, ContentType.create("Multipart/related", "UTF-8"));
    }


}
