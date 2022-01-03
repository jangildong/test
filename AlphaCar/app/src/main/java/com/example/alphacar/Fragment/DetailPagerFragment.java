package com.example.alphacar.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.DetailActivity;
import com.example.alphacar.DetailImageActivity;
import com.example.alphacar.R;

public class DetailPagerFragment extends Fragment {
    DetailActivity dActivity;
    ImageView view;

   StoreDTO dto;
   // String dto;

    public DetailPagerFragment(StoreDTO dto) {this.dto = dto;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.detail_slider_item, container, false);
        dActivity = (DetailActivity) getActivity();

        view = rootView.findViewById(R.id.view);



            Glide.with(DetailPagerFragment.this)
                    .load(dto.getImgpath())
                    .into(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(dActivity, DetailImageActivity.class);
                    intent.putExtra("image_path", dto.getImgpath());
                    startActivity(intent);

                }
            });



        return rootView;
    }
}
