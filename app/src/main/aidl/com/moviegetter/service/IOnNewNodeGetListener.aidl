package com.moviegetter.service;

//import com.moviegetter.service.SpiderTask;
import com.moviegetter.crawl.base.CrawlNode;

interface IOnNewNodeGetListener{
    void onNewNodeGet(in CrawlNode crawlNode);
    void onError(in int errorCode,in String errorMsg);
    void onFinished();
}