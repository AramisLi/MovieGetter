package com.aramis.library.http.interfaces;


import android.view.View;

import com.aramis.library.http.custom.PromptView;
import com.aramis.library.ui.dialog.LoadingDialog;
import com.aramis.library.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认json list Listener
 * Created by admin on 2016/9/12.
 */
public abstract class DefaultListHttpListener<T> extends DefaultHttpListener<T> {
    private final String TAG = "===DefaultListHttp";
    private Class<T> clazz;
    private String jsonArrayName;

    public DefaultListHttpListener() {
        super();
        init();
    }

    public DefaultListHttpListener(Class<T> clazz) {
        super();
        this.clazz = clazz;
        init();
    }

    public DefaultListHttpListener(boolean needToLogin) {
        super(needToLogin);
        init();
    }

    public DefaultListHttpListener(PromptView errorView) {
        super(errorView);
        init();
    }

    public DefaultListHttpListener(PromptView errorView, LoadingDialog loadingDialog) {
        super(errorView, loadingDialog);
        init();
    }

    protected void init() {
        if (clazz == null) {
            clazz = getParsedClass();
        }
        jsonArrayName = "list";//默认
    }

    public void setJsonArrayName(String jsonName) {
        this.jsonArrayName = jsonName;
    }

    @Override
    protected void onSuccessParsedFirst(int code, String data) {
        LogUtils.e(TAG, "onSuccessParsedFirst============== code:" + code + ",data:" + data);
        try {
            if (data.charAt(0) == '[') {
                parseArray(data);
            } else if (data.charAt(0) == '{') {
                JSONObject obj = new JSONObject(data);
                if (!obj.isNull(jsonArrayName)) {
                    parseArray(obj.getString(jsonArrayName));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseArray(String data) {
        List<T> tt = parse(new Gson(), data, clazz);
        if (tt != null && tt.size() > 0) {
            LogUtils.e(TAG, tt.get(0).getClass().getSimpleName());
            onSuccessParsed(tt);
        } else {
            PromptView errorView = getErrorView();
            if (errorView != null) {
//                errorView.setLoadingImg(R.drawable.ico_fb_nodata);
                errorView.setLoadingText("暂无数据", null);
                errorView.setVisibility(View.VISIBLE);
            }
            onListSizeZero();
        }
    }

    private ArrayList<T> parse(Gson gson, String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<>();
        JsonArray asJsonArray = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement j : asJsonArray) {
            mList.add(gson.fromJson(j, cls));
        }
        return mList;
    }

    public abstract void onSuccessParsed(List<T> result);

    public abstract void onListSizeZero();
}