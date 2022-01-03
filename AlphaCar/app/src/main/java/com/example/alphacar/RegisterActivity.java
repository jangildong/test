package com.example.alphacar;

import static com.example.alphacar.Common.CommonMethod.ipConfig;
import static com.example.alphacar.LoginPageActivity.loginDTO;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alphacar.ATask.StoreRegister;
import com.lakue.lakuepopupactivity.PopupActivity;
import com.lakue.lakuepopupactivity.PopupGravity;
import com.lakue.lakuepopupactivity.PopupResult;
import com.lakue.lakuepopupactivity.PopupType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    LinearLayout layout;
    TextView textView;

    EditText et_store_name, et_store_master_name, et_store_registration_number, et_inventory, et_store_price,
            et_introduce, et_addr0,et_addr1, et_addr2, et_store_tel, et_store_time, et_store_dayoff;

    Button btnSearch_addr, btnRegister;

    ImageView iv_pic1, iv_pic2, iv_pic3;

    ImageButton btn_back;

    String customer_email;

    int regCheck = 0;

    int storeNameChk = 0, masterNameChk = 0, regNumChk = 0, storeTel = 0, timeChk = 0, offChk = 0;

    File imgFile = null;

    private int GALLEY_CODE = 10;
    private int CAMERA_CODE = 1004;

    private ArrayList<String> storePic = new ArrayList<>();
    private String profile;

    private ArrayList<String> storeInventory = new ArrayList<String>();


    //이미지 리얼패스
    private String getRealPathFromUri(Uri uri) {
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close(); return url;
    }

    //팝업으로 받아온 프로필 사진 처리 방법
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iv_pic1 = findViewById(R.id.register_iv_pic1);
        iv_pic2 = findViewById(R.id.register_iv_pic2);
        iv_pic3 = findViewById(R.id.register_iv_pic3);
        et_addr1 = findViewById(R.id.register_et_store_addr1);
        et_addr0 = findViewById(R.id.register_et_store_addr0);


        ArrayList<ImageView> list = new ArrayList<>();
        list.add(iv_pic1);
        list.add(iv_pic2);
        list.add(iv_pic3);

        if (resultCode == RESULT_OK) {
            PopupResult result = (PopupResult) data.getSerializableExtra("result");
            if(result == PopupResult.RIGHT){
                //closeOptionsMenu(); //테스트끝나면 쓸코드

                // 사진 찍기
                Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(picIntent.resolveActivity(getPackageManager()) != null){
                    imgFile = null;
                    // creatFile 매소드를 이용하여 임시파일을 만듬
                    imgFile = creatFile();

                    if(imgFile != null){
                        // API24 이상부터는 FileProvider를 제공해야함
                        Uri imgUri = FileProvider.getUriForFile(getApplicationContext(),
                                getApplicationContext().getPackageName()+".fileprovider",
                                imgFile);
                        // 만약에 API24 이상이면
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){ // API24
                            picIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                        }else {
                            picIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
                        }

                        startActivityForResult(picIntent, CAMERA_CODE);
                    }

                }

            } else if(result == PopupResult.LEFT){
                // 사진첩 접근
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //데이터를 uri로 받는다.
                startActivityForResult(intent, GALLEY_CODE);

            }
        }

        if(requestCode == GALLEY_CODE){

            if (resultCode == RESULT_OK) {

                //기존 이미지 지우기
                iv_pic1.setImageResource(0);
                iv_pic2.setImageResource(0);
                iv_pic3.setImageResource(0);

                //ClipData 또는 Uri를 가져온다
                Uri uri = data.getData();
                ClipData clipData = data.getClipData();

                //이미지 URI 를 이용하여 이미지뷰에 순서대로 세팅한다.
                if(clipData!=null){
                    if (clipData.getItemCount()>3){
                        Toast.makeText(getApplicationContext(), "사진은 3장까지 선택가능합니다", Toast.LENGTH_SHORT).show();

                    }
                    for(int i = 0; i < list.size(); i++) {
                        if(i<clipData.getItemCount()){
                            Uri urione =  clipData.getItemAt(i).getUri();
                            switch (i){
                                case 0:
                                    storePic.add(getRealPathFromUri(urione));
                                    iv_pic1.setImageURI(urione);
                                    break;
                                case 1:
                                    storePic.add(getRealPathFromUri(urione));
                                    iv_pic2.setImageURI(urione);
                                    break;
                                case 2:
                                    storePic.add(getRealPathFromUri(urione));
                                    iv_pic3.setImageURI(urione);
                                    break;
                            }
                        }
                    }
                }else if(uri != null){
                    storePic.add(getRealPathFromUri(data.getData()));
                    iv_pic1.setImageURI(uri);
                }
            }
        }
        if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            // 저장처리를 함
            //Toast.makeText(JoinPage.this, "사진이 잘 찍힘", Toast.LENGTH_SHORT).show();
            setPic();
        }

        switch (requestCode){
            case SEARCH_ADDRESS_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    String addr = data.getExtras().getString("data").trim();
                    String post = addr.substring(0,5);
                    String detail_addr = addr.substring(7);
                    if(data != null){
                        et_addr0.setText(post);
                        et_addr1.setText(detail_addr);
                    }
                }
                break;
        }

    }
    //사진 파일을 이름을 만듦
    private File creatFile() {
        // 파일 이름을 만들기 위해 시간값을 생성함
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = "My" + timestamp;
        // 사진파일을 저장하기 위한 경로
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File curFile = null;
        try {
            // 임시파일을 생성함(전체경로),  2번째 suffix 확장자:파일확장자(jpg)
            curFile = File.createTempFile(imageFileName, ".jpg"
                    , storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 스트링타입으로 임시파일이 있는 곳의 절대경로를 저장함
        profile = curFile.getAbsolutePath();

        return curFile;
    }
    // 사진을 저장처리 하는 곳
    private void setPic() {
        // 이미지뷰의 크기 알아오기
        int targetW = iv_pic1.getWidth();
        int targetH = iv_pic1.getHeight();

        // 사진의 크기 가져오기
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int photoW = options.outWidth;
        int photoH = options.outHeight;

        // 이미지 크기를 맟출비율을 결정
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // 이미지뷰의 크기에 맞게 이미지크기를 조절
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        // 비트맵 이미지를 생성
        Bitmap bitmap = BitmapFactory.decodeFile(profile);
        // 이미지를 갤러리에 저장하기
        gelleryAddPic(bitmap);
        Glide.with(this).load(bitmap).into(iv_pic1);



    }
    // 이미지를 갤러리에 저장하기
    private void gelleryAddPic(Bitmap bitmap) {
        FileOutputStream fos;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){ // API29
            ContentResolver resolver = getContentResolver();

            // 맵구조를 가진 ContentValues : 파일정보를 저장함
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,
                    "Image_" + "jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,
                    "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + "TestFolder");

            Uri imageUri = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues );
            try {
                fos = (FileOutputStream) resolver
                        .openOutputStream(Objects.requireNonNull(imageUri));
                //Toast.makeText(JoinPage.this, "fos 작업됨", Toast.LENGTH_SHORT).show();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);

            }catch (Exception e){

            }

        }else {
            // 이미지 파일을 스캔해서 갤러리에 저장하기 위한 인텐트
            Intent msIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            // 처음에 CreateFile에서 생성해둔 이미지경로(imgFilePath)를 이용하여 파일객체를 만든다
            Uri contentUri = Uri.fromFile(imgFile);
            File f = new File(profile);
            msIntent.setData(contentUri);
            // sendBroadcast를 이용하여 저장
            this.sendBroadcast(msIntent);
        }
    }


    //onCreate
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        checkDangerousPermissions();

        btn_back = findViewById(R.id.btn_back);
        btnSearch_addr = findViewById(R.id.register_btn_search_addr);
        customer_email = loginDTO.getCustomer_email();




        //상단 뒤로가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSearch_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, WebSearch.class);
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY );
            }
        });

        et_store_name = findViewById(R.id.register_et_store_name);
        et_store_master_name = findViewById(R.id.register_et_store_master_name);
        et_store_registration_number = findViewById(R.id.register_et_store_registration_number);
        et_inventory = findViewById(R.id.register_et_inventory);
        et_store_price = findViewById(R.id.register_et_store_price);
        et_introduce = findViewById(R.id.register_et_introduce);
        et_addr0 = findViewById(R.id.register_et_store_addr0);
        et_addr1 = findViewById(R.id.register_et_store_addr1);
        et_addr2 = findViewById(R.id.register_et_store_addr2);
        et_store_tel = findViewById(R.id.register_et_store_tel);
        et_store_time = findViewById(R.id.register_et_store_time);
        et_store_dayoff = findViewById(R.id.register_et_store_dayoff);

        btnRegister = findViewById(R.id.btnRegister);




        //등록 버튼 클릭 이벤트
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String store_name = et_store_name.getText().toString();
                String store_master_name = et_store_master_name.getText().toString();
                String regnum = et_store_registration_number.getText().toString();
                int inventory = 0;
                if (!et_inventory.getText().toString().equals("")) {
                    inventory = Integer.parseInt(et_inventory.getText().toString());
                }
                String store_price = et_store_price.getText().toString();
                String introduce = et_introduce.getText().toString();
                String store_post = et_addr0.getText().toString();
                String store_addr = et_addr1.getText().toString();
                String store_detail_addr = et_addr2.getText().toString();
                String store_telnum = et_store_tel.getText().toString();
                String store_time = et_store_time.getText().toString();
                String store_dayoff = et_store_dayoff.getText().toString();

                String first = store_telnum.substring(0, 3);
                String second = store_telnum.substring(3, 7);
                String third = store_telnum.substring(7);
                String store_tel = first + "-" + second + "-" + third;

                first = regnum.substring(0, 3);
                second = regnum.substring(4, 9);
                third = regnum.substring(9);
                String store_registration_number = first + "-" + second + "-" + third;

                if (store_name.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "가게 명을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_store_name.requestFocus();
                    return;
                }
                if (store_master_name.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "대표 명을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_store_master_name.requestFocus();
                    return;
                }
                if (store_registration_number.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "사업자 등록 번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    et_store_registration_number.requestFocus();
                    return;
                }
                if (inventory == 0) {
                    Toast.makeText(RegisterActivity.this, "베이 수를 입력하세요", Toast.LENGTH_SHORT).show();
                    et_inventory.requestFocus();
                    return;
                }
                if (store_price.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "가격을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_store_price.requestFocus();
                    return;
                }
                if (introduce.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "가게 소개를 입력하세요", Toast.LENGTH_SHORT).show();
                    et_introduce.requestFocus();
                    return;
                }
                if (store_post.length() == 0 && store_detail_addr.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "주소를 입력하세요", Toast.LENGTH_SHORT).show();
                    et_introduce.requestFocus();
                    return;
                }
                if (store_tel.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "연락처를 입력하세요", Toast.LENGTH_SHORT).show();
                    et_addr2.requestFocus();
                    return;
                }
                if (store_time.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "영업 시간을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_store_time.requestFocus();
                    return;
                }

                if (store_dayoff.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "휴무일을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_store_dayoff.requestFocus();
                    return;
                }

                if(storeNameChk == 0 && masterNameChk == 0 && regNumChk ==0 && storeTel == 0 && timeChk ==0 && offChk ==0){
                    regCheck = 0;
                }
                else {
                    regCheck = 1;
                }

                if(regCheck == 0) {
                      if(storePic.size() != 0) {
                            StoreRegister register = new StoreRegister(customer_email, store_name, store_post, store_addr,
                                    store_detail_addr, store_tel, store_time, store_dayoff, introduce, inventory, store_price,
                                    store_master_name, store_registration_number, storePic);
                            String state = "";
                            try {
                                state = register.execute().get().trim();
                                Log.d("main:joinact0 : ", state);
                                state = state.substring(11, 12);
                                Log.d("main:joinact1 : ", state);
                            } catch (ExecutionException e) {
                                //e.printStackTrace();
                            } catch (InterruptedException e) {
                                //e.printStackTrace();
                            }


                            if (state.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "삽입성공 !!!", Toast.LENGTH_SHORT).show();
                                Log.d("main:joinact", "삽입성공 !!!");
                                finish();
                            }
                            //fix
                            else {
                                Toast.makeText(RegisterActivity.this, "삽입실패 !!!", Toast.LENGTH_SHORT).show();
                                Log.d("main:joinact", "삽입성공 !!!");
                                finish();
                            }
                        } else{
                            Toast.makeText(RegisterActivity.this, "사진을 넣어주세요, 사진은 3장이 들어가야 합니다", Toast.LENGTH_SHORT).show();
                        }
                }else {
                    if(storeNameChk == 1){
                        Toast.makeText(RegisterActivity.this, "가게명을 확인하세요", Toast.LENGTH_SHORT).show();
                        et_store_name.requestFocus();

                    }
                    else if(masterNameChk == 1){
                        Toast.makeText(RegisterActivity.this, "대표명을 확인하세요", Toast.LENGTH_SHORT).show();
                        et_store_master_name.requestFocus();
                    }
                    else if(regNumChk == 1){
                        Toast.makeText(RegisterActivity.this, "사업자 번호를 확인하세요", Toast.LENGTH_SHORT).show();
                        et_store_registration_number.requestFocus();
                    }
                    else if(storeTel == 1){
                        Toast.makeText(RegisterActivity.this, "연락처를 확인하세요", Toast.LENGTH_SHORT).show();
                        et_store_tel.requestFocus();
                    }
                    else if(timeChk == 1){
                        Toast.makeText(RegisterActivity.this, "영업시간을 확인하세요", Toast.LENGTH_SHORT).show();
                        et_store_time.requestFocus();
                    }
                    else if(offChk == 1){
                        Toast.makeText(RegisterActivity.this, "휴무일을 확인하세요", Toast.LENGTH_SHORT).show();
                        et_store_dayoff.requestFocus();
                    }
                }

            }
        });
     /*   Editable inventory = et_inventory.getText();
        Editable store_registration_number = et_store_registration_number.getText();
        Editable store_tel = et_store_tel.getText();
        Editable store_time = et_store_time.getText();
*/
        int white = ContextCompat.getColor(getApplicationContext(), R.color.white);
        int red = ContextCompat.getColor(getApplicationContext(), R.color.red);

        //가게명 유효성
        et_store_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String storeName = et_store_name.getText().toString();
                if(!Pattern.matches("^[a-z|A-Z|가-힣|0-9]*$",storeName)) {
                    et_store_name.setTextColor(red);
                    storeNameChk = 1;
                }else{
                    et_store_name.setTextColor(white);
                    storeNameChk = 0;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //대표자명 유효성
        et_store_master_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = et_store_master_name.getText().toString();
                if(!Pattern.matches("^[가-힣]*$",name)) {
                    et_store_master_name.setTextColor(red);
                    masterNameChk = 1;
                }else{
                    et_store_master_name.setTextColor(white);
                    masterNameChk = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //사업자번호 유효성
        et_store_registration_number.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String regnum = et_store_registration_number.getText().toString();
            if(!Pattern.matches("^[0-9]{10}$",regnum)){

                et_store_registration_number.setTextColor(red);
                regNumChk = 1;
            }else{
                et_store_registration_number.setTextColor(white);
                regNumChk = 0;
            }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        //연락처 유효성
        et_store_tel.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String telnum = et_store_tel.getText().toString();
                if(!Pattern.matches("^[0-9]{11}$",telnum)){
                    et_store_tel.setTextColor(red);
                    storeTel = 1;
                }else{
                    et_store_tel.setTextColor(white);
                    storeTel = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //영업시간 유효성
        et_store_time.addTextChangedListener(new TextWatcher() {
            String store_time = et_store_time.getText().toString();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!Pattern.matches("^[0-9.~:]*$",store_time)){
                    et_store_time.setTextColor(red);
                    timeChk = 1;

                }else{
                    et_store_time.setTextColor(white);
                    timeChk = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //휴무일 유효성
        et_store_dayoff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String off = et_store_dayoff.getText().toString();
                if(!Pattern.matches("^[가-힣]*$", off)){
                    et_store_dayoff.setTextColor(red);
                    offChk = 1;
                }else{
                    et_store_dayoff.setTextColor(white);
                    offChk = 0;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


//        textView = findViewById(R.id.register);

//        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                //사진관련 처리-------------------------------------------------------------------

                iv_pic1 = findViewById(R.id.register_iv_pic1);
        iv_pic2 = findViewById(R.id.register_iv_pic2);
        iv_pic3 = findViewById(R.id.register_iv_pic3);

        ArrayList<ImageView> list = new ArrayList<>();
        list.add(iv_pic1);
        list.add(iv_pic2);
        list.add(iv_pic3);

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(getBaseContext(), PopupActivity.class);
                    intent.putExtra("type", PopupType.SELECT);
                    intent.putExtra("gravity", PopupGravity.LEFT);
                    intent.putExtra("title", "세차장 사진");
                    intent.putExtra("content", "새로운 사진을 찍거나 사진을 가져올 수 있습니다.");
                    intent.putExtra("buttonLeft", "불러오기");
                    intent.putExtra("buttonRight", "사진찍기");
                    startActivityForResult(intent, 2);

                }
            });
        }
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}