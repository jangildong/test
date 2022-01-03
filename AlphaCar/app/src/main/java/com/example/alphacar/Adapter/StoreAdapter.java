package com.example.alphacar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StoreAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<StoreDTO> list ;

    private ArrayList<StoreDTO> storeDTOArrayList;

    public StoreAdapter(Context mContext,  List<StoreDTO> list) {
        this.mContext = mContext;
        this.list = list;
        this.inflater = LayoutInflater.from(mContext);
               // .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.storeDTOArrayList = new ArrayList<StoreDTO>();
        this.storeDTOArrayList.addAll(list);

    }

    public class ViewHolder{
        TextView name;
    }

    @Override
    public int getCount() {
        return storeDTOArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeDTOArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
       ViewHolder holder;
       if (view == null){
           holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_bar_item,parent,false);
           // Locate the TextViews in listview_item.xml
            holder.name = view.findViewById(R.id.name);
            view.setTag(holder);
       } else{
           holder = (ViewHolder) view.getTag();
       }
        // Set the results into TextViews
        holder.name.setText(storeDTOArrayList.get(position).getStore_name());

        return view;
    }




}
