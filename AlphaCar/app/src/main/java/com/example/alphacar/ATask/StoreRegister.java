package com.example.alphacar.ATask;

import static com.example.alphacar.Common.CommonMethod.ipConfig;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.alphacar.DTOS.RegisterDTO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class StoreRegister extends AsyncTask<Void, Void, String> {
    private static final String TAG = "main:StoreRegister";

    String customer_email             ;
    String store_name                 ;
    String store_post                 ;
    String store_addr                 ;
    String store_detail_addr          ;
    String store_tel                  ;
    String store_time                 ;
    String store_dayoff               ;
    String introduce                  ;
    int inventory                  ;
    String store_price                ;
    String store_master_name          ;
    String store_registration_number  ;
    ArrayList<String> storePic;

    public StoreRegister(String customer_email, String store_name, String store_addr, String store_tel, String store_time, String store_dayoff, String introduce, String store_price, String store_master_name, String store_registration_number, ArrayList<String> storePic) {
        this.customer_email = customer_email;
        this.store_name = store_name;
        this.store_addr = store_addr;
        this.store_tel = store_tel;
        this.store_time = store_time;
        this.store_dayoff = store_dayoff;
        this.introduce = introduce;
        this.store_price = store_price;
        this.store_master_name = store_master_name;
        this.store_registration_number = store_registration_number;
        this.storePic = storePic;
    }

    public StoreRegister(String customer_email, String store_name,String store_post, String store_addr,String store_detail_addr, String store_tel, String store_time, String store_dayoff, String introduce, int inventory, String store_price, String store_master_name, String store_registration_number, ArrayList<String> storePic) {
        this.customer_email = customer_email;
        this.store_name = store_name;
        this.store_post = store_post;
        this.store_addr = store_addr;
        this.store_detail_addr = store_detail_addr;
        this.store_tel = store_tel;
        this.store_time = store_time;
        this.store_dayoff = store_dayoff;
        this.introduce = introduce;
        this.inventory = inventory;
        this.store_price = store_price;
        this.store_master_name = store_master_name;
        this.store_registration_number = store_registration_number;
        this.storePic = storePic;
    }

    // 데이터베이스에 삽입결과 0보다크면 삽입성공, 같거나 작으면 실패
    String state = "";

    HttpClient httpClient;
    HttpPost httpPost;
    HttpResponse httpResponse;
    HttpEntity httpEntity;


    @Override
    protected String doInBackground(Void... voids) {

        try {
            // MultipartEntityBuild 생성
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName("UTF-8"));

            // 문자열 및 데이터 추가
            builder.addTextBody("customer_email", customer_email, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_name", store_name, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_post", store_post, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_addr", store_addr, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_detail_addr", store_detail_addr, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_tel", store_tel, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_time", store_time, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_dayoff", store_dayoff, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("introduce", introduce, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("inventory", String.valueOf(inventory), ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_price", store_price, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_master_name", store_master_name, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("store_registration_number", store_registration_number, ContentType.create("Multipart/related", "UTF-8"));

            for (int i = 0; i< storePic.size(); i++){
                builder.addPart("imgpath"+(i+1), new FileBody(new File(storePic.get(i))));
            }

            String postURL = ipConfig + "/alphacar/android/anStoreRegister";
            // 전송
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);
            httpClient= null;
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            // 응답
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            state = stringBuilder.toString();

            inputStream.close();

        }  catch (Exception e) {
            //e.printStackTrace();
        }finally {
            if(httpEntity != null){
                httpEntity = null;
            }
            if(httpResponse != null){
                httpResponse = null;
            }
            if(httpPost != null){
                httpPost = null;
            }
            if(httpClient != null){
                httpClient = null;
            }

        }

        return state;
    }

    @Override
    protected void onPostExecute(String state) {

    }
}