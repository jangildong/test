package com.example.alphacar.DTOS;

import java.io.Serializable;

public class FileDTO implements Serializable {
    private int file_id;
    private int store_number;
    private String imgpath;
    private String imgname;
    private int rank;

    public FileDTO(int file_id, int store_number, String imgpath, String imgname, int rank) {
        this.file_id = file_id;
        this.store_number = store_number;
        this.imgpath = imgpath;
        this.imgname = imgname;
        this.rank = rank;
    }

    public int getFile_id() {
        return file_id;
    }

    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }

    public int getStore_number() {
        return store_number;
    }

    public void setStore_number(int store_number) {
        this.store_number = store_number;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
