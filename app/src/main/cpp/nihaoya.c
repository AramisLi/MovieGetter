//
// Created by 李志丹 on 2018/8/9.
//
#include <jni.h>
#include <android/log.h>

#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"Aramis",FORMAT,__VA_ARGS__)
#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"Aramis",FORMAT,__VA_ARGS__)

JNICALL jstring JNICALL
Java_com_moviegetter_config_MGsp_getIPZDefaultStr(JNIEnv *env, jobject jobj) {
    jstring new_str = (*env)->NewStringUTF(env, "http://www.xfa50.com");
    return new_str;
}

JNICALL jstring JNICALL
Java_com_moviegetter_config_MGsp_getIPZPicDefaultStr(JNIEnv *env, jobject jobj) {
    jstring new_str = (*env)->NewStringUTF(env, "http://www.ssssbb.com");
    return new_str;
}
JNICALL jstring JNICALL
Java_com_moviegetter_config_MGsp_getXfyyDefaultStr(JNIEnv *env, jobject jobj) {
    jstring new_str = (*env)->NewStringUTF(env, "http://www.xfyy166.com");
    return new_str;
}

JNICALL jstring JNICALL
Java_com_moviegetter_ui_MainActivity_getIPZDefaultStr(JNIEnv *env, jobject jobj) {

    //从java获取一个String属性
//    jclass mainActivity = (*env)->GetObjectClass(env, jobj);
//    jfieldID key_id = (*env)->GetFieldID(env, mainActivity, "key", "Ljava/lang/String;");
//    jstring jstr = (*env)->GetObjectField(env, jobj, key_id);
//
//    //将jstring转成 c的字符串
//    char *c_str = (char *) (*env)->GetStringUTFChars(env, jstr, JNI_FALSE);
//    //拼接字符串
//    char w[20] = "asdfasdfas";
//    strcat(w, c_str);

    //c string-> jstring

//    jstring  new_str=(*env)->NewStringUTF(env,w);
    jstring new_str = (*env)->NewStringUTF(env, "http://www.xfa50.com");
    LOGE("测试Android打印%d", 1);
    LOGI("测试Android打印%d", 1);

    return new_str;
}

