package com.example.alphacar.ATask;



import static com.example.alphacar.Common.CommonMethod.ipConfig;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;


import com.example.alphacar.DTOS.StoreDTO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class DetailSelect extends AsyncTask<Void, Void, Void> {


//    String customer_email = "store_master@naver.com";

    int store_number = 0;
    /*public ListDetail(int store_number) {
        this.store_number = store_number;
    }*/
    StoreDTO dto;
    ArrayList<StoreDTO> storeDTOArrayList;

    public DetailSelect(int store_number, ArrayList<StoreDTO> storeDTOArrayList) {
        this.store_number = store_number;
        this.storeDTOArrayList = storeDTOArrayList;
    }

    // 반드시 선언해야 할것들 : 무조건 해야함  복,붙
    HttpClient httpClient;       // 클라이언트 객체
    HttpPost httpPost;           // 클라이언트에 붙일 본문
    HttpResponse httpResponse;   // 서버에서의 응답받는 부분
    HttpEntity httpEntity;       // 응답내용

    // doInBackground 하기전에 설정 및 초기화 : 여기서는 사용않함
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // MultipartEntityBuilder 생성 : 무조건 해야함  복,붙
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName("UTF-8"));

            // 여기가 우리가 수정해야 하는 부분 : 서버로 보내는 데이터
            // builder에 문자열 및 데이터 추가하기
            builder.addTextBody("store_number", String.valueOf(store_number), ContentType.create("Multipart/related", "UTF-8"));
            // builder.addTextBody("customer_email", customer_email, ContentType.create("Multipart/related", "UTF-8"));


            // 전송
            // 전송 url : 우리가 수정해야 하는 부분
            String postURL = ipConfig + "/alphacar/anSelectDetail";
            // 그대로 사용  복,붙
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);  // 보내고 응답 받는 부분
            httpEntity = httpResponse.getEntity();    // 응답내용을 저장
            inputStream = httpEntity.getContent();    // 응답내용을 inputStream 에 넣음

            // 응답 처리 : dto 형태
            readJsonStream(inputStream);

            inputStream.close();

        } catch (Exception e) {
            e.getMessage();
        } finally {
            if (httpEntity != null) {
                httpEntity = null;
            }
            if (httpResponse != null) {
                httpResponse = null;
            }
            if (httpPost != null) {
                httpPost = null;
            }
            if (httpClient != null) {
                httpClient = null;
            }
        }
        return null;
    }
    // doInBackground 끝난후에 실행하는 부분


    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
    }

    public void readJsonStream(InputStream inputStream) throws Exception {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                storeDTOArrayList.add(readMessage(reader));
            }
            reader.endArray();
        } finally {
            reader.close();
        }
    }


    //하나의 DTO형태로 데이터를 받을때 파싱하는 부분
    private StoreDTO readMessage(JsonReader reader) throws IOException {


        int store_number = 0, fav_number = 0 ;
        String customer_email = "", store_name = "", store_addr = "", store_tel = "", store_time = "", store_dayoff = "", introduce = "", imgpath = "";
        int inventory = 0;
        String store_price = "", store_master_name = "", store_registration_number = "";
        int store_favorite_cnt = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String readStr = reader.nextName();
            if (readStr.equals("store_number")) {
                store_number = reader.nextInt();
            } else if (readStr.equals("customer_email")) {
                customer_email = reader.nextString();
            } else if (readStr.equals("store_name")) {
                store_name = reader.nextString();
            } else if (readStr.equals("store_addr")) {
                store_addr = reader.nextString();
            } else if (readStr.equals("store_tel")) {
                store_tel = reader.nextString();
            } else if (readStr.equals("store_time")) {
                store_time = reader.nextString();
            } else if (readStr.equals("store_dayoff")) {
                store_dayoff = reader.nextString();
            } else if (readStr.equals("introduce")) {
                introduce = reader.nextString();
            } else if (readStr.equals("imgpath")) {
                imgpath = reader.nextString();
            } else if (readStr.equals("inventory")) {
                inventory = reader.nextInt();
            } else if (readStr.equals("store_price")) {
                store_price = reader.nextString();
            } else if (readStr.equals("store_master_name")) {
                store_master_name = reader.nextString();
            } else if (readStr.equals("store_registration_number")) {
                store_registration_number = reader.nextString();
            } else if (readStr.equals("store_favorite_cnt")) {
                store_favorite_cnt = reader.nextInt();
            }  else if (readStr.equals("fav_number")) {
                fav_number = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new StoreDTO(store_number, customer_email, store_name, store_addr, store_tel, store_time, store_dayoff, introduce, imgpath, inventory, store_price, store_master_name, store_registration_number, store_favorite_cnt, fav_number);
    }
}
