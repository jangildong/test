package com.example.alphacar.DTOS;

import java.io.Serializable;

public class MemberVO implements Serializable {

    private String customer_email     ;
    private String customer_pw        ;
    private String customer_name      ;
    private String customer_picture   ;
    private String naver              ;
    private String kakao              ;
    private String admin              ;

    public MemberVO() {

    }

    public String getCustomer_email() {
        return customer_email;
    }

    public MemberVO(String customer_email, String customer_pw, String customer_name, String admin, String customer_picture) {
        this.customer_email = customer_email;
        this.customer_pw = customer_pw;
        this.customer_name = customer_name;
        this.admin = admin;
        this.customer_picture = customer_picture;
    }


    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_pw() {
        return customer_pw;
    }

    public void setCustomer_pw(String customer_pw) {
        this.customer_pw = customer_pw;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_picture() {
        return customer_picture;
    }

    public void setCustomer_picture(String customer_picture) {
        this.customer_picture = customer_picture;
    }

    public String getNaver() {
        return naver;
    }

    public void setNaver(String naver) {
        this.naver = naver;
    }

    public String getKakao() {
        return kakao;
    }

    public void setKakao(String kakao) {
        this.kakao = kakao;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
