package com.example.alphacar.DTOS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetroStoreDTO {


    @SerializedName("store_number")
    @Expose
    private Integer storeNumber;
    @SerializedName("imgpath")
    @Expose
    private String imgpath;
    @SerializedName("imgname")
    @Expose
    private String imgname;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("store_addr")
    @Expose
    private String storeAddr;
    @SerializedName("store_tel")
    @Expose
    private String storeTel;
    @SerializedName("store_time")
    @Expose
    private String storeTime;
    @SerializedName("store_dayoff")
    @Expose
    private String storeDayoff;
    @SerializedName("introduce")
    @Expose
    private String introduce;
    @SerializedName("inventory")
    @Expose
    private Integer inventory;
    @SerializedName("store_price")
    @Expose
    private String storePrice;
    @SerializedName("store_registration_number")
    @Expose
    private String storeRegistrationNumber;
    @SerializedName("store_favorite_cnt")
    @Expose
    private Integer storeFavoriteCnt;

    public Integer getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(Integer storeNumber) {
        this.storeNumber = storeNumber;
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

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddr() {
        return storeAddr;
    }

    public void setStoreAddr(String storeAddr) {
        this.storeAddr = storeAddr;
    }

    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }

    public String getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(String storeTime) {
        this.storeTime = storeTime;
    }

    public String getStoreDayoff() {
        return storeDayoff;
    }

    public void setStoreDayoff(String storeDayoff) {
        this.storeDayoff = storeDayoff;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(String storePrice) {
        this.storePrice = storePrice;
    }

    public String getStoreRegistrationNumber() {
        return storeRegistrationNumber;
    }

    public void setStoreRegistrationNumber(String storeRegistrationNumber) {
        this.storeRegistrationNumber = storeRegistrationNumber;
    }

    public Integer getStoreFavoriteCnt() {
        return storeFavoriteCnt;
    }

    public void setStoreFavoriteCnt(Integer storeFavoriteCnt) {
        this.storeFavoriteCnt = storeFavoriteCnt;
    }
}
