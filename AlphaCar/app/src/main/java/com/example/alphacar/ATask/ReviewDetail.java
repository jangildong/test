package com.example.alphacar.ATask;

import static com.example.alphacar.Common.CommonMethod.ipConfig;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.example.alphacar.DTOS.ReviewDTO;

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

public class ReviewDetail extends AsyncTask<Void, Void, ReviewDTO> {
    String review_id;
    ReviewDTO dto;

    public ReviewDetail(int review_id) {
        this.review_id = Integer.toString(review_id);
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
    protected ReviewDTO doInBackground(Void... voids) {
        try {
            // MultipartEntityBuilder 생성 : 무조건 해야함  복,붙
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName("UTF-8"));

            // 여기가 우리가 수정해야 하는 부분 : 서버로 보내는 데이터
            // builder 에 문자열 및 데이터 추가하기
            //builder.addTextBody("store_number", String.valueOf(store_number), ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("review_id", review_id, ContentType.create("Multipart/related", "UTF-8"));

            // 전송
            // 전송 url : 우리가 수정해야 하는 부분
            String postURL = ipConfig + "/alphacar/android/reviewDetail";
            // 그대로 사용  복,붙
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);  // 보내고 응답 받는 부분
            httpEntity = httpResponse.getEntity();    // 응답내용을 저장
            inputStream = httpEntity.getContent();    // 응답내용을 inputStream 에 넣음

            // 응답 처리 : dto 형태
            dto = readMessage(inputStream);

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

        return dto;
    }

    @Override
    protected void onPostExecute(ReviewDTO reviewDTO) {
        super.onPostExecute(reviewDTO);
    }


    //하나의 DTO 형태로 데이터를 받을때 파싱하는 부분
    private ReviewDTO readMessage(InputStream inputStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

        int store_number = 0;
        String customer_email = "", review_title = "", review_content = "", review_filepath = "", customer_picture = "", review_score="";

        reader.beginObject();
        while (reader.hasNext()) {
            String readStr = reader.nextName();
            if (readStr.equals("store_number")) {
                store_number = reader.nextInt();
            } else if (readStr.equals("customer_email")) {
                customer_email = reader.nextString();
            }
            else if (readStr.equals("review_title")) {
                review_title = reader.nextString();
            } else if (readStr.equals("review_content")) {
                review_content = reader.nextString();
            } else if (readStr.equals("review_score")) {
                review_score = reader.nextString();
            } else if (readStr.equals("review_filepath")) {
                review_filepath = reader.nextString();
            } else if (readStr.equals("customer_picture")) {
                customer_picture = reader.nextString();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new ReviewDTO(customer_email, review_title, review_content, review_score, review_filepath, customer_picture);
    }

}
