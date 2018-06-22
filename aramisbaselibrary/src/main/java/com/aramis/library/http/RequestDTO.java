package com.aramis.library.http;

import java.io.Serializable;
import java.util.Map;


public class RequestDTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String reqSn;
    private String token;
    private Map<String, Object> data;


    public RequestDTO() {
        super();
    }

    public RequestDTO(String token, String reqSn, Map<String, Object> data) {
        this.reqSn = reqSn;
        this.token = token;
        this.data = data;
    }

    public String getReqSn() {
        return reqSn;
    }

    public void setReqSn(String reqSn) {
        this.reqSn = reqSn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
