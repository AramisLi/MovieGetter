package com.moviegetter.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import com.aramis.library.http.ArRxVolley
import com.aramis.library.http.SingalRetryPolicy
import com.aramis.library.utils.ApkUtils
import com.kymjs.rxvolley.client.FileRequest
import com.kymjs.rxvolley.client.HttpCallback
import com.kymjs.rxvolley.client.RequestConfig
import com.kymjs.rxvolley.http.VolleyError
import com.moviegetter.R
import com.moviegetter.ui.MainActivity
import org.jetbrains.anko.toast
import java.io.File

/**
 *Created by Aramis
 *Date:2018/9/17
 *Description:
 */
class VersionService : Service() {
    private var mNotificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null

    private var downloadUrl = ""
    private var downFilePath = ""
    private var downloadFileFolder = ""
    private var lastL = 0L

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (mNotificationManager == null) {
            initNotification()
        }
        intent?.apply {
            downloadUrl = intent.getStringExtra("downloadUrl")
            downFilePath = intent.getStringExtra("downFilePath")

            logE("downloadUrl:$downloadUrl,downFilePath:$downFilePath")

            if (downloadUrl.isNotBlank() && downFilePath.isNotBlank()) {
                downloadFileFolder = downFilePath.substring(0, downFilePath.lastIndexOf("/"))
                downloadFile(downloadUrl)
            } else {
                toast("params null. downloadUrl:$downloadUrl,downFilePath:$downFilePath")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun downloadFile(url: String) {
        val file = File(downloadFileFolder);
        if (!file.exists()) file.mkdirs();
//        File file1 = new File(GlobalPrams.getApkFilePath());
//        if (file1.exists()) file1.delete();
        //下载文件路径
        val filePath = downFilePath

        val requestConfig = RequestConfig()
        requestConfig.mUrl = url
        requestConfig.mRetryPolicy = SingalRetryPolicy();
        requestConfig.mTimeout = 90 * 1000
        val fileRequest = FileRequest(filePath, requestConfig,
                object : HttpCallback() {

                    override fun onSuccess(t: String?) {
                        super.onSuccess(t)
                        ArBus.getDefault().post(getBundle(true, 100, 100));
                        notifyNotification("下载完成", 100);
                        ApkUtils.installApk(this@VersionService, downFilePath);
                        setNotificationClick();
                        stopSelf();
                    }

                    override fun onFailure(error: VolleyError?) {
                        super.onFailure(error)
                        logE("$error")
                        val bundle = Bundle()
                        bundle.putBoolean("isUpdateFail", true)
                        notifyNotification("下载失败", 0)
                        ArBus.getDefault().post(bundle)
                        stopSelf()
                    }

                })
        fileRequest.setOnProgressListener { transferredBytes, totalSize ->
            val l = System.currentTimeMillis();
            if (l - lastL >= 1000) {
//                    LogUtils.e("开始进度", "per:" + transferredBytes + ",total:" + totalSize);
                lastL = l
                notifyNotification("下载中", (transferredBytes * 100 / totalSize).toInt())
                ArBus.getDefault().post(getBundle(false, transferredBytes, totalSize))
            }
        }
        ArRxVolley.Builder().setRequest(fileRequest).doTask()
    }

    private fun notifyNotification(title: String, percent: Int) {
        if (title.isNotBlank()) {
            mBuilder?.setContentTitle(title);
        }
        mBuilder?.setContentText(percent.toString() + "%")?.setProgress(100, percent, false);
        mNotificationManager?.notify(12, mBuilder?.build())
    }

    private fun getBundle(isFinish: Boolean, transferredBytes: Long, totalSize: Long): Bundle {
        val bundle = Bundle();
        bundle.putBoolean("isUpdateVersion", true);
        bundle.putBoolean("isUpdateFinish", isFinish);
        bundle.putLong("total", totalSize);
        bundle.putLong("transfer", transferredBytes);
        if (totalSize != 0L) {
            bundle.putDouble("percent", transferredBytes.toDouble() / totalSize)
        }
        return bundle
    }

    private fun initNotification() {
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = NotificationCompat.Builder(this);
        mBuilder?.setContentTitle("正在下载")?.setContentText("0%")?.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder?.setAutoCancel(true);
    }

    private fun setNotificationClick() {
        val notifyIntent = Intent(this, MainActivity::class.java)
        notifyIntent.putExtra("apkPath", downFilePath);
//        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        val pendingNotifyIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder?.setContentIntent(pendingNotifyIntent);
    }
}