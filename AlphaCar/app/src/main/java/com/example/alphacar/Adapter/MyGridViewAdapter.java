package com.example.alphacar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.alphacar.DTOS.NowDTO;
import com.example.alphacar.R;


import java.util.ArrayList;

public class MyGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NowDTO> arrayList;

    public MyGridViewAdapter(Context context, ArrayList<NowDTO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        //이미지셋에 있는 아이템의 수를 반환함(그리드뷰는 아이템의 수에 해당하는 행렬을 준비함)
        // getCount : 개수를 arrayList.size만큼 리턴
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        // 이미지 i개를 돌려가면서 출력
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //★★★★★ 실제로 화면으로 보여주게해주는 녀석
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.now_view, viewGroup, false);
            ViewHolder holder= new ViewHolder(view);
            onBindViewholder(holder , i);

        }
        return view;
    }
    public void onBindViewholder(@NonNull ViewHolder holder, int position) {

        NowDTO dto = arrayList.get(position);
        holder.setItem(dto,position);

    }

    private static class ViewHolder {
        ImageView img1;
        ImageView img2;
        ImageView img3;



        public ViewHolder(@NonNull View itemView) {
            img1 = itemView.findViewById(R.id.imageView1);
            //img2 = itemView.findViewById(R.id.imageView1);
            //img3 = itemView.findViewById(R.id.imageView1);


            img1.setImageResource(R.drawable.able);
           /* img2.setImageResource(R.drawable.disable);
            img3.setImageResource(R.drawable.x);*/

        }
        public void setItem(NowDTO dto , int position){
            if (dto.getImg().equals("able")){
                img1.setImageResource(R.drawable.able);
            }else if(dto.getImg().equals("disable")){
                img1.setImageResource(R.drawable.disable);
            }else{
                img1.setImageResource(R.drawable.x);
            }
            /*  for (int i =0; i < dto.size(); i++){

                img1.setImageResource(Integer.parseInt(dto.get(i).getImg()));
            }*/


            //            //img1.setImageResource(Integer.parseInt(dtos.get(i).getImg()));

            //glide로 이미지 넣기 추가 하면됨.

        }
    }
}
