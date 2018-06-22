package com.aramis.library.aramis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aramis.library.R;
import com.aramis.library.base.BaseActivity;
import com.aramis.library.base.BasePresenter;
import com.aramis.library.utils.ScreenUtils;

/**
 * @author aramis
 * @Description 内置浏览器
 */
public class WebBrowserActivity extends BaseActivity {
    public static final String WEB_URL = "webUrl", WEB_TITLE = "title", WEB_FLAG = "flag";
    private WebView webView;
    private ProgressBar progressBar;
    private String title, webUrl, flag = " ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setDefaultTitleColor(this);
        setContentView(R.layout.activity_web);
        getDataFromIntent();
        initView();
        initWebView();
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                // 显示正在加载对话框
//                Log.v("jpush", "web is loading");
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 网页加载完成后,显示出来
                view.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
                // 隐藏加载框
//                Log.v("jpush", "web has loaded");

				/**/
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.line_titlebar).setVisibility(View.VISIBLE);
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    findViewById(R.id.line_titlebar).setVisibility(View.VISIBLE);
                } else {
                    progressBar.setProgress(progress);
                }
                Log.v("jpush", "web url is loading progress is " + progress);

            }

        });
        if (!TextUtils.isEmpty(webUrl)) {
            webView.loadUrl(webUrl);
        }
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        ImageView backIv = (ImageView) findViewById(R.id.image_toolbar_left);
        TextView titelTextVeiw = (TextView) findViewById(R.id.text_toolbar_middle);
        backIv.setVisibility(View.VISIBLE);
        titelTextVeiw.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }

            }
        });
        if (!TextUtils.isEmpty(title)) {
            titelTextVeiw.setText(title);
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        webUrl = intent.getStringExtra(WEB_URL);
        flag = intent.getStringExtra(WEB_FLAG);
        title = intent.getStringExtra(WEB_TITLE);
    }

    @Nullable
    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    /*
     * 返回键退回上一个页面
     * 时间：2015年11月3日15:13:02
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        flag = getIntent().getStringExtra("flag");
//		if (!StringUtil.isEmpty(flag)) {flag.equals("1") || flag.equals("2") || flag.equals("3")
        if (!TextUtils.isEmpty(flag)) {
            finish();
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getRepeatCount() == 0) {
                // 返回上一个页面
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            }
        }
//		}
        return super.onKeyDown(keyCode, event);
    }

    public void gotoMsgActivity() {
        this.finish();
    }
}
