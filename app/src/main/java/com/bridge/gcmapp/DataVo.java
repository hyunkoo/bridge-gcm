package com.bridge.gcmapp;

import java.io.Serializable;

/**
 * Created by sec on 2016-10-25.
 */
public class DataVo implements Serializable{

    private String code;
    private String title;
    private String contents;
    private String date;

    public DataVo(){}

    public  DataVo(String title, String contents, String code){
        super();
        this.title = title;
        this.contents = contents;
        this.code = code;
    }

    public DataVo(String title, String contents, String date, String code) {
        super();
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
