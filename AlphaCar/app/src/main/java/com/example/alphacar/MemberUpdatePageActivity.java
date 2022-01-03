package com.example.alphacar;

import static com.example.alphacar.LoginPageActivity.loginDTO;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
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
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alphacar.ATask.LoginAtask;
import com.example.alphacar.ATask.LoginSelect;
import com.example.alphacar.ATask.MemberUpdate;
import com.example.alphacar.DTOS.MemberVO;
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


public class MemberUpdatePageActivity extends AppCompatActivity {

    EditText memberupdate_et_email, memberupdate_et_name, memberupdate_et_oldpw, memberupdate_et_newpw, memberupdate_et_newpw2;
    CheckBox memberupdate_cb_company;
    TextView memberupdate_bt_update;
    ImageButton btn_back;

    int joinUpChk = 0;

    private int GALLEY_CODE = 10;
    private int CAMERA_CODE = 1004;
    private String profile="";
    File imgFile = null;
    ImageView memberupdate_iv_profile;

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

    //팝업으로 받아온 프로필 사진 처리 방법
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        memberupdate_iv_profile = findViewById(R.id.memberupdate_iv_profile);

        if (resultCode == RESULT_OK) {
            PopupResult result = (PopupResult) data.getSerializableExtra("result");
            if(result == PopupResult.LEFT){
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

            } else if(result == PopupResult.RIGHT){
                // 사진첩 접근
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLEY_CODE);
            }
        }
        if(requestCode == GALLEY_CODE){
            profile = getRealPathFromUri(data.getData());
            RequestOptions cropOptions = new RequestOptions();
            Glide.with(getApplicationContext())
                    .load(profile)  //사진의 절대 경로
                    .apply(cropOptions.optionalCircleCrop()) //원형태로 변경
                    .into(memberupdate_iv_profile); //imageView에 출력함.
        }

        if(requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            // 저장처리를 함
            Toast.makeText(MemberUpdatePageActivity.this, "사진이 잘 찍힘", Toast.LENGTH_SHORT).show();
            setPic();
        }
    }

    // 사진을 저장처리 하는 곳
    private void setPic() {
        memberupdate_iv_profile = findViewById(R.id.memberupdate_iv_profile);

        // 이미지뷰의 크기 알아오기
        int targetW = memberupdate_iv_profile.getWidth();
        int targetH = memberupdate_iv_profile.getHeight();

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
        Glide.with(this).load(bitmap).circleCrop().into(memberupdate_iv_profile);

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_update);


        memberupdate_et_name = findViewById(R.id.memberupdate_et_name);
        memberupdate_et_email = findViewById(R.id.memberupdate_et_email);
        btn_back = findViewById(R.id.btn_back);
        memberupdate_cb_company = findViewById(R.id.memberupdate_cb_company);
        memberupdate_et_oldpw = findViewById(R.id.memberupdate_et_oldpw);
        memberupdate_et_newpw = findViewById(R.id.memberupdate_et_newpw);
        memberupdate_et_newpw2 = findViewById(R.id.memberupdate_et_newpw2);
        memberupdate_iv_profile = findViewById(R.id.memberupdate_iv_profile);
        memberupdate_bt_update = findViewById(R.id.memberupdate_bt_update);


        //프로필 사진 클릭시
        memberupdate_iv_profile.setOnClickListener(new View.OnClickListener() {
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

        //dto의 이메일 주소를 회원에게 보여줌 (수정은 불가능)
        memberupdate_et_email.setHint(loginDTO.getCustomer_email() + "(수정불가)");

        //이름을 힌트로 출력
        memberupdate_et_name.setHint(loginDTO.getCustomer_name());

        //프로필 사진을 등록하지 않았을 경우 기본 이미지를 보여줌
        Glide.with(this).load(R.drawable.guest2).circleCrop().into(memberupdate_iv_profile);
        
        //사업자를 체크 했을 경우 체크한 채로 나타나게 하기
        if(loginDTO.getAdmin().equals("M")){ memberupdate_cb_company.setChecked(true); }

        //프로필 사진을 등록했을 경우 저장된 이미지를 보여줌
        if (loginDTO.getCustomer_picture() != "") {
            Glide.with(this).load(loginDTO.getCustomer_picture()).circleCrop().into(memberupdate_iv_profile);
        }

        //상단 뒤로가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //새로운 비밀번호와 새로운 비밀번호 확인이 서로 맞지 않는 경우 textcolor를 red로 변경
        memberupdate_et_newpw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwd = memberupdate_et_newpw.getText().toString();
                String passwd2 = memberupdate_et_newpw2.getText().toString();
                int white = ContextCompat.getColor(getApplicationContext(), R.color.white);
                int red = ContextCompat.getColor(getApplicationContext(), R.color.red);
                if(!passwd.equals(passwd2)) {
               //     memberupdate_et_newpw.setTextColor(red);
                    memberupdate_et_newpw2.setTextColor(red);
                }else{
              //      memberupdate_et_newpw.setTextColor(white);
                    memberupdate_et_newpw2.setTextColor(white);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //비밀번호 유효성 검사 할때 틀리면 빨간색으로 변경
        memberupdate_et_newpw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwd = memberupdate_et_newpw.getText().toString();
                int white = ContextCompat.getColor(getApplicationContext(), R.color.white);
                int red = ContextCompat.getColor(getApplicationContext(), R.color.red);

                if(!Pattern.matches(
                        "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{6,15}.$", passwd)) {
                    memberupdate_et_newpw.setTextColor(red);
                    joinUpChk = 1;
                }else{
                    memberupdate_et_newpw.setTextColor(white);

                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //가입하기 버튼 클릭
        memberupdate_bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd = memberupdate_et_oldpw.getText().toString();
                String newpasswd = memberupdate_et_newpw.getText().toString();
                String newpasswd2 = memberupdate_et_newpw2.getText().toString();
                String name = memberupdate_et_name.getText().toString();
                String admin;
                String email = loginDTO.getCustomer_email();

                //비번을 입력하지 않았을 때 경고창
                if (passwd.length() == 0) {
                    Toast.makeText(MemberUpdatePageActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    memberupdate_et_oldpw.requestFocus();
                    return;
                }

                //비밀번호가 기존의 비밀번호와 다를 경우
                if (!passwd.equals(loginDTO.getCustomer_pw())) {
                    Toast.makeText(MemberUpdatePageActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    memberupdate_et_oldpw.requestFocus();
                    return;
                }

                //새로운비밀번호2가 새로운비밀번호1과 다를경우 경고장
                if (!newpasswd.equals(newpasswd2)) {
                    Toast.makeText(MemberUpdatePageActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    memberupdate_et_newpw.requestFocus();
                    return;
                }

                //사업자 유무
                if (memberupdate_cb_company.isChecked()) {
                    admin = "M";
                } else {
                    admin = "C";
                }



                //위의 검사를 모두 통과 한 후 newpasswd에 값이 들어있을 경우 passwd에 newpasswd 값을 넣는다.
                if (!newpasswd.isEmpty()) {
                    passwd = newpasswd;
                }
                //name에 값이 없을 경우 lgoinDTO의 name 값을 가져온다.
                if (name.isEmpty()) {
                    name = loginDTO.getCustomer_name();
                }


                if (joinUpChk == 0) {
                    //회원가입 처리 시작
                    MemberUpdate memberUpdatePage = new MemberUpdate(email, passwd, name, admin, loginDTO.getCustomer_picture());

                    //가입 성공 여부
                    String state = "";
                    try {
                        state = memberUpdatePage.execute().get().trim();
                        Log.d("main:joinact0 : ", state);
                        state = state.substring(11, 12);
                        Log.d("main:joinact1 : ", state);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (state.equals("1")) {
                   /* LoginSelect loginSelect = new LoginSelect(email, passwd);
                    try {
                        loginSelect.execute().get();
                    } catch (ExecutionException e) {
                        e.getMessage();
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
*/
                        LoginAtask loginAtask = new LoginAtask(email, passwd);
                        try {
                            loginDTO = (MemberVO) loginAtask.execute().get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(MemberUpdatePageActivity.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("main:memberUpdate", "수정되었습니다.");
                        finish();
                    } else {
                        Toast.makeText(MemberUpdatePageActivity.this, "수정실패!", Toast.LENGTH_SHORT).show();
                        Log.d("main:memberUpdate", "수정실패!");
                        finish();
                    }
                } else {
                    Toast.makeText(MemberUpdatePageActivity.this, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                    memberupdate_et_newpw.requestFocus();
                }
            }
        });
    }
}