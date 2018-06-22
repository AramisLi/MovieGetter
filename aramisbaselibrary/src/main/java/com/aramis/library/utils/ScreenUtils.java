package com.aramis.library.utils;

import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import com.aramis.library.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 沉浸式
 * Created by Aramis on 2017/3/28.
 */

public class ScreenUtils {
    public static void setDefaultTitleColor(Activity activity) {
        setActionBarTransColor(activity, ContextCompat.getColor(activity, R.color.bg_toolbar));
    }

    public static void setActionBarTrans(Activity context) {
        setActionBarTrans(context, true, ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public static void setActionBarTrans(Activity context, int drawableResId) {
        setActionBarTrans(context, false, drawableResId);
    }

    public static void setActionBarTransColor(Activity context, int color) {
        setActionBarTrans(context, true, color);
    }

    public static void setActionBarTransColorRes(Activity context, int colorResId) {
        setActionBarTrans(context, true, ContextCompat.getColor(context, colorResId));
    }

    public static void setActionBarTranslucent(Activity context) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            context.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            context.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setTintColor(0x00000000);
        }
    }

    private static void setActionBarTrans(Activity context, boolean isColor, int res) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            // 透明状态栏
            context.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                context.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            // 透明导航栏
            context.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(false);
            if (isColor) {
                // 设置一个颜色给系统栏
                tintManager.setTintColor(res);
            } else {
                tintManager.setTintDrawable(context.getResources().getDrawable(res));
            }
        }
    }
}
