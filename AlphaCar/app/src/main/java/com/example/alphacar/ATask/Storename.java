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

public class Storename extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "main:Storename";

    /* 스토어 정보 AsyncTask */
    ArrayList<StoreDTO> dtos;
    StoreDTO dto;

    String name;

    public Storename(ArrayList<StoreDTO> dtos , String name ){
        this.dtos = dtos;
        this.name = name;
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
            //builder.addTextBody("store_number", String.valueOf(store_number), ContentType.create("Multipart/related", "UTF-8"));
          // builder.addTextBody("customer_email", customer_email, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("name", name, ContentType.create("Multipart/related", "UTF-8"));

            // 전송
            // 전송 url : 우리가 수정해야 하는 부분
            String postURL ="";
            postURL = ipConfig +"/alphacar/android/anShowName";

            // 그대로 사용  복,붙
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);  // 보내고 응답 받는 부분
            httpEntity = httpResponse.getEntity();    // 응답내용을 저장
            inputStream = httpEntity.getContent();    // 응답내용을 inputStream 에 넣음

            // 응답 처리 : array 형태
           readJsonStream(inputStream);


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
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);


    }


    public void readJsonStream(InputStream inputStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                dtos.add(readMessage(reader));
            }
            Log.d(TAG, "readJsonStream: " +dtos.size());
            reader.endArray();
        }catch (Exception e){
            e.getMessage();
        }finally {
            reader.close();
        }

    }

    //하나의 DTO형태로 데이터를 받을때 파싱하는 부분
    private StoreDTO readMessage(JsonReader reader) throws IOException {

        int store_number = 0;

        String
                store_name = "",
                introduce = "",
                imgpath = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String readStr = reader.nextName();
            if (readStr.equals("store_number")) {
                store_number = reader.nextInt();
            }
            else if (readStr.equals("store_name")) {
                store_name = reader.nextString();
            }  else if (readStr.equals("imgpath")) {
                imgpath =reader.nextString();
            } else if (readStr.equals("introduce")) {
                introduce = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new StoreDTO(store_number,store_name, introduce,imgpath);
        //return new StoreDTO(store_number, imgpath, inventory);

    }

}
