package com.example.alphacar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class LoadingPageActivity extends AppCompatActivity {

    LottieAnimationView animationView;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);

        animationView = findViewById(R.id.lottie);
        animationView.setAnimation("test8.json");
        animationView.loop(true);
        animationView.playAnimation();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(LoadingPageActivity.this,MainActivity.class);
                LoadingPageActivity.this.startActivity(intent);
                LoadingPageActivity.this.finish();
            }
        },3000);
    }

}
