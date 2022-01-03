package com.example.alphacar.ATask;

import static com.example.alphacar.Common.CommonMethod.ipConfig;
import static com.example.alphacar.LoginPageActivity.loginDTO;


import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.alphacar.DTOS.MemberVO;


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

public class LoginSelect extends AsyncTask<Void, Void, MemberVO> {

    String customer_email, customer_pw;



    public LoginSelect(String customer_email, String customer_pw) {
        this.customer_email = customer_email;
        this.customer_pw = customer_pw;
    }

    HttpClient httpClient;
    HttpPost httpPost;
    HttpResponse httpResponse;
    HttpEntity httpEntity;


    @Override
    protected MemberVO doInBackground(Void... voids) {

        try {
            // MultipartEntityBuild 생성
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            // 문자열 및 데이터 추가
            builder.addTextBody("customer_email", customer_email, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("customer_pw", customer_pw, ContentType.create("Multipart/related", "UTF-8"));

            String postURL = ipConfig + "/alphacar/android/and_login";
            // 전송
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            // 로그인 된 정보를 가져옴
          loginDTO =  readMessage(inputStream);
            inputStream.close();


        } catch (Exception e) {
            Log.d("main:loginselect", e.getMessage());
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

        return loginDTO;
    }




    public MemberVO readMessage(InputStream inputStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

        String customer_email = "", customer_pw = "", customer_name = "",  admin = "", customer_picture="";

        reader.beginObject();
        while (reader.hasNext()) {
            String readStr = reader.nextName();
            if (readStr.equals("customer_email")) {
                customer_email = reader.nextString();
            } else if (readStr.equals("customer_pw")) {
                customer_pw = reader.nextString();
            } else if (readStr.equals("customer_name")) {
                customer_name = reader.nextString();
            } else if (readStr.equals("admin")) {
                admin = reader.nextString();
            } else if (readStr.equals("customer_picture")) {
                customer_picture = reader.nextString();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d("main:loginselect : ", customer_email + "," + customer_pw + "," + customer_name + "," + admin+ "," + customer_picture);
        return new MemberVO(customer_email, customer_pw, customer_name, admin, customer_picture);

    }


}
