package com.aramis.library.http;

/**
 * ArHttpConfig
 * Created by Aramis on 2017/5/10.
 */

public class ArHttpConfig {

    public static final int ERROR = 500;//未知错误
    public static final int NOT_FOUND = 404;//数据未找到
    public static final int HAS_FOUND = 405;//数据已经存在
    public static final int PASSWORD_ERROR = 501;//密码错误
    public static final int SUCCESS = 200;//成功
    public static final int TOKEN_EXPIRE = 300;//token过期
    public static final int TOKEN_HASUSED = 301;//token已经使用
    public static final int FAIL_VAILDATE = 502;//验证码错误
    public static final int REQUESTPARAM_BAD = 310;//传入参数不合规
    public static final int RESPONSE_NULL = 601;//返回数据为空
    public static final int RESPONSE_ERROR = 602;//请求错误
    public static final int TOKEN_OVERDUE = 300;//token过期

    public static final String TOKEN_OVERDUE_BUS_KEY = "tokenError";
    public static final String TOKEN_OVERDUE_ERROR_KEY = "tokenErrorString";

    public static final String RESPONSE_NULL_STR = "返回数据为空", RESPONSE_ERROR_STR = "请求错误";
}
