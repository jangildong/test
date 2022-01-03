package com.example.alphacar.Fragment;

import static com.example.alphacar.Common.CommonMethod.isNetworkConnected;
import static com.example.alphacar.LoginPageActivity.loginDTO;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alphacar.ATask.MasterStoreSelect;
import com.example.alphacar.Adapter.MasterStoreAdapter;
import com.example.alphacar.DTOS.RegisterDTO;
import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.MainActivity;
import com.example.alphacar.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MasterStoreFragment extends Fragment {

    RecyclerView recyclerView;

    MainActivity mainActivity;
    ArrayList<RegisterDTO> registerDTOArrayList;
    StoreDTO storeDTO;

    MasterStoreSelect masterStoreSelect;
    MasterStoreAdapter masterStoreAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.master_store,
                container,false);

        mainActivity = (MainActivity) getActivity();
        registerDTOArrayList = new ArrayList<>();

        if (isNetworkConnected(mainActivity) == true) {
            masterStoreSelect = new MasterStoreSelect(loginDTO.getCustomer_email(),storeDTO, registerDTOArrayList);
            //listDetail = new ListDetail(store_number);
            try {
                masterStoreSelect.execute().get();
                //    Log.d(TAG, "onCreate: "+dto.getCustomer_email());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mainActivity, "인터넷이 연결되어 있지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }


        masterStoreAdapter = new MasterStoreAdapter(mainActivity.getApplicationContext(), registerDTOArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                mainActivity.getApplicationContext(), RecyclerView.VERTICAL, false
        );

        recyclerView = rootView.findViewById(R.id.master_recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(masterStoreAdapter);


        return rootView;

    }
}
