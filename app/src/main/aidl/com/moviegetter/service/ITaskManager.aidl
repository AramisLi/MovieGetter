// ITaskManager.aidl
package com.moviegetter.service;

import com.moviegetter.service.SpiderTask;
import com.moviegetter.service.IOnNewNodeGetListener;

interface ITaskManager {
    void add(in SpiderTask spiderTask);
    void cancel();

    void registerListener(IOnNewNodeGetListener listener);
    void unregisterListener(IOnNewNodeGetListener listener);
}
