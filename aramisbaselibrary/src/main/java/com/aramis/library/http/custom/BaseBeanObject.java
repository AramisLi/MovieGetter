package com.aramis.library.http.custom;

import java.io.Serializable;

/**
 * 根bean
 * Created by ASUS on 2017/3/2.
 */

public class BaseBeanObject implements Serializable {
    private int status;
    private String timestamp;
    private Object results;
    private String msg;
    private String cap;

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
