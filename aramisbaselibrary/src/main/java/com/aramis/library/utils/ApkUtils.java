package com.aramis.library.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;


import java.io.File;

/**
 * ApkUtils
 * Created by Aramis on 2017/3/6.
 */

public class ApkUtils {
    public final static String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "yhh" + File.separator;
    private final static String apkFolder = basePath + "apk";
    public final static String packageName = "com.cbt.yhh";
    public final static String newApkPath = apkFolder + File.separator + "new.apk";
    public final static String patchPath = apkFolder + File.separator + "apk.patch";
    private static final String TAG = "===ApkUtils===";

    public static void createApkFolder() {
        File file = new File(apkFolder);
        if (!file.exists()) file.mkdirs();
    }


    public int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取已安装Apk文件的源Apk文件
     * 如：/data/app/com.sina.weibo-1.apk
     *
     * @param context
     * @return
     */
    public static String getSourceApkPath(Context context) {
        if (TextUtils.isEmpty(packageName))
            return null;

        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(packageName, 0);
            return appInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 安装Apk
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //没有这一步的话，最后安装好了，点打开，是不会打开新版本应用的
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.cbt.yhh", new File(apkPath));
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(Uri.fromFile(new File(apkPath)),"application/vnd.android.package-archive");
        }

//        intent.setDataAndType(Uri.parse("file://" + apkPath),
//                "application/vnd.android.package-archive");
        context.startActivity(intent);
        //友盟用来保存数据用
//        MobclickAgent.onKillProcess(context);
        //没有这一步，最后不会提示完成、打开
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
