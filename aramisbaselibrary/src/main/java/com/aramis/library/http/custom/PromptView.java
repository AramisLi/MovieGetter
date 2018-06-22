package com.aramis.library.http.custom;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PromptView extends LinearLayout {

    private ImageView mPromptView;
    private TextView mPromptText;
    private TextView mPromptTextBottom;
    private TextView mPromptAdd;

    public PromptView(Context context) {
        super(context);
    }

    public PromptView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PromptView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        init();

    }

    private void init() {
        mPromptView = (ImageView) getChildAt(0);
        mPromptText = (TextView) getChildAt(1);
        mPromptTextBottom = (TextView) getChildAt(2);
        mPromptAdd = (TextView) getChildAt(3);
    }

    public void setLoadingText(String topString) {
        setLoadingText(topString, null);
    }

    /**
     * 设置加载文字。
     *
     * @param topString
     */
    public void setLoadingText(String topString, String bottomString) {
        if (mPromptText != null && !TextUtils.isEmpty(topString)) {
            mPromptText.setText(topString);
            mPromptText.setVisibility(View.VISIBLE);

        }

        if (mPromptTextBottom != null && !TextUtils.isEmpty(bottomString)) {
            mPromptTextBottom.setText(bottomString);
            mPromptTextBottom.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置加载文字。
     *
     * @param text
     */
    public void setAddText(String text) {
        if (mPromptAdd != null && !TextUtils.isEmpty(text)) {
            mPromptAdd.setText(text);
            mPromptAdd.setVisibility(View.VISIBLE);

        }

    }

    /**
     * 设置加载文字颜色。
     *
     * @param
     */
    public void setLoadingTextColor(int colorId, int color) {
        if (mPromptText != null) {
            mPromptText.setTextColor(colorId);
        }
        if (mPromptTextBottom != null) {
            mPromptTextBottom.setTextColor(color);
        }
    }

    public void setLoadingImg(int resImg) {
        if (mPromptView != null) {
            mPromptView.setBackgroundResource(resImg);
            mPromptView.setVisibility(View.VISIBLE);
        }
    }

    public void setNoDate() {
        mPromptText.setVisibility(View.VISIBLE);
        mPromptTextBottom.setVisibility(View.VISIBLE);
    }

    public void setSearchNoData(String str) {
        mPromptView.setVisibility(View.GONE);
        mPromptText.setVisibility(View.GONE);
        if (mPromptTextBottom != null && !TextUtils.isEmpty(str)) {
            mPromptTextBottom.setText(str);
            mPromptTextBottom.setVisibility(View.VISIBLE);
        }
    }

    public void setLoadingViewVisibility(boolean isShow) {
        if (mPromptView != null) {
            mPromptView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }

    }

}
