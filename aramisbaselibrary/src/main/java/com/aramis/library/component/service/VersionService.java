package com.aramis.library.component.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.kymjs.rxvolley.client.FileRequest;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.client.RequestConfig;

import java.io.File;

/**
 * VersionService 版本更新
 * Created by Aramis on 2017/6/25.
 */

public class VersionService {
//        extends Service {
//    private VersionBean versionBean;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (mNotificationManager == null) {
//            initNotification();
//        }
//        String url = "";
//        if (intent != null) {
//            versionBean = (VersionBean) intent.getSerializableExtra("bean");
//            url = versionBean.getVersionsUrl();
//        }
//        LogUtils.e("onStartCommand", "url:" + url);
//        if (versionBean != null && !TextUtils.isEmpty(url)) {
//            downloadFile(url);
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private long cc = 0;
//
//    private String getDownloadFileFolder() {
//        return GlobalPrams.getRootFolder() + File.separator + "apk";
//    }
//
//    private String getDownloadFilePath() {
//        if (versionBean != null) {
//            return getDownloadFileFolder() + File.separator + "yhh" + versionBean.getVersionsName() + ".apk";
//        }
//        return "";
//    }
//
//    private void downloadFile(String url) {
//        File file = new File(getDownloadFileFolder());
//        if (!file.exists()) file.mkdirs();
////        File file1 = new File(GlobalPrams.getApkFilePath());
////        if (file1.exists()) file1.delete();
//        //下载文件路径
//        String filePath = getDownloadFilePath();
//
//        RequestConfig requestConfig = new RequestConfig();
//        requestConfig.mUrl = url;
//        requestConfig.mRetryPolicy = new SingalRetryPolicy();
//        requestConfig.mTimeout = 90 * 1000;
//        FileRequest fileRequest = new FileRequest(filePath, requestConfig,
//                new HttpCallback() {
//                    @Override
//                    public void onSuccess(String t) {
//                        super.onSuccess(t);
//                        LogUtils.e("onSuccess", "下载成功");
//                        ArBus.getDefault().post(getBundle(true, 100, 100));
//                        notifyNotification("下载完成", 100);
//                        ApkUtils.installApk(VersionService.this, getDownloadFilePath());
//                        setNotificationClick();
//                        stopSelf();
//                    }
//
//                    @Override
//                    public void onFailure(int errorNo, String strMsg) {
//                        super.onFailure(errorNo, strMsg);
//                        LogUtils.e("onSuccess", "下载失败");
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean("isUpdateFail", true);
//                        notifyNotification("下载失败", 0);
//                        ArBus.getDefault().post(bundle);
//                        stopSelf();
//                    }
//                });
//        fileRequest.setOnProgressListener(new ProgressListener() {
//            @Override
//            public void onProgress(long transferredBytes, long totalSize) {
////                LogUtils.e("开始进度", "per:" + transferredBytes + ",total:" + totalSize);
//                long l = System.currentTimeMillis();
//                if (l - cc >= 1000) {
//                    LogUtils.e("开始进度", "per:" + transferredBytes + ",total:" + totalSize);
//                    cc = l;
//                    notifyNotification("下载中", (int) (transferredBytes * 100 / totalSize));
//                    ArBus.getDefault().post(getBundle(false, transferredBytes, totalSize));
//                }
//            }
//        });
//        new ArRxVolley.Builder().setRequest(fileRequest).doTask();
//    }
//
//    private Bundle getBundle(boolean isFinish, long transferredBytes, long totalSize) {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("isUpdateVersion", true);
//        bundle.putBoolean("isUpdateFinish", isFinish);
//        bundle.putLong("total", totalSize);
//        bundle.putLong("transfer", transferredBytes);
//        if (totalSize != 0) {
//            bundle.putDouble("percent", transferredBytes / totalSize);
//        }
//        return bundle;
//    }
//
//    private NotificationManager mNotificationManager;
//    private NotificationCompat.Builder mBuilder;
//
//    /**
//     * 更新通知
//     */
//    private void notifyNotification(String title, int percent) {
//        if (!TextUtils.isEmpty(title)) {
//            mBuilder.setContentTitle(title);
//        }
//        mBuilder.setContentText(percent + "%").setProgress(100, percent, false);
//        mNotificationManager.notify(12, mBuilder.build());
//    }
//
//    private void initNotification() {
//        mNotificationManager =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mBuilder = new NotificationCompat.Builder(this);
//        mBuilder.setContentTitle("正在下载")
//                .setContentText("0%")
//                .setSmallIcon(R.mipmap.ic_launcher);
//        mBuilder.setAutoCancel(true);
//    }
//
//    private void setNotificationClick() {
//        Intent notifyIntent = new Intent(this, OldMainActivity.class);
//        notifyIntent.putExtra("apkPath", getDownloadFilePath());
////        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingNotifyIntent =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        notifyIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//
//        mBuilder.setContentIntent(pendingNotifyIntent);
//    }
}
