package com.aramis.library.http;

import com.kymjs.rxvolley.http.RetryPolicy;
import com.kymjs.rxvolley.http.VolleyError;

/**
 * 只请求一次
 * Created by Aramis on 2016/9/23.
 */

public class SingalRetryPolicy implements RetryPolicy {
    @Override
    public int getCurrentTimeout() {
        return 90*1000;
    }

    @Override
    public int getCurrentRetryCount() {
        return 0;
    }

    @Override
    public void retry(VolleyError error) throws VolleyError {

    }
}
