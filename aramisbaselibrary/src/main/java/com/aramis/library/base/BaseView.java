package com.aramis.library.base;

import com.aramis.library.ui.dialog.LoadingDialog;

/**
 * BaseView
 * Created by Aramis on 2017/6/22.
 */

public interface BaseView {

    /**
     * 获取loading
     */
    LoadingDialog getLoadingDialog();

    /**
     * 网络错误（无网络）
     *
     * @param errorCode    错误码
     *                     *
     * @param errorMessage 错误信息
     */
    void onNetError(int errorCode, String errorMessage);
}
