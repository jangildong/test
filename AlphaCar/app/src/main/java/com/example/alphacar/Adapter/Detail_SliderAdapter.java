package com.example.alphacar.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacar.R;

import java.util.ArrayList;

public class Detail_SliderAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> list = new ArrayList<>();

    public Detail_SliderAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

   public void setList(ArrayList<Fragment> list) {
        this.list = list;

   }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}