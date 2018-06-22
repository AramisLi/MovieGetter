package com.aramis.library.http.interfaces;

import android.text.TextUtils;
import android.view.View;

import com.aramis.library.http.ArHttpConfig;
import com.aramis.library.http.custom.BaseBean;
import com.aramis.library.http.custom.PromptView;
import com.aramis.library.ui.dialog.LoadingDialog;
import com.aramis.library.utils.LogUtils;
import com.kymjs.rxvolley.client.HttpCallback;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 网络回调父类
 * Created by Aramis on 2016/11/14.
 */

public abstract class DefaultHttpListener<T> extends HttpCallback {
    private final String TAG = "===DefaultHttpListener";
    private Class<T> clazz;
    private PromptView errorView;
    private LoadingDialog loadingDialog;
    private boolean needToLogin = true;
    public static final int PROTMP_NO_DATA = 1;
    public static final int PROTMP_NET_ERROR = 2;
    public static final int PROTMP_NO_LOCATION = 3;

    public DefaultHttpListener() {
        init(null, null);
    }

    public DefaultHttpListener(boolean needToLogin) {
        this.needToLogin = needToLogin;
        init(null, null);
    }

    public DefaultHttpListener(PromptView errorView) {
        init(errorView, null);
    }

    public DefaultHttpListener(PromptView errorView, LoadingDialog loadingDialog) {
        init(errorView, loadingDialog);
    }

    private void init(PromptView errorView, LoadingDialog loadingDialog) {
//        if (errorView != null && errorView.getId() == R.id.layout_http_error)
        this.errorView = errorView;
        this.loadingDialog = loadingDialog;

        Type modelType = getClass().getGenericSuperclass();
        if ((modelType instanceof ParameterizedType)) {
            Type[] modelTypes = ((ParameterizedType) modelType).getActualTypeArguments();
//            LogUtils.e(TAG, Arrays.toString(modelTypes));
            if (modelTypes[0] instanceof Class) {
                clazz = ((Class) modelTypes[0]);
            }
//            if (modelTypes[0] instanceof ListObjectBean)
        }
    }

    protected Class<T> getParsedClass() {
        return clazz;
    }

    protected PromptView getErrorView() {
        return errorView;
    }

    protected LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }

    private BaseBean baseBean;

    @Nullable
    protected BaseBean getBaseBean() {
        return baseBean;
    }

    @Override
    public void onSuccess(String t) {
        super.onSuccess(t);
        LogUtils.e(TAG, "=" + t);
        if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
        if (errorView != null) errorView.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(t)) {
            onSuccessParsedFirst(200, t);
//            BaseBean baseBean = null;
//            try {
//                baseBean = JsonUtil.getIntance().parseObject(t, BaseBean.class);
//            } catch (Exception e) {
//                LogUtils.e(TAG, "解析错误");
//                e.printStackTrace();
//            }
//
//            if (baseBean != null) {
//                int status = baseBean.getStatus();
//                if (status == ArHttpConfig.TOKEN_OVERDUE) {
//                    if (needToLogin) {
//                        LogUtils.e(TAG, "token 过期:" + t);
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean(ArHttpConfig.TOKEN_OVERDUE_BUS_KEY, true);
////                        bundle.putString(ArHttpConfig.TOKEN_OVERDUE_ERROR_KEY, "身份令牌已过期，请重新登录");
//                        bundle.putString(ArHttpConfig.TOKEN_OVERDUE_ERROR_KEY, baseBean.getMsg());
//                        ArBus.getDefault().post(bundle);
//                    }
//                } else if (status == ArHttpConfig.SUCCESS) {
//                    String results = baseBean.getResults();
//                    if (clazz == BaseBean.class || !TextUtils.isEmpty(results)) {
////                        LogUtils.e("dd", "success results:"+results);
//                        onSuccessParsedFirst(status, results);
//                    } else {
//                        onFailWithCode(status,
//                                ArHttpConfig.RESPONSE_ERROR, ArHttpConfig.RESPONSE_ERROR_STR);
//                    }
//                } else {
//                    onFailWithCode(status, ArHttpConfig.RESPONSE_ERROR, baseBean.getMsg());
//                }
//            } else {
//                onFailWithCode(ArHttpConfig.RESPONSE_NULL,
//                        ArHttpConfig.RESPONSE_NULL, ArHttpConfig.RESPONSE_NULL_STR);
//            }
        } else {
            onFailWithCode(ArHttpConfig.RESPONSE_NULL,
                    ArHttpConfig.RESPONSE_NULL, ArHttpConfig.RESPONSE_NULL_STR);
        }
    }

    protected abstract void onSuccessParsedFirst(int code, String data);

    @Override
    public void onFailure(int errorNo, @Nullable String strMsg) {
        super.onFailure(errorNo, TextUtils.isEmpty(strMsg) ? "" : strMsg);

        if (strMsg == null && errorNo == 401) {
            strMsg = "Invalid Login or password";
        }
        onFailWithCode(errorNo, errorNo, TextUtils.isEmpty(strMsg) ? "" : strMsg);

//        LogUtils.e("1234234234234234", "errorNo:" + errorNo + ",strMsg:" + strMsg);
    }

    protected void showPrompt() {
        showPrompt(PROTMP_NO_DATA);
    }

    protected void showPrompt(int flag) {
        if (errorView != null) {
            int imgId = 0;
            String errorText = "";
//            switch (flag) {
//                case PROTMP_NO_DATA:
//                    imgId = R.mipmap.ico_error_nodata;
//                    errorText = "抱歉！暂时没有数据哦！";
//                    break;
//                case PROTMP_NET_ERROR:
//                    imgId = R.mipmap.ico_error_net;
//                    errorText = "网络出现了问题，请重新刷新网络哟！";
//                    break;
//                case PROTMP_NO_LOCATION:
//                    imgId = R.mipmap.ico_error_nolocation;
//                    errorText = "抱歉！定位失败了，请重新定位哦！";
//                    break;
//            }
            errorView.setLoadingImg(imgId);
            errorView.setLoadingText(errorText);
            errorView.setVisibility(View.VISIBLE);
        }
    }

    protected void onFailWithCode(int code, int errorNo, @Nullable String strMsg) {
//        super.onFailure(errorNo, TextUtils.isEmpty(strMsg) ? "" : strMsg);
        LogUtils.e(TAG, "errorNo:" + errorNo + ",strMsg:" + strMsg);
        if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
        if (errorView != null) {
            switch (errorNo) {
                case -1:
//                    errorView.setLoadingImg(R.drawable.icon_no_network);
//                    errorView.setLoadingText("别嚎啦，一会儿网就来...", "请检查网络后点击重新加载...");
                    break;
                case ArHttpConfig.RESPONSE_NULL:
//                    errorView.setLoadingImg(R.drawable.ico_fb_nodata);
//                    errorView.setLoadingText("暂无数据", null);
                    break;
                case ArHttpConfig.RESPONSE_ERROR:
//                    errorView.setLoadingImg(R.drawable.icon_no_permission);
//                    errorView.setLoadingText("暂无权限查看他人数据", "请重新选择筛选条件哦");
                    break;
                default:
//                    errorView.setLoadingImg(R.drawable.icon_who_me);
//                    errorView.setLoadingText("木有找到任何数据呢", "快去看看别的吧~");
                    break;

            }
            errorView.setVisibility(View.VISIBLE);
        }
    }
}
