package com.example.alphacar.Fragment;

import static com.example.alphacar.Common.CommonMethod.isNetworkConnected;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.alphacar.ATask.FavoriteSelect;
import com.example.alphacar.ATask.NotiSelect;
import com.example.alphacar.ATask.Storename;
import com.example.alphacar.Adapter.NotifyAdapter;
import com.example.alphacar.Adapter.StoreAdapter;
import com.example.alphacar.DTOS.NotiftDTO;
import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.MainActivity;
import com.example.alphacar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

public class AnnounceFragment extends Fragment implements AbsListView.OnScrollListener {

    MainActivity mActivity;
    NotiSelect notiSelect;
    ArrayList<NotiftDTO> notiftDTOS;
    NotifyAdapter notifyAdapter;
    ListView listView;
    boolean lastItemVisibleFlag = false;
    int page = 0;
    final int OFFSET = 10;
    ProgressBar progressBar;
    boolean mLockListView = false;
    ArrayList<NotiftDTO> notiftList;
    LinearLayout noti_item;
    int notice_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.notify,
                            container,false);
        mActivity = (MainActivity)  getActivity();
        notiftDTOS = new ArrayList<>();

        listView = rootview.findViewById(R.id.notify_listView);
        progressBar = rootview.findViewById(R.id.noti_progressbar);
        noti_item = rootview.findViewById(R.id.noti_item);

        progressBar.setVisibility((View.GONE));

        notiftList = new ArrayList<>();
     

        if (isNetworkConnected(mActivity) == true) {
            notiSelect = new NotiSelect("admin@naver.com",notiftDTOS,notice_id);
            //listDetail = new ListDetail(store_number);
            try {
                notiSelect.execute().get();
                //    Log.d(TAG, "onCreate: "+dto.getCustomer_email());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mActivity, "인터넷이 연결되어 있지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }


            notifyAdapter = new NotifyAdapter(mActivity, notiftList);
            listView.setAdapter(notifyAdapter);

            listView.setOnScrollListener(this);
            getItem();





        return rootview;
    }





    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false){
            progressBar.setVisibility(View.VISIBLE);

            getItem();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItemVisibleFlag =(totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    private void getItem() {

        mLockListView = true;

        for(int i = 0; i < 10; i ++){
            notiftList.add(notiftDTOS.get((page * OFFSET) + i));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                notifyAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        },1000);
    }
}
