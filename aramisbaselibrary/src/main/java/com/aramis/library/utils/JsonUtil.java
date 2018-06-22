package com.aramis.library.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.util.List;

/**
 * json解析工具
 * Created by Cbt on 2016/12/19.
 */
public class JsonUtil {
    volatile private static JsonUtil intance = null;
    private static Gson gson = new Gson();

    private JsonUtil() {
    }

    public static JsonUtil getIntance() {
        synchronized (JsonUtil.class) {
            if (intance == null) {
                intance = new JsonUtil();
            }
        }
        return intance;
    }


    public final <T> T parseObject(String data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }


    public final <T> List<T> parseArray(String data, Class<T> clazz) {
//        T[] array = gson.fromJson(data, clazz);
//        return Arrays.asList(array);
        return JSON.parseArray(data, clazz);
    }


}
