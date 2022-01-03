package com.example.alphacar;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.alphacar.ATask.LoginAtask;
import com.example.alphacar.ATask.LoginSelect;
import com.example.alphacar.Common.RetrofitCommon;
import com.example.alphacar.DTOS.MemberVO;
import com.example.alphacar.DTOS.StoreDTO;
import com.example.alphacar.retro.StoreService;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginPageActivity extends AppCompatActivity {

    //카카오로그인에 필요한 ISession
    private ISessionCallback mSessionCallback;

    Retrofit retrofit;
    StoreService storeService;
    LoginAtask loginAtask;


    // 로그인이 성공하면 static 로그인DTO 변수에 담아서
    // 어느곳에서나 접근할 수 있게 한다
    public static MemberVO loginDTO;




    EditText memberlogin_et_email, memberlogin_et_pw;
    TextView memberlogin_bt_login;
    Button memberlogin_bt_join;
    ImageButton btn_back;

   // LoginButton loginButton;
    ImageButton kakao_login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        checkDangerousPermissions();

        loginDTO = new MemberVO();

  //      kakao_login_btn = findViewById(R.id.kakao_login_btn);
      //  loginButton = findViewById(R.id.loginButton);
        memberlogin_et_email = findViewById(R.id.memberlogin_et_email);
        memberlogin_et_pw = findViewById(R.id.memberlogin_et_pw);

        memberlogin_bt_login = findViewById(R.id.memberlogin_bt_login);
        memberlogin_bt_join = findViewById(R.id.memberlogin_bt_join);
        btn_back = findViewById(R.id.btn_back);
        //상단 뒤로가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 로그인 버튼
        memberlogin_bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberlogin_et_email.getText().toString().length() != 0 && memberlogin_et_pw.getText().toString().length() != 0) {
                    String customer_email = memberlogin_et_email.getText().toString();
                    String customer_pw = memberlogin_et_pw.getText().toString();

                    loginAtask = new LoginAtask(customer_email, customer_pw);
                    try {
                        loginDTO = (MemberVO) loginAtask.execute().get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



              /*       LoginSelect loginSelect = new LoginSelect(customer_email, customer_pw);
                    try {
                         loginSelect.execute().get();
                    } catch (ExecutionException e) {
                        e.getMessage();
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }*/
                    //레트로핏
                    /*
                    Call<MemberVO> call = RetrofitCommon.getClient().getAnLogin(customer_email,customer_pw);
                    call.enqueue(new Callback<MemberVO>() {
                        @Override
                        public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                            loginDTO = response.body();
                        }

                        @Override
                        public void onFailure(Call<MemberVO> call, Throwable t) {

                        }
                    });

                     */

                } else {
                    Toast.makeText(LoginPageActivity.this, "아이디와 암호를 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                    Log.d("main:login", "아이디와 암호를 모두 입력하세요.");
                    return;
                }

                if (loginDTO != null) {
                    Toast.makeText(LoginPageActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("main:login", loginDTO.getCustomer_email() + "님 로그인 되었습니다.");

                    // 로그인 정보에 값이 있으면 로그인이 되었으므로 메인화면으로 이동
                    if (loginDTO != null) {
                        Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
                        //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //   finish();
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(LoginPageActivity.this, "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("main:login", "아이디나 비밀번호가 일치하지 않습니다.");
                    memberlogin_et_email.setText("");
                    memberlogin_et_pw.setText("");
                    memberlogin_et_email.requestFocus();
                }

            }
        });

        // 회원 가입 버튼
        memberlogin_bt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 화면
                Intent intent = new Intent(LoginPageActivity.this, JoinPageActivity.class);
                startActivity(intent);
            }
        });



        //카카오 로그인 시작

        mSessionCallback = new ISessionCallback() {

            @Override
            public void onSessionOpened() {
                //로그인 요청


                UserManagement.getInstance().me(new MeV2ResponseCallback() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        //로그인 실패
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        //세션이 닫힘...
                        Toast.makeText(LoginPageActivity.this, "로그인 도중 오류가 발생했습니다.. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
                        intent.putExtra("name", result.getKakaoAccount().getProfile().getNickname());
                        intent.putExtra("profileImg", result.getKakaoAccount().getProfile().getProfileImageUrl());
                        intent.putExtra("email", result.getKakaoAccount().getEmail());
                        startActivity(intent);

                    }

                });
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                Toast.makeText(LoginPageActivity.this, "onSessionOpenFailed", Toast.LENGTH_SHORT).show();
            }
        };
        Session.getCurrentSession().addCallback(mSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();


    }


                //카카오 로그인 끝




    @Override
    public void onPause() {
        super.onPause();
    }

    //권한설정
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
            //Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                //Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mSessionCallback);
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {

            }
        });
    }

 /*   private class SessionCallback implements ISessionCallback{

        @Override
        public void onSessionOpened() {
            String email =
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {

        }
    }

    protected void redirectSignupActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/
}