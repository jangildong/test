package com.example.alphacar;

import static com.example.alphacar.Common.CommonMethod.isNetworkConnected;
import static com.example.alphacar.LoginPageActivity.loginDTO;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacar.ATask.FavoriteSelect;
import com.example.alphacar.Adapter.FavoriteAdapter;
import com.example.alphacar.DTOS.FavoriteDTO;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FavoriteActivity extends AppCompatActivity {

        ImageButton imageButton;
        ArrayList<FavoriteDTO> arrayList;
        FavoriteDTO dto;
        FavoriteSelect favoriteSelect;
        RecyclerView recyclerView;
        FavoriteAdapter favoriteAdapter;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoriteinside);
        recyclerView = findViewById(R.id.recyclerView);
        imageButton = findViewById(R.id.fav_back_Button);

        arrayList = new ArrayList<>();
        if(loginDTO != null) {
            if (isNetworkConnected(this) == true) {
                favoriteSelect = new FavoriteSelect(dto, arrayList);
                //listDetail = new ListDetail(store_number);
                try {
                    favoriteSelect.execute().get();
                    //    Log.d(TAG, "onCreate: "+dto.getCustomer_email());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "인터넷이 연결되어 있지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this, arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false
        );






        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(favoriteAdapter);



        /* 백버튼 */
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
