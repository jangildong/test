package com.example.alphacar.DTOS;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;


public class NotiftDTO implements Serializable {
  private int notice_id, notice_readcnt, notice_root, notice_step, notice_indent;
   private String customer_email, notice_title, notice_content, notice_filename, notice_filepath, notice_attribute;
   //private Date notice_writedate;
   private String notice_writedate;


    public NotiftDTO(int notice_id, String customer_email, String notice_title, String notice_content, String notice_filename, String notice_filepath, String notice_writedate) {
        this.notice_id = notice_id;
        this.customer_email = customer_email;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
        this.notice_filename = notice_filename;
        this.notice_filepath = notice_filepath;
        this.notice_writedate = notice_writedate;
    }

    public NotiftDTO(int notice_id, String customer_email, String notice_title, String notice_content , String notice_writedate){
        this.notice_id = notice_id;
        this.customer_email = customer_email;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
        this.notice_writedate = notice_writedate;
    }

    public NotiftDTO() {

    }

    public String getNotice_writedate() {
        return notice_writedate;
    }

    public void setNotice_writedate(String notice_writedate) {
        this.notice_writedate = notice_writedate;
    }

    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }

    public int getNotice_readcnt() {
        return notice_readcnt;
    }

    public void setNotice_readcnt(int notice_readcnt) {
        this.notice_readcnt = notice_readcnt;
    }

    public int getNotice_root() {
        return notice_root;
    }

    public void setNotice_root(int notice_root) {
        this.notice_root = notice_root;
    }

    public int getNotice_step() {
        return notice_step;
    }

    public void setNotice_step(int notice_step) {
        this.notice_step = notice_step;
    }

    public int getNotice_indent() {
        return notice_indent;
    }

    public void setNotice_indent(int notice_indent) {
        this.notice_indent = notice_indent;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public String getNotice_filename() {
        return notice_filename;
    }

    public void setNotice_filename(String notice_filename) {
        this.notice_filename = notice_filename;
    }

    public String getNotice_filepath() {
        return notice_filepath;
    }

    public void setNotice_filepath(String notice_filepath) {
        this.notice_filepath = notice_filepath;
    }

    public String getNotice_attribute() {
        return notice_attribute;
    }

    public void setNotice_attribute(String notice_attribute) {
        this.notice_attribute = notice_attribute;
    }

/*   public Date getNotice_writedate() {
        return notice_writedate;
    }

    public void setNotice_writedate(Date notice_writedate) {
        this.notice_writedate = notice_writedate;
    }*/
}
