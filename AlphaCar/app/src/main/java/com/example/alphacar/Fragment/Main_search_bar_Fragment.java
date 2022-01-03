package com.example.alphacar.Fragment;

import static com.example.alphacar.Common.CommonMethod.isNetworkConnected;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.alphacar.ATask.Storename;
import com.example.alphacar.Adapter.StoreAdapter;
import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.DetailActivity;
import com.example.alphacar.MainActivity;
import com.example.alphacar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Main_search_bar_Fragment extends Fragment {

    MainActivity mainActivity;
    ListView listView;
    SearchView searchView;
    Storename storename;
    ArrayList<StoreDTO> storeDTOArrayList;
    StoreAdapter sAdapter;
    StoreDTO storeDTO;
    String[] store_name;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_bar_list,
                container,false);

        storeDTOArrayList = new ArrayList<>();

        listView =rootView.findViewById(R.id.listview);
        searchView =rootView.findViewById(R.id.searchView);


        mainActivity = (MainActivity) getActivity();

        searchBar("");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                storeDTO = (StoreDTO) sAdapter.getItem(position);
                Intent intent = new Intent(mainActivity, DetailActivity.class);
//                intent.putExtra("storeDTO",storeDTO.getStore_name());
                intent.putExtra("store_number",storeDTOArrayList.get(position).getStore_number());
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                storeDTOArrayList = new ArrayList<>();
                searchBar(query);
                listView.setVisibility(View.GONE);

                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {

                if(query.trim().length() == 0){
                    storeDTOArrayList = new ArrayList<>();
                    sAdapter = new StoreAdapter(getContext(),storeDTOArrayList);
                    listView.setAdapter(sAdapter);
                    sAdapter.notifyDataSetChanged();

                    listView.setVisibility(View.GONE);

                }else{
                    searchBar(query);
                    listView.setVisibility(View.VISIBLE);
                }



                return false;
            }
        });



        return rootView;
    }


    /* 검색 보여주기 메소드*/
    public void searchBar(String query){
        storeDTOArrayList = new ArrayList<>();
        /* 검색바 */
        if(isNetworkConnected(getContext()) == true){
            storename = new Storename(storeDTOArrayList,query);
            try {
                storename.execute().get();
                //    Log.d(TAG, "onCreate: "+dto.getCustomer_email());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(), "인터넷이 연결되어 있지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
        if(storeDTOArrayList != null){



//            for (int i = 0; i < store_name.length; i++) {
//               storeDTO  = new StoreDTO(store_name[i]);
//                // Binds all strings into an array
//                arraylist.add(animalNames);
//            }


            sAdapter = new StoreAdapter(getContext(),storeDTOArrayList);
            listView.setAdapter(sAdapter);
            sAdapter.notifyDataSetChanged();
//            Toast.makeText(getContext(), dto.getStore_name(), Toast.LENGTH_SHORT).show();

        }

    }



}


