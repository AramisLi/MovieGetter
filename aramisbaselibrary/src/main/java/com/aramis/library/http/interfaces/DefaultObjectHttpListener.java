package com.aramis.library.http.interfaces;


import com.aramis.library.http.custom.BaseBean;
import com.aramis.library.http.custom.PromptView;
import com.aramis.library.ui.dialog.LoadingDialog;
import com.aramis.library.utils.LogUtils;
import com.google.gson.Gson;

/**
 * 默认json object Listener(当T instanceof BaseBean时，返回null，只用于判断成功与否用途)
 * Created by admin on 2016/9/12.
 */
public abstract class DefaultObjectHttpListener<T> extends DefaultHttpListener<T> {
    //    private final String TAG = "===DefaultObjectHttp";
    private Class<T> clazz;

    public DefaultObjectHttpListener() {
        super();
        init();
    }

    public DefaultObjectHttpListener(Class<T> clazz) {
        super();
        this.clazz = clazz;
        init();
    }

    public DefaultObjectHttpListener(boolean needToLogin) {
        super(needToLogin);
        init();
    }

    public DefaultObjectHttpListener(PromptView errorView) {
        super(errorView);
        init();
    }

    public DefaultObjectHttpListener(PromptView errorView, LoadingDialog loadingDialog) {
        super(errorView, loadingDialog);
        init();
    }

    protected void init() {
        if (clazz == null) {
            clazz = getParsedClass();
        }
    }

    @Override
    protected void onSuccessParsedFirst(int code, String data) {
//        LogUtils.e("DefaultObjectHttpListener", "" + data);
        if (clazz == BaseBean.class) {
            onSuccessParsed(null);
        } else {
//            T tt = JSON.parseObject(data, clazz);
            try{
                T tt = new Gson().fromJson(data, clazz);
                if (tt != null) {
                    onSuccessParsed(tt);
                }
            }catch (Exception e){
                e.printStackTrace();
//                LogUtils.e();
                onFailure(0,"parse error");
            }
        }
    }

    public abstract void onSuccessParsed(T result);
}