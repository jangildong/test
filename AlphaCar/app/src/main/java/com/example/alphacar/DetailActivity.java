package com.example.alphacar;




import static com.example.alphacar.Common.CommonMethod.isNetworkConnected;
import static com.example.alphacar.LoginPageActivity.loginDTO;



import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.alphacar.ATask.DetailAtask;
import com.example.alphacar.ATask.FavoriteCheck;
import com.example.alphacar.ATask.FavoriteCntUpdate;
import com.example.alphacar.ATask.FavoriteDelect;
import com.example.alphacar.ATask.FavoriteInsert;
import com.example.alphacar.ATask.ReviewSelect;
import com.example.alphacar.Adapter.DetailAdapter;
import com.example.alphacar.Adapter.Detail_SliderAdapter;
import com.example.alphacar.Common.RetrofitCommon;
import com.example.alphacar.DTOS.ReviewDTO;
import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.Fragment.DetailPagerFragment;
import com.example.alphacar.Fragment.MapFragment;
import com.example.alphacar.retro.StoreService;
import com.lakue.lakuepopupactivity.PopupActivity;
import com.lakue.lakuepopupactivity.PopupGravity;
import com.lakue.lakuepopupactivity.PopupResult;
import com.lakue.lakuepopupactivity.PopupType;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity{
    //사업장 정보를 id값 기준으로 가져와야함
    ReviewSelect reviewSelect;
    FavoriteInsert favoriteInsert;
    FavoriteCntUpdate favoriteCntUpdate;
    FavoriteCheck favoriteCheck;
    FavoriteDelect favoriteDelect;
    ViewPager pager;

    ArrayList<StoreDTO> store_list;

    //ArrayList<String> review = new ArrayList<>();
    // List<String> review = new ArrayList<>();
    String[] review;
    StoreDTO dto;
    ReviewDTO rdto;
    ArrayList<ReviewDTO> dtos ;

    //ImageButton like_btn;
    ImageView like_btn;
    ImageView detail_map, detail_star_point, detail_back,detail_home;

    MapFragment mapFragment;

    DetailAtask detailAtask;

    TextView store_name;
    TextView store_price;
    TextView store_addr;
    TextView detail_now_btn;
    TextView detail_review_btn;
  //  String customer_email= "";


    int store_number;

    Retrofit retrofit;
    StoreService storeService;




    ScrollView scrollView;



    //-----------expandableListView----------
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> store_info;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    //-----------expandableListView------

    public static String store_address = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            PopupResult result = (PopupResult) data.getSerializableExtra("result");
            if(result == PopupResult.LEFT){
                Intent intent = new Intent(DetailActivity.this, LoginPageActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        String state = "";

        detail_now_btn = findViewById(R.id.detail_now_btn);
        detail_review_btn = findViewById(R.id.detail_review_btn);
        detail_star_point = findViewById(R.id.detail_star_point);

        like_btn = findViewById(R.id.detail_favorite_btn);
        pager = findViewById(R.id.image_container);

        detail_back = findViewById(R.id.detail_back);
        detail_home = findViewById(R.id.detail_home);

        scrollView = findViewById(R.id.detail_scroll);

        store_name = findViewById(R.id.detail_store_name);
        store_price = findViewById(R.id.detail_store_price);
        store_addr = findViewById(R.id.detail_store_addr);

        dtos = new ArrayList<>();
        store_list = new ArrayList<StoreDTO>();
        dto = new StoreDTO();
        rdto = new ReviewDTO();


        Intent intent = getIntent();
        store_number = intent.getIntExtra("store_number", 0);


        //영업장 정보 불러오는 atask
        detailAtask = new DetailAtask(store_number,store_list);
        try {
             store_list = (ArrayList<StoreDTO>) detailAtask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }



        //리뷰 정보 불러오는 atask
        if(isNetworkConnected(this) == true){
            reviewSelect = new ReviewSelect(store_number,dtos,rdto);
            try {
               reviewSelect.execute().get();
                //     reviewSelect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, "인터넷이 연결되어 있지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }

   /*     allAtask = new AllAtask(store_number, dtos, rdto);
        try {
            allAtask.execute().get();
            //    reviewSelect.execute().get();
            //     reviewSelect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        if(loginDTO != null) {
        if (isNetworkConnected(DetailActivity.this) == true) {
            favoriteCheck = new FavoriteCheck(loginDTO.getCustomer_email(), store_list.get(0).getStore_number());
            //listDetail = new ListDetail(store_number);
            try {
                state = favoriteCheck.execute().get().trim();
                state = state.substring(11, 12);
                //    Log.d(TAG, "onCreate: "+dto.getCustomer_email());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(DetailActivity.this, "인터넷이 연결되어 있지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }

        if (state.equals("1")) {
            like_btn.setImageResource(R.drawable.ic_baseline_favorite_red_24);
            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isNetworkConnected(DetailActivity.this) == true){

                        favoriteDelect = new FavoriteDelect(store_list.get(0).getFav_number());
                        //listDetail = new ListDetail(store_number);
                        try {
                            favoriteDelect.execute().get();
                            like_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
                            //    Log.d(TAG, "onCreate: "+dto.getCustomer_email());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }



                    }

                    if (isNetworkConnected(DetailActivity.this) == true) {
                        favoriteCntUpdate = new FavoriteCntUpdate(store_list.get(0).getStore_number());
                        //listDetail = new ListDetail(store_number);
                        try {
                            favoriteCntUpdate.execute().get();
                            //    Log.d(TAG, "onCreate: "+dto.getCustomer_email());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(DetailActivity.this, "인터넷이 연결되어 있지 않습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }else{
            like_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
            if(loginDTO != null) {
                like_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNetworkConnected(DetailActivity.this) == true){
                            favoriteInsert  = new FavoriteInsert(loginDTO.getCustomer_email(), store_list.get(0).getStore_number());
                            //    reviewSelect = new ReviewSelect(customer_email,dtos,rdto);
                            try {
                                favoriteInsert.execute().get();
                                Toast.makeText(DetailActivity.this, "즐겨찾기 추가 !!!", Toast.LENGTH_SHORT).show();
                                like_btn.setImageResource(R.drawable.ic_baseline_favorite_red_24);
                                //    reviewSelect.execute().get();
                                //     reviewSelect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(DetailActivity.this, "인터넷이 연결되어 있지 않습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (isNetworkConnected(DetailActivity.this) == true) {
                            favoriteCntUpdate = new FavoriteCntUpdate(store_list.get(0).getStore_number());
                            //listDetail = new ListDetail(store_number);
                            try {
                                favoriteCntUpdate.execute().get();
                                //    Log.d(TAG, "onCreate: "+dto.getCustomer_email());
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DetailActivity.this, "인터넷이 연결되어 있지 않습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        //유저 메일 = dd 인 사람이 현재 가게를 등록한다
                        //1. 즐겨찾기 버튼은 로그인 된 상태에서만 활성화 된다
                        //2. 즐겨찾기 버튼을 누르면 사용자의 이메일을 기준으로 favorite에 값이 담긴다
                        //insert into favorite values (FAV_NUMBER ,CUSTOMER_EMAIL ,STORE_NUMBER)
                        //select * from favorite where customer_email
                    }
                });
            }
        }

    }

       //별점 가져오기
        int avg = 0;
        int sum =0;
       for (int i = 0; i < dtos.size(); i++){
          String score =dtos.get(i).getReview_score().substring(0,1);
          int j = dtos.size();
          int iScore = Integer.parseInt(score);
          sum += iScore;
          double dScore = sum/j;
          avg = (int) Math.round(dScore);

        //  avg += score;
       }
       switch (avg){
           case 1:
               detail_star_point.setImageResource(R.drawable.one_star);
               break;
           case 2:
               detail_star_point.setImageResource(R.drawable.two_star);
               break;
           case 3:
               detail_star_point.setImageResource(R.drawable.three_star);
               break;
           case 4:
               detail_star_point.setImageResource(R.drawable.four_star);
               break;
           case 5:
               detail_star_point.setImageResource(R.drawable.five_star);
               break;
       }



        //////////


        store_name.setText(store_list.get(0).getStore_name());
        store_price.setText(store_list.get(0).getStore_price());
        store_addr.setText(store_list.get(0).getStore_addr());

        store_address = store_list.get(0).getStore_addr();


/*        store_name.setText(dto.getStore_name());
        store_price.setText(dto.getStore_price());
        store_addr.setText(dto.getStore_addr());*/

        detail_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, NowActivity.class);
                intent.putExtra("store_number", store_list.get(0).getStore_number());
                startActivity(intent);
            }
        });

        detail_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(intent1);
            }
        });

        detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //이메일과 스토어 넘버 비교 loginDTO email == favdto email && store_number == favdto_store_number  => 색 바뀜


        //좋아요 버튼 누르면 FAV_NUMBER, CUSTOMER_EMAIL, STORE_NUMBER값을 Favorite테이블에 넣어줌





        ArrayList<Fragment> list = new ArrayList<>();
        for (int i = 0; i <store_list.size(); i++ ){
            list.add(new DetailPagerFragment(store_list.get(i)));
        }

        //어댑터에 리스트를 담아서 보내줌
       Detail_SliderAdapter adapter = new Detail_SliderAdapter(getSupportFragmentManager());
        adapter.setList(list);
        pager.setAdapter(adapter);


        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.map_layout, mapFragment).commit();



        //-----------------------expandableListView--------------------

        //소개, 리뷰를 가져와서 넣어야함

        //부모리스트 생성
        createGroupList();
        //부모리스트에 자식리스트 넣기
        createCollection();


        expandableListView = findViewById(R.id.store_info_main);
        expandableListAdapter = new DetailAdapter(this, groupList, store_info);
        expandableListView.setAdapter(expandableListAdapter);

        //스크롤뷰 안에 리스트 뷰가 들어있을때 리스트 뷰 스크롤을 사용할때 메인 스크롤이 움직이지 않게 하는 메소드
        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //부모리스트를 펼쳤을때
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            //리뷰 눌렀을때 디테일하게 보기
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(getApplicationContext() , ReviewPageActivity.class);
                intent.putExtra("review_id" , dtos.get(i1).getReview_id());
                startActivity(intent);
                return true;
            }
        });

        detail_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginDTO != null) {
                    Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                    intent.putExtra("store_number", store_list.get(0).getStore_number());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getBaseContext(), PopupActivity.class);
                    intent.putExtra("type", PopupType.SELECT);
                    intent.putExtra("gravity", PopupGravity.LEFT);
                    intent.putExtra("title", "알림");
                    intent.putExtra("content", "로그인이 필요한 서비스 입니다");
                    intent.putExtra("buttonLeft", "로그인");
                    intent.putExtra("buttonRight", "뒤로가기");
                    startActivityForResult(intent, 2);
                }
            }
        });



        //-----------------------expandableListView--------------------
    }

    private long backpressedTime = 0;

    @Override
    public void onBackPressed() {


        finish();

        /*if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }*/

    }
    //-----------------------expandableListView--------------------
    private void createCollection() {

        //소개, 리뷰를 가져와서 넣어야함

      //  String[] intro = {dto.getIntroduce(),"쉬는 날 :" + dto.getStore_dayoff(),"영업 시간 : " + dto.getStore_time()};//dto.getIntroduce()
        String[] intro = {store_list.get(0).getIntroduce(),"쉬는 날 :" + store_list.get(0).getStore_dayoff(),"영업 시간 : " + store_list.get(0).getStore_time()};//dto.getIntroduce()



        review = new String[dtos.size()];

        for(int i = 0; i <dtos.size(); i++){
            review[i] = dtos.get(i).getReview_content();
        }
        //일단 arrayDTO 사이즈만큼 배열 크기 주고 for문 돌리기


        //String에 부모리스트, List<String>에 자식리스트
        store_info = new HashMap<String, List<String>>();


        for(String group : groupList){
            if (group.equals("소개")){
                loadChild(intro);
            } else if (group.equals("리뷰"))
                loadChild(review);
            store_info.put(group, childList);
        }
    }

    //부모리스트 안에 자식 리스트 담는 메소드
    private void loadChild(String[] store_info) {
        childList = new ArrayList<>();
        for(String model : store_info){
            childList.add(model);
        }
    }

    //부모리스트 생성 메소드
    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add("소개");
        groupList.add("리뷰");

    }
    //-----------------------expandableListView--------------------

}
