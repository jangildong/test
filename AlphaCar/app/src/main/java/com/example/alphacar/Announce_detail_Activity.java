package com.example.alphacar;

import static com.example.alphacar.Common.CommonMethod.isNetworkConnected;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.alphacar.DTOS.NotiftDTO;

import java.util.concurrent.ExecutionException;

public class Announce_detail_Activity extends AppCompatActivity {

    NotiftDTO notiftDTO;
    LinearLayout notify_datail;
    ImageView noti_detail_photo;
    TextView noti_detail_title;
    TextView  noti_detail_content;
    int notice_id;
    String customer_email, notice_title,notice_content, notice_file;

    Button noti_detail_btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //화면 붙이기
        setContentView(R.layout.notify_detail);

        noti_detail_photo = findViewById(R.id.noti_detail_photo);
        noti_detail_title = findViewById(R.id.noti_detail_title);
        noti_detail_content = findViewById(R.id.noti_detail_content);
        noti_detail_btnBack = findViewById(R.id.noti_detail_btnBack);

        Intent intent = getIntent();
        notice_id = intent.getIntExtra("notice_id",0);
        customer_email = intent.getStringExtra("customer_email");
        notice_title = intent.getStringExtra("notice_title");
        notice_content = intent.getStringExtra("notice_content");
        notice_file = intent.getStringExtra("notice_file");



        noti_detail_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        noti_detail_title.setText(notice_title);
        noti_detail_content.setText(notice_content);
//        Glide.with(Announce_detail_Activity.this)
//                .load(notiftDTO.getNotice_filepath())
//                .into(noti_detail_photo);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
