package com.example.alphacar.DTOS;

import java.io.Serializable;
import java.util.Date;

public class ReviewDTO implements Serializable {

    private int review_id              ;
    private String customer_email         ;
    private int store_number           ;
    private String review_title           ;
    private String review_content         ;
    private Date review_writedate       ;
    private int review_readcnt         ;
    private String review_filename        ;
    private String review_filepath        ;
    private String review_score           ;
    private String customer_picture;

    public ReviewDTO(String review_title, String review_content, String review_score) {
        this.review_title = review_title;
        this.review_content = review_content;
        this.review_score = review_score;
    }

    public ReviewDTO(){}

    public ReviewDTO(String customer_email, String review_title, String review_content, String review_score, String review_filepath, String customer_picture) {
        this.customer_email =customer_email;
        this.review_title = review_title;
        this.review_content = review_content;
        this.review_score = review_score;
        this.review_filepath = review_filepath;
        this.customer_picture = customer_picture;

    }

    public ReviewDTO(int review_id, String customer_email, String review_title, String review_content, String review_score) {
        this.review_id = review_id;
        this.customer_email = customer_email;
        this.review_title = review_title;
        this.review_content = review_content;
        this.review_score = review_score;
    }

    public String getCustomer_picture() {
        return customer_picture;
    }

    public void setCustomer_picture(String customer_picture) {
        this.customer_picture = customer_picture;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public int getStore_number() {
        return store_number;
    }

    public void setStore_number(int store_number) {
        this.store_number = store_number;
    }

    public String getReview_title() {
        return review_title;
    }

    public void setReview_title(String review_title) {
        this.review_title = review_title;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public Date getReview_writedate() {
        return review_writedate;
    }

    public void setReview_writedate(Date review_writedate) {
        this.review_writedate = review_writedate;
    }

    public int getReview_readcnt() {
        return review_readcnt;
    }

    public void setReview_readcnt(int review_readcnt) {
        this.review_readcnt = review_readcnt;
    }

    public String getReview_filename() {
        return review_filename;
    }

    public void setReview_filename(String review_filename) {
        this.review_filename = review_filename;
    }

    public String getReview_filepath() {
        return review_filepath;
    }

    public void setReview_filepath(String review_filepath) {
        this.review_filepath = review_filepath;
    }

    public String getReview_score() {
        return review_score;
    }

    public void setReview_score(String review_score) {
        this.review_score = review_score;
    }
}
