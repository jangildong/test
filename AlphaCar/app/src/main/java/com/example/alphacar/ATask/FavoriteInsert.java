package com.example.alphacar.ATask;



import static com.example.alphacar.Common.CommonMethod.ipConfig;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.InputStream;
import java.nio.charset.Charset;

public class FavoriteInsert extends AsyncTask<Void, Void,Void> {


    String customer_email = "";
    int store_number = 0;

    // 반드시 선언해야 할것들 : 무조건 해야함  복,붙
    HttpClient httpClient;       // 클라이언트 객체
    HttpPost httpPost;           // 클라이언트에 붙일 본문
    HttpResponse httpResponse;   // 서버에서의 응답받는 부분
    HttpEntity httpEntity;       // 응답내용

    public FavoriteInsert(String customer_email, int store_number) {
        this.customer_email = customer_email;
        this.store_number = store_number;
    }

    @Override
    protected Void doInBackground(Void... voids) {
    try {
        // MultipartEntityBuilder 생성 : 무조건 해야함  복,붙
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Charset.forName("UTF-8"));

        builder.addTextBody("customer_email", customer_email, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("store_number", String.valueOf(store_number), ContentType.create("Multipart/related", "UTF-8"));

        String postURL = ipConfig + "/alphacar/android/anFavoriteInsert";

        InputStream inputStream = null;
        httpClient = AndroidHttpClient.newInstance("Android");
        httpPost = new HttpPost(postURL);
        httpPost.setEntity(builder.build());
        httpResponse = httpClient.execute(httpPost);  // 보내고 응답 받는 부분
        httpEntity = httpResponse.getEntity();    // 응답내용을 저장
        inputStream = httpEntity.getContent();    // 응답내용을 inputStream 에 넣음

    }catch (Exception e) {
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
}
