package com.aramis.library.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.aramis.library.aramis.WebBrowserActivity;
import com.aramis.library.component.dialog.BeforeLoadingDialog;
import com.aramis.library.ui.dialog.LoadingDialog;


/**
 * MotionsBaseFragment
 * Created by Aramis on 2017/4/26.
 */

public class BaseFragment extends Fragment {
    public final String TAG = "===" + getClass().getSimpleName() + "===";

    protected View mRootView;
    protected Activity mActivity;
    protected int screenWidth, screenHeight;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        loadingDialog = new BeforeLoadingDialog(mActivity);
    }

    public LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }

    protected View findViewById(int id) {
        return mRootView == null ? null : mRootView.findViewById(id);
    }

//    protected void toLoginActivity() {
//        Intent intent = new Intent(mActivity, LoginSingleActivity.class);
//        mActivity.startActivity(intent);
//    }

//    protected boolean checkAndToLogin() {
//        if (!BunnySP.Companion.isLogin()) {
//            toLoginActivity();
//            return false;
//        }
//        return true;
//    }

//    protected boolean isLogin() {
//        return isLogin(true);
//    }
//
//    protected boolean isLogin(boolean toActivity) {
//        boolean login = BunnySP.Companion.isLogin();
//        if (toActivity && !login) {
//            LogUtils.e(TAG,"fragment启动登录activity");
//            toOtherActivity(LoginSingleActivity.class, null);
//        }
//        return login;
//    }

    protected void toOtherActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(mActivity, clazz);
        if (bundle != null) intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart("BaseFragment"); // 统计页面
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd("BaseFragment"); // 统计页面
//    }

    protected void toWebActivity(String webUrl, String title) {
        Intent intent = new Intent(getActivity(), WebBrowserActivity.class);
        intent.putExtra(WebBrowserActivity.WEB_URL, webUrl);
        intent.putExtra(WebBrowserActivity.WEB_TITLE, title);
        startActivity(intent);
    }
}
