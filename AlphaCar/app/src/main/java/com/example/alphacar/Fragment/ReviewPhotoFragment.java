package com.example.alphacar.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.alphacar.DTOS.ReviewDTO;
import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.R;
import com.example.alphacar.ReviewPageActivity;

public class ReviewPhotoFragment extends Fragment {

    ReviewPageActivity reviewPage;
    ImageView reviewPhoto;
    ReviewDTO dto ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.review_photo_fragment,
                container, false);

        reviewPage = (ReviewPageActivity) getActivity();

        Glide.with(reviewPage)
                .load(dto.getReview_filepath())
                .circleCrop()
                .into(reviewPhoto);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
