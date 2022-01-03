package com.example.alphacar;


import static com.example.alphacar.Common.CommonMethod.isNetworkConnected;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.alphacar.ATask.ListAllDetail;
import com.example.alphacar.Adapter.MyGridViewAdapter;
import com.example.alphacar.DTOS.NowDTO;
import com.example.alphacar.DTOS.StoreDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class NowActivity extends AppCompatActivity {
    GridView gridView;
    MyGridViewAdapter adapter;
    ArrayList<StoreDTO> list = new ArrayList<>();

    ListAllDetail listAllDetail;

    SwipeRefreshLayout swipeRefreshLayout;


    ArrayList<NowDTO> nowDTOArrayList;

    //String customer_email = ;
    int store_number = 0;

    ImageView btn_back, now_refresh;

    Handler mHandler;

    TextView now_date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now);

        Intent intent = getIntent();
        store_number = intent.getIntExtra("store_number", 0);

        btn_back = findViewById(R.id.btn_back);
        now_refresh = findViewById(R.id.now_refresh);
        now_date = findViewById(R.id.now_date);
        this.mHandler = new Handler();




        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       now_refresh.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent1 = new Intent(NowActivity.this, NowActivity.class);
               intent1.putExtra("store_number", store_number);
               finish();
               startActivity(intent1);
           }
       });

        Date rightNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy.MM.dd hh:mm:ss ");
        String dateString = formatter.format(rightNow);
        now_date.setText(dateString);

        nowDTOArrayList = new ArrayList<>();
        list = new ArrayList<>();
        StoreDTO dto = new StoreDTO();
        if(isNetworkConnected(this) == true){
            listAllDetail = new ListAllDetail(store_number, list, adapter );
            //listDetail = new ListDetail(store_number);
            try {
                listAllDetail.execute().get();
                //Log.d(TAG, "onCreate: "+dto.getCustomer_email());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, "인터넷이 연결되어 있지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
        gridView = findViewById(R.id.gridView);
        //dto.setNow_state("Y");
        for (int i = 0; i < list.size(); i++ ){
            if (list.get(i).getNow_state().equals("Y")){
                nowDTOArrayList.add(new NowDTO("able", "able"));
            } else if(list.get(i).getNow_state().equals("N")){
                nowDTOArrayList.add(new NowDTO("disable", "disable"));
            } else if(list.get(i).getNow_state().equals("X")) {
                nowDTOArrayList.add(new NowDTO("x", "x"));
            }
        }


        adapter = new MyGridViewAdapter(this, nowDTOArrayList);

        //gridview사이
        gridView.setVerticalSpacing(0);
        gridView.setHorizontalSpacing(0);

        //gridview 열 갯수
        gridView.setNumColumns(3);

        gridView.setAdapter(adapter);
        // gridView.setlayout(layoutManager);

        this.mHandler.postDelayed(m_Runnable,5000);




    }


    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            nowDTOArrayList = new ArrayList<>();
            list = new ArrayList<>();
            gridView = findViewById(R.id.gridView);
            if(isNetworkConnected(NowActivity.this) == true){
                listAllDetail = new ListAllDetail(store_number, list, adapter );
                //listDetail = new ListDetail(store_number);
                try {
                    listAllDetail.execute().get();
                    //Log.d(TAG, "onCreate: "+dto.getCustomer_email());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(NowActivity.this, "인터넷이 연결되어 있지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }


            //dto.setNow_state("Y");
            for (int i = 0; i < list.size(); i++ ){
                if (list.get(i).getNow_state().equals("Y")){
                    nowDTOArrayList.add(new NowDTO("able", "able"));
                } else if(list.get(i).getNow_state().equals("N")){
                    nowDTOArrayList.add(new NowDTO("disable", "disable"));
                } else if(list.get(i).getNow_state().equals("X")) {
                    nowDTOArrayList.add(new NowDTO("x", "x"));
                }
            }


            adapter = new MyGridViewAdapter(NowActivity.this, nowDTOArrayList);

            //gridview사이
            gridView.setVerticalSpacing(0);
            gridView.setHorizontalSpacing(0);

            //gridview 열 갯수
            gridView.setNumColumns(3);

            gridView.setAdapter(adapter);

            NowActivity.this.mHandler.postDelayed(m_Runnable, 5000);

            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy.MM.dd hh:mm:ss ");
            String dateString = formatter.format(rightNow);
            now_date.setText(dateString);
        }

    };//runnable


    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();

    }
}
