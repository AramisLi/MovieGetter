package com.aramis.library.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


/**
 * BunnyApplication
 * Created by Aramis on 2017/4/26.
 */

public class BunnyApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        BunnySP.Companion.init(this);
//        UiUtil.initUiParams(this);
//        //同盾
//        try {
//            FMAgent.init(this, FMAgent.ENV_PRODUCTION);
//            if (LogUtils.isDebug()) {
//                String blackBox = FMAgent.onEvent(this);
//                LogUtils.e("===blackBox", blackBox);
//            }
//        } catch (FMException e) {
//            e.printStackTrace();
//        }
//        registerUMeng();
    }

//    private void registerUMeng() {
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        //注册推送服务，每次调用register方法都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回device token
//                LogUtils.e("===友盟注册", "success：" + deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                LogUtils.e("===友盟注册", "onFailure s:" + s + ",s1:" + s1);
//            }
//        });
//        mPushAgent.setDebugMode(LogUtils.isDebug());
//    }


}
