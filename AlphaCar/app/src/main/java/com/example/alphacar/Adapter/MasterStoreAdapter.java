package com.example.alphacar.Adapter;

import static com.example.alphacar.LoginPageActivity.loginDTO;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.alphacar.DTOS.FavoriteDTO;
import com.example.alphacar.DTOS.RegisterDTO;
import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.R;
import com.example.alphacar.RegisterUpdateActivity;

import java.util.ArrayList;

public class MasterStoreAdapter extends RecyclerView.Adapter<MasterStoreAdapter.ItemViewHolder> {

    Context mContext;
    ArrayList<RegisterDTO> registerDTOArrayList;

    public MasterStoreAdapter(Context mContext, ArrayList<RegisterDTO> registerDTOArrayList) {
        this.mContext = mContext;
        this.registerDTOArrayList = registerDTOArrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.master_store_item,parent,false);


        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        RegisterDTO item = registerDTOArrayList.get(position);
        holder.setItem(item);


        holder.master_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegisterUpdateActivity.class);
                intent.putExtra("store_number", item.getStore_number());
                intent.putExtra("store_name", item.getStore_name());
                intent.putExtra("customer_name",item.getStore_master_name());
                intent.putExtra("store_registration_number", item.getStore_registration_number());
                intent.putExtra("inventory", item.getInventory());
                intent.putExtra("store_price", item.getStore_price());
                intent.putExtra("introduce", item.getIntroduce());
                intent.putExtra("store_post", item.getStore_post());
                intent.putExtra("store_addr", item.getStore_addr());
                intent.putExtra("store_detail_addr", item.getStore_detail_addr());
                intent.putExtra("store_tel", item.getStore_tel());
                intent.putExtra("store_time", item.getStore_time());
                intent.putExtra("store_dayoff", item.getStore_dayoff());
                intent.putExtra("image_path", item.getImgpath());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return registerDTOArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout master_layout;
        public ImageView storeImg;
        public TextView storeName;
        public TextView fabt;
        public ImageButton del_btn;






        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            master_layout = itemView.findViewById(R.id.master_item_lay);
            storeImg = itemView.findViewById(R.id.master_img);
            storeName = itemView.findViewById(R.id.master_tv);
            fabt = itemView.findViewById(R.id.master_bt);




        }

        public void setItem(RegisterDTO item){
            Glide.with(itemView).load(item.getImgpath()).into(storeImg);
            storeName.setText(item.getStore_name());


        }
    }
}
