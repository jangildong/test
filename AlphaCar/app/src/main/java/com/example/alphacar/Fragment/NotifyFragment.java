package com.example.alphacar.Fragment;

import static com.example.alphacar.Common.CommonMethod.isNetworkConnected;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alphacar.ATask.Storename;
import com.example.alphacar.Adapter.StoreAdapter;
import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class NotifyFragment extends Fragment {

    ListView notify;
    Storename storename;
    ArrayList<StoreDTO> storeDTOArrayList;
    StoreAdapter sAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.notify,
                            container,false);

        return rootview;
    }
}
