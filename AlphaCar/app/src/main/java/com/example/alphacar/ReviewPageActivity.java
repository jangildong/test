package com.example.alphacar;

import static com.example.alphacar.LoginPageActivity.loginDTO;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.alphacar.ATask.ReviewDetail;
import com.example.alphacar.DTOS.ReviewDTO;
import com.example.alphacar.Fragment.ReviewPhotoFragment;

import java.util.concurrent.ExecutionException;

public class ReviewPageActivity extends AppCompatActivity {

    ReviewPhotoFragment fragment;

    Button btnBack;
    ImageView profileImg, reviewPhoto;
    TextView userID, reviewHeader, reviewTitle, reviewContent;
    RatingBar showRating;

    ReviewDTO dto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        Intent intent = getIntent();
        int review_id = intent.getIntExtra("review_id", 0);

        btnBack = findViewById(R.id.btnBack);
        profileImg = findViewById(R.id.profileImg);
        reviewPhoto = findViewById(R.id.reviewPhoto);
        userID = findViewById(R.id.userID);
        reviewTitle = findViewById(R.id.reviewTitle);
        reviewContent = findViewById(R.id.reviewContent);
        showRating = findViewById(R.id.showRating);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ReviewDetail reviewDetail = new ReviewDetail(review_id);
        try {
            dto = reviewDetail.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reviewPage();
    }

    public void reviewPage() {

        profileImg = findViewById(R.id.profileImg);
        reviewPhoto = findViewById(R.id.reviewPhoto);
        reviewHeader = findViewById(R.id.reviewHeader);
        userID = findViewById(R.id.userID);
        reviewTitle = findViewById(R.id.reviewTitle);
        reviewContent = findViewById(R.id.reviewContent);
        showRating = findViewById(R.id.showRating);

      /*  fragment = new ReviewPhotoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.forPhoto, fragment).commit();
*/


        /*reviewPhoto.setImageResource(dto.getReview_filepath());*/
        Glide.with(ReviewPageActivity.this)
                .load(dto.getReview_filepath())
                .into(reviewPhoto);
        Glide.with(ReviewPageActivity.this)
                .load(dto.getCustomer_picture())
                .into(profileImg);
        userID.setText(dto.getCustomer_email());
        reviewTitle.setText("제목: " + dto.getReview_title());
        reviewContent.setText("내용: " + dto.getReview_content());
        showRating.setRating(Float.parseFloat(dto.getReview_score().substring(0,1)));


    }

}
