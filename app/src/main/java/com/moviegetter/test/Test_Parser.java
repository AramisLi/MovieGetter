package com.moviegetter.test;

import org.seimicrawler.xpath.JXDocument;

import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Aramis
 * Date:2018/6/23
 * Description:
 */
public class Test_Parser {
    private static String html = Detalhtml.html;


    public static void main(String[] args) {
        System.out.println("我是大帅哥");
        JXDocument jxDocument = JXDocument.create(html);
        List<Object> sel = jxDocument.sel("//div[@id='Zoom']/span/table//tr//a");
        for (int i = 0; i < sel.size(); i++) {
            print("==="+i);
            print(sel.get(i).getClass().getName());
//            print((String) sel.get(i));
            print(sel.get(i).toString());
        }
    }

    private static void print(String str) {
        System.out.println(str);
    }
}
