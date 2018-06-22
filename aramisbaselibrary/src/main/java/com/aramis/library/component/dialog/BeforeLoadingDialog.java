package com.aramis.library.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import com.aramis.library.R;
import com.aramis.library.ui.dialog.LoadingDialog;
import com.aramis.library.widget.LoadingView;


/**
 * 等待框
 *
 * @author Aramis
 */

public class BeforeLoadingDialog implements LoadingDialog {
    private ImageView mLoading;
    private Context mContext;
    private Dialog dialog;
    private boolean isShowing;
    private LoadingView loadingView;

    public BeforeLoadingDialog(Context context) {
        this.mContext = context;
        dialog = new Dialog(mContext, R.style.dialog_loading);
        loadingView = new LoadingView(mContext);
        dialog.setContentView(loadingView);
    }

    public void setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
    }

    @Override
    public void show() {
        if (!isShowing) {
            loadingView.startAnim();
            isShowing = true;
            dialog.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing) {
            loadingView.release();
            isShowing = false;
            dialog.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return isShowing;
    }
}
