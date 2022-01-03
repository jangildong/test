package com.example.alphacar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.alphacar.Fragment.DetailPagerFragment;

public class DetailImageActivity extends AppCompatActivity {
    ImageView imageView;
    String image_path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_image_view);
        imageView = findViewById(R.id.detail_image_view);

        Intent intent = getIntent();
        image_path = intent.getStringExtra("image_path");

        Glide.with(DetailImageActivity.this)
                .load(image_path)
                .into(imageView);
    }
}
