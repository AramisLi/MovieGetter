package com.aramis.library.http;

import android.text.TextUtils;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.FileRequest;
import com.kymjs.rxvolley.client.FormRequest;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.client.JsonRequest;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.client.RequestConfig;
import com.kymjs.rxvolley.http.Request;
import com.kymjs.rxvolley.http.RetryPolicy;
import com.kymjs.rxvolley.rx.Result;
import com.kymjs.rxvolley.rx.RxBus;

import rx.Observable;
import rx.functions.Func1;

/**
 * 重写rxVolley
 * Created by Aramis on 2016/11/23.
 */

public class ArRxVolley {

    private static int DEFAULT_CONN_TIMEOUT = 1000 * 30; // 30s

    public void setDefaultConnTimeout(int timeout) {
        DEFAULT_CONN_TIMEOUT = timeout;
    }

    public static void get(String url, HttpCallback callback) {
        new Builder().url(url).callback(callback).doTask();
    }

    public static void get(String url, HttpParams params, HttpCallback callback,boolean useCache) {
        new Builder().url(url).params(params).useServerControl(!useCache).callback(callback).doTask();
    }

    public static void post(String url, HttpParams params, HttpCallback callback) {
        new Builder().url(url).params(params).httpMethod(RxVolley.Method.POST).callback(callback).timeout(DEFAULT_CONN_TIMEOUT).doTask();
    }

    public static void post(String url, HttpParams params, ProgressListener listener,
                            HttpCallback callback) {
        new Builder().url(url).params(params).progressListener(listener).httpMethod(RxVolley.Method.POST)
                .callback(callback).doTask();
    }

    public static void jsonPost(String url, HttpParams params, HttpCallback callback) {
        new Builder().url(url).params(params).contentType(RxVolley.ContentType.JSON)
                .httpMethod(RxVolley.Method.POST).callback(callback).doTask();
    }

    public static void download(String storeFilePath, String url, ProgressListener
            progressListener, HttpCallback callback) {
        RequestConfig config = new RequestConfig();
        config.mUrl = url;
        FileRequest request = new FileRequest(storeFilePath, config, callback);
        request.setOnProgressListener(progressListener);
        new Builder().setRequest(request).doTask();
    }

    /**
     * 构建器
     */
    public static class Builder {
        private HttpParams params;
        private int contentType;
        private HttpCallback callback;
        private Request<?> request;
        private ProgressListener progressListener;
        private RequestConfig httpConfig = new RequestConfig();

        /**
         * Http请求参数
         */
        public Builder params(HttpParams params) {
//            if (Constant.isLogin) {
//                params.put("userId", Integer.parseInt(Constant.sessionId));
//            }
            this.params = params;
            return this;
        }

        /**
         * 参数的类型:FORM表单,或 JSON内容传递
         */
        public Builder contentType(int contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * 请求回调,不需要可以为空
         */
        public Builder callback(HttpCallback callback) {
            this.callback = callback;
            return this;
        }

        /**
         * HttpRequest
         */
        public Builder setRequest(Request<?> request) {
            this.request = request;
            return this;
        }

        /**
         * HttpRequest的配置器
         *
         */
        public Builder httpConfig(RequestConfig httpConfig) {
            this.httpConfig = httpConfig;
            return this;
        }

        /**
         * 请求超时时间,如果不设置则使用重连策略的超时时间,默认3000ms
         */
        public Builder timeout(int timeout) {
            this.httpConfig.mTimeout = timeout;
            return this;
        }

        /**
         * 上传进度回调
         *
         * @param listener 进度监听器
         */
        public Builder progressListener(ProgressListener listener) {
            this.progressListener = listener;
            return this;
        }

        /**
         * 为了更真实的模拟网络,如果读取缓存,延迟一段时间再返回缓存内容
         */
        public Builder delayTime(int delayTime) {
            this.httpConfig.mDelayTime = delayTime;
            return this;
        }

        /**
         * 缓存有效时间,单位分钟
         */
        public Builder cacheTime(int cacheTime) {
            this.httpConfig.mCacheTime = cacheTime;
            return this;
        }

        /**
         * 是否使用服务器控制的缓存有效期(如果使用服务器端的,则无视#cacheTime())
         */
        public Builder useServerControl(boolean useServerControl) {
            this.httpConfig.mUseServerControl = useServerControl;
            return this;
        }

        /**
         * 查看RequestConfig$Method
         * GET/POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
         */
        public Builder httpMethod(int httpMethod) {
            this.httpConfig.mMethod = httpMethod;
            if (httpMethod == RxVolley.Method.POST) {
                this.httpConfig.mShouldCache = false;
            }
            return this;
        }

        /**
         * 是否启用缓存
         */
        public Builder shouldCache(boolean shouldCache) {
            this.httpConfig.mShouldCache = shouldCache;
            return this;
        }

        /**
         * 网络请求接口url
         */
        public Builder url(String url) {
            this.httpConfig.mUrl = url;
            return this;
        }

        /**
         * 重连策略,不传则使用默认重连策略
         */
        public Builder retryPolicy(RetryPolicy retryPolicy) {
            this.httpConfig.mRetryPolicy = retryPolicy;
            return this;
        }

        /**
         * 编码,默认UTF-8
         */
        public Builder encoding(String encoding) {
            this.httpConfig.mEncoding = encoding;
            return this;
        }

        private Builder build() {
            if (request == null) {
                if (params == null) {
                    params = new HttpParams();
                } else {
                    if (httpConfig.mMethod == RxVolley.Method.GET)
                        httpConfig.mUrl += params.getUrlParams();
                }

                if (httpConfig.mShouldCache == null) {
                    //默认情况只对get请求做缓存
                    if (httpConfig.mMethod == RxVolley.Method.GET) {
                        httpConfig.mShouldCache = Boolean.TRUE;
                    } else {
                        httpConfig.mShouldCache = Boolean.FALSE;
                    }
                }

                if (contentType == RxVolley.ContentType.JSON) {
                    request = new JsonRequest(httpConfig, params, callback);
                } else {
                    request = new FormRequest(httpConfig, params, callback);
                }

                request.setOnProgressListener(progressListener);

                if (TextUtils.isEmpty(httpConfig.mUrl)) {
                    throw new RuntimeException("Request url is empty");
                }
            }
            if (callback != null) {
                callback.onPreStart();
            }
            return this;
        }

        /**
         * 执行请求任务,并返回一个RxJava的Observable类型
         */
        public Observable<Result> getResult() {
            doTask();
            return RxBus.getDefault().take(Result.class)
                    .filter(new Func1<Result, Boolean>() {
                        @Override
                        public Boolean call(Result result) {
                            return httpConfig.mUrl.equals(result.url);
                        }
                    })
                    .take(1);
        }

        /**
         * 执行请求任务
         */
        public void doTask() {
            if (this.httpConfig.mTimeout == 0) httpConfig.mTimeout = DEFAULT_CONN_TIMEOUT;
            if (this.httpConfig.mRetryPolicy == null)
                this.httpConfig.mRetryPolicy = new SingalRetryPolicy();
            build();
            RxVolley.getRequestQueue().add(request);
        }
    }
}
