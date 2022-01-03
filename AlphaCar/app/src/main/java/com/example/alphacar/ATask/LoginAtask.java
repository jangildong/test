package com.example.alphacar.ATask;

import android.os.AsyncTask;
import static com.example.alphacar.LoginPageActivity.loginDTO;
import com.example.alphacar.Common.AtaskCommon;
import com.example.alphacar.DTOS.MemberVO;
import com.example.alphacar.DTOS.StoreDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LoginAtask extends AsyncTask<Object,Object,Object> {

    private String customer_email;
    private String customer_pw;
    private InputStream is;

    public LoginAtask( String customer_email, String customer_pw){
        this.customer_email = customer_email;
        this.customer_pw = customer_pw;
    }

    @Override
    protected Object doInBackground(Object... objects) {
        if (customer_email != null && customer_pw != null) {
            try {
                AtaskCommon common = new AtaskCommon("and_login");
                is = common.sendSpring(customer_email, customer_pw);
                Gson gson = new Gson();
                loginDTO = gson.fromJson(new InputStreamReader(is), new TypeToken<MemberVO>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return loginDTO;
        }

        else{
            return null;
        }
    }

}
