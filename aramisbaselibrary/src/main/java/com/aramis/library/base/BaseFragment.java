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

    protected void toOtherActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(mActivity, clazz);
        if (bundle != null) intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }


    protected void toWebActivity(String webUrl, String title) {
        Intent intent = new Intent(getActivity(), WebBrowserActivity.class);
        intent.putExtra(WebBrowserActivity.WEB_URL, webUrl);
        intent.putExtra(WebBrowserActivity.WEB_TITLE, title);
        startActivity(intent);
    }
}
