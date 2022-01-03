package com.example.alphacar;

import static com.example.alphacar.R.color.red;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alphacar.ATask.IdCheck;
import com.example.alphacar.ATask.JoinInsert;
import com.example.alphacar.ATask.JoinNoImgInsert;
import com.lakue.lakuepopupactivity.PopupActivity;
import com.lakue.lakuepopupactivity.PopupGravity;
import com.lakue.lakuepopupactivity.PopupResult;
import com.lakue.lakuepopupactivity.PopupType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class JoinPageActivity extends AppCompatActivity {

    EditText memberjoin_et_email, memberjoin_et_pw, memberjoin_et_name, memberjoin_et_pw2;
    CheckBox memberjoin_cb_company;
    TextView memberjoin_bt_join;
    boolean emailCheck = true;
    ImageButton btn_back;

    private int GALLEY_CODE = 10;
    private int CAMERA_CODE = 1004;
    private String profile = null;
    File imgFile = null;
    ImageView memberjoin_iv_profile;
    String imgFilePath = "";

    int join_check = 0;

    int emailChk = 0, passChk = 0, passChk2 = 0;

    //이미지 리얼패스
    private String getRealPathFromUri(Uri uri) {
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String url = cursor.getString(columnIndex);

        cursor.close();
        return url;
    }

    private void takePicture(){
        int permissionCheck = ContextCompat.checkSelfPermission(JoinPageActivity.this, Manifest.permission.CAMERA);

        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            // 권한없음
            ActivityCompat.requestPermissions(JoinPageActivity.this, new String[]{Manifest.permission.CAMERA},0);
        }else{
            // 암묵적인텐트 : 사진찍기(카메라를 불러옴)
            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 일단 이 인텐트가 사용가능한지 체크
            if(picIntent.resolveActivity(getPackageManager()) != null){
                imgFile = null;
                // createFile 매소드를 이용하여 임시파일을 만듬
                imgFile = creatFile();

                if(imgFile != null){
                    // API24 이상부터는 FileProvider 를 제공해야함
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
        }

    }

    //팝업으로 받아온 프로필 사진 처리 방법
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        memberjoin_iv_profile = findViewById(R.id.memberjoin_iv_profile);

   //     if (resultCode == RESULT_OK) {
            PopupResult result = (PopupResult) data.getSerializableExtra("result");
            if(result == PopupResult.LEFT){
                takePicture();
              /*  // 사진 찍기
                Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(picIntent.resolveActivity(getPackageManager()) != null){
                    // imgFile = null;
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

                }*/

            } else if(result == PopupResult.RIGHT){
                // 사진첩 접근
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLEY_CODE);
            }
//        }
        if(requestCode == GALLEY_CODE){
                    profile = getRealPathFromUri(data.getData());
            RequestOptions cropOptions = new RequestOptions();
            Glide.with(getApplicationContext())
                    .load(profile)  //사진의 절대 경로
                    .apply(cropOptions.optionalCircleCrop()) //원형태로 변경
                    .into(memberjoin_iv_profile); //imageView에 출력함.
        }

        if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            // 저장처리를 함
            //Toast.makeText(JoinPage.this, "사진이 잘 찍힘", Toast.LENGTH_SHORT).show();
            setPic();
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
        int targetW = memberjoin_iv_profile.getWidth();
        int targetH = memberjoin_iv_profile.getHeight();

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
        Glide.with(this).load(bitmap).circleCrop().into(memberjoin_iv_profile);



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
            File f = new File(profile);
            Uri contentUri = Uri.fromFile(f);
            msIntent.setData(contentUri);
            // sendBroadcast를 이용하여 저장
            this.sendBroadcast(msIntent);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_page);

        memberjoin_et_email = findViewById(R.id.memberjoin_et_email);
        memberjoin_et_pw = findViewById(R.id.memberjoin_et_pw);
        memberjoin_et_pw2 = findViewById(R.id.memberjoin_et_pw2);
        memberjoin_et_name = findViewById(R.id.memberjoin_et_name);
        memberjoin_cb_company = findViewById(R.id.memberjoin_cb_company);
        memberjoin_iv_profile = findViewById(R.id.memberjoin_iv_profile);
        memberjoin_bt_join = findViewById(R.id.memberjoin_bt_join);

        Glide.with(this).load(R.drawable.guest2).circleCrop().into(memberjoin_iv_profile); //사람 모양 이미지 둥글게 넣기

        btn_back = findViewById(R.id.btn_back);

        //상단 뒤로가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //프로필 사진 클릭시
        memberjoin_iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PopupActivity.class);
                intent.putExtra("type", PopupType.SELECT);
                intent.putExtra("gravity", PopupGravity.LEFT);
                intent.putExtra("title", "프로필 사진");
                intent.putExtra("content", "새로운 사진을 찍거나 사진을 가져올 수 있습니다.");
                intent.putExtra("buttonLeft", "사진찍기");
                intent.putExtra("buttonRight", "불러오기");
                startActivityForResult(intent, 2);
            }
        });


        //가입하기 버튼 클릭
        memberjoin_bt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = memberjoin_et_email.getText().toString();
                String passwd = memberjoin_et_pw.getText().toString();
                String passwd2 = memberjoin_et_pw2.getText().toString();
                String name = memberjoin_et_name.getText().toString();
                String admin;


                //이멜 입력 안했을 때 경고창
                if(id.length() == 0){
                    Toast.makeText(JoinPageActivity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    memberjoin_et_email.requestFocus();
                    return;
                }
                //비번을 입력안했을 때 경고창
                if(passwd.length() == 0){
                    Toast.makeText(JoinPageActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    memberjoin_et_pw.requestFocus();
                    return;
                }
                //이름 입력안했을 때 경고창
                if(name.length() == 0) {
                    Toast.makeText(JoinPageActivity.this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    memberjoin_et_name.requestFocus();
                    return;
                }
                //비밀번호 확인 경고창
                if(!passwd.equals(passwd2)) {
                    Toast.makeText(JoinPageActivity.this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    memberjoin_et_pw.requestFocus();
                    return;
                }
                //사업자 유무
                if(memberjoin_cb_company.isChecked()){
                    admin = "M";
                }else{
                    admin = "C";
                }

                //이메일 형식 체크
                /*if(!Patterns.EMAIL_ADDRESS.matcher(id).matches()){
                    Toast.makeText(JoinActivity.this, "이메일 형식을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    memberjoin_et_name.requestFocus();
                    return;
                }*/
                //비밀번호 유효성 검사
                /*if(!Pattern.matches(
                        "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{6,15}.$", passwd)){
                    Toast.makeText(JoinActivity.this, "비밀번호 형식을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    memberjoin_et_pw.requestFocus();
                    return;
                }*/

                //이메일 중복 시 로그안 안됨.
                if(emailCheck == false){
                    Toast.makeText(JoinPageActivity.this, "중복된 이메일 입니다.", Toast.LENGTH_SHORT).show();
                    memberjoin_et_name.requestFocus();
                    return;
                }

                //회원가입 처리 시작
                JoinInsert joinInsert = null;
                JoinNoImgInsert joinNoImgInsert = null;
                if(emailChk == 0 && passChk == 0 && passChk2 == 0){
                    join_check = 0;
                }else {
                    join_check = 1;
                }

                if(join_check == 0){
                    if (profile == null) {
                        joinNoImgInsert = new JoinNoImgInsert(id, passwd, name, admin);
                    } else {
                        joinInsert = new JoinInsert(id, passwd, name, admin, profile);
                    }

                    String state = "";
                    try {
                        if(profile == null){
                            state = joinNoImgInsert.execute().get().trim();

                        }else{
                            state = joinInsert.execute().get().trim();
                        }
                        Log.d("main:joinact0 : ", state);
                        state = state.substring(11, 12);
                        Log.d("main:joinact1 : ", state);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(state.equals("1")){
                        Toast.makeText(JoinPageActivity.this, "가입성공!", Toast.LENGTH_SHORT).show();
                        Log.d("main:joinact", "가입성공!");
                        finish();
                    }else{
                        Toast.makeText(JoinPageActivity.this, "가입실패!", Toast.LENGTH_SHORT).show();
                        Log.d("main:joinact", "가입실패!");
                        finish();
                    }

                } else {
                    if(emailChk == 1){
                        Toast.makeText(JoinPageActivity.this, "이메일을 확인하세요", Toast.LENGTH_SHORT).show();
                        memberjoin_et_email.requestFocus();
                    } else if(passChk == 1 || passChk2 == 1){
                        Toast.makeText(JoinPageActivity.this,"비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                        memberjoin_et_pw.requestFocus();
                    }
                }



                //가입 성공 여부

            }
        });

        //아이디 중복 체크
        memberjoin_et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String id = memberjoin_et_email.getText().toString();
                IdCheck idCheck = new IdCheck(id);
                int white = ContextCompat.getColor(getApplicationContext(), R.color.white);
                int red = ContextCompat.getColor(getApplicationContext(), R.color.red);

                String state = "";
                try {
                    state = idCheck.execute().get().trim();
                    Log.d("main:joinact0 : ", state);
                    state = state.substring(11, 12);
                    Log.d("main:joinact1 : ", state);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(state.equals("0")) {
                    memberjoin_et_email.setTextColor(white);
                    emailCheck = true;
                    emailChk = 0;
                }else{
                    memberjoin_et_email.setTextColor(red);
                    emailCheck = false;
                    emailChk = 1;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches())
                {
                    memberjoin_et_email.setTextColor(red);
                    emailChk = 1;
                }


            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //비밀번호와 비밀번호 확인이 서로 맞지 않는 경우 textcolor를 red로 변경
        memberjoin_et_pw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwd = memberjoin_et_pw.getText().toString();
                String passwd2 = memberjoin_et_pw2.getText().toString();
                int white = ContextCompat.getColor(getApplicationContext(), R.color.white);
                int red = ContextCompat.getColor(getApplicationContext(), R.color.red);
                if(!passwd.equals(passwd2)) {
                    memberjoin_et_pw2.setTextColor(red);
                    passChk2 = 1;
                }else{
                    memberjoin_et_pw2.setTextColor(white);
                    passChk2 = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //비밀번호 유효성 검사 할때 틀리면 빨간색으로 변경
        memberjoin_et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwd = memberjoin_et_pw.getText().toString();
                int white = ContextCompat.getColor(getApplicationContext(), R.color.white);
                int red = ContextCompat.getColor(getApplicationContext(), R.color.red);

                if(!Pattern.matches(
                        "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{6,15}.$", passwd)) {
                    memberjoin_et_pw.setTextColor(red);
                    memberjoin_et_pw2.setTextColor(red);
                    passChk = 1;
                }else{
                    memberjoin_et_pw.setTextColor(white);
                    memberjoin_et_pw2.setTextColor(white);
                    passChk = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

     //   memberjoin_et_email
        CharSequence cs  = memberjoin_et_pw.getText().toString();
        /*memberjoin_et_pw2
        memberjoin_et_name
        memberjoin_cb_company
        memberjoin_iv_profile
        memberjoin_bt_join*/








    }


}