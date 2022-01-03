package com.example.alphacar.DTOS;

public class RegisterDTO {
    private int store_number;
    private String customer_email             ;
    private String store_name                 ;
    private String store_post                 ;
    private String store_addr                 ;
    private String store_detail_addr          ;
    private String store_tel                  ;
    private String store_time                 ;
    private String store_dayoff               ;
    private String introduce                  ;
    private int inventory                  ;
    private String store_price                ;
    private String store_master_name          ;
    private String store_registration_number  ;
    private String imgpath ;
    private String imgname ;

    public RegisterDTO(int store_number, String customer_email, String store_name,String store_post, String store_addr,String store_detail_addr , String store_tel, String store_time, String store_dayoff, String introduce, int inventory, String store_price, String store_master_name, String store_registration_number, String imgpath, String imgname) {
        this.store_number = store_number;
        this.customer_email = customer_email;
        this.store_name = store_name;
        this.store_post = store_post;
        this.store_addr = store_addr;
        this.store_detail_addr = store_detail_addr;
        this.store_tel = store_tel;
        this.store_time = store_time;
        this.store_dayoff = store_dayoff;
        this.introduce = introduce;
        this.inventory = inventory;
        this.store_price = store_price;
        this.store_master_name = store_master_name;
        this.store_registration_number = store_registration_number;
        this.imgpath = imgpath;
        this.imgname = imgname;
    }

    public int getStore_number() {
        return store_number;
    }

    public void setStore_number(int store_number) {
        this.store_number = store_number;
    }

    public String getStore_post() {
        return store_post;
    }

    public void setStore_post(String store_post) {
        this.store_post = store_post;
    }

    public String getStore_detail_addr() {
        return store_detail_addr;
    }

    public void setStore_detail_addr(String store_detail_addr) {
        this.store_detail_addr = store_detail_addr;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_addr() {
        return store_addr;
    }

    public void setStore_addr(String store_addr) {
        this.store_addr = store_addr;
    }

    public String getStore_tel() {
        return store_tel;
    }

    public void setStore_tel(String store_tel) {
        this.store_tel = store_tel;
    }

    public String getStore_time() {
        return store_time;
    }

    public void setStore_time(String store_time) {
        this.store_time = store_time;
    }

    public String getStore_dayoff() {
        return store_dayoff;
    }

    public void setStore_dayoff(String store_dayoff) {
        this.store_dayoff = store_dayoff;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getStore_price() {
        return store_price;
    }

    public void setStore_price(String store_price) {
        this.store_price = store_price;
    }

    public String getStore_master_name() {
        return store_master_name;
    }

    public void setStore_master_name(String store_master_name) {
        this.store_master_name = store_master_name;
    }

    public String getStore_registration_number() {
        return store_registration_number;
    }

    public void setStore_registration_number(String store_registration_number) {
        this.store_registration_number = store_registration_number;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }
}
