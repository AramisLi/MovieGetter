package com.aramis.library.http.custom;

import java.io.Serializable;

/**
 * 根bean
 * Created by ASUS on 2017/3/2.
 */

public class BaseBean implements Serializable {
    private int status;
    private String timestamp;
    private String results;
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

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "status=" + status +
                ", timestamp='" + timestamp + '\'' +
                ", results='" + results + '\'' +
                ", msg='" + msg + '\'' +
                ", cap='" + cap + '\'' +
                '}';
    }
}
