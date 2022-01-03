package com.example.alphacar.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.alphacar.DTOS.StoreDTO;

import java.util.ArrayList;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> list = new ArrayList<>();

    /* 메인 뷰페이저 어뎁터 */

    public ViewpagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /* 리스트 받아오는 메소드 */
    public void setList(ArrayList<Fragment> list){

        this.list = list;
    }




    // list 인덱스 위치에 있는 프래그먼트를 가져온다.
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    // list 가지고 있는 프래그먼트의 갯수
    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "세차창" + (position + 1);
    }


}
