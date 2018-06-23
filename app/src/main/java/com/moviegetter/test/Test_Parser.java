package com.moviegetter.test;

import org.seimicrawler.xpath.JXDocument;

/**
 * Created by Aramis
 * Date:2018/6/23
 * Description:
 */
public class Test_Parser {
    String html = Detalhtml.html;
    JXDocument jxDocument = JXDocument.create(html);

    public static void main(String[] args) {
        System.out.println("我是大帅哥");
    }
}
