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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class KakaoJoinInsert extends AsyncTask<Void, Void, String> {

    String customer_email, customer_pw, customer_name, admin, kakao;

    public KakaoJoinInsert(String customer_name, String customer_email) {
        this.customer_name = customer_name;
        this.customer_email = customer_email;

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
            builder.addTextBody("customer_name", customer_name, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("customer_email", customer_email, ContentType.create("Multipart/related", "UTF-8"));
            //builder.addTextBody("kakao", kakao, ContentType.create("Multipart/related", "UTF-8"));
//            builder.addTextBody("customer_email", customer_email, ContentType.create("Multipart/related", "UTF-8"));
//            builder.addTextBody("customer_pw", customer_pw, ContentType.create("Multipart/related", "UTF-8"));
//            builder.addTextBody("customer_name", customer_name, ContentType.create("Multipart/related", "UTF-8"));
//            builder.addTextBody("admin", admin, ContentType.create("Multipart/related", "UTF-8"));

            String postURL = ipConfig + "/alphacar/android/kakao_login";
            // 전송
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);
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
            e.printStackTrace();
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
