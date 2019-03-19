package com.moviegetter.api

/**
 *Created by Aramis
 *Date:2018/6/29
 *Description:
 */
object Api {
    //    const val baseUrl = "http://192.168.40.6:5001/"
    const val baseUrl = "http://180.76.190.163:5001/"

    const val index = baseUrl + "index"
    const val createUser = baseUrl + "create_user"
    const val getRole = baseUrl + "get_role"
    const val markIn = baseUrl + "mark_in"
    const val markInIp = baseUrl + "mark_in_ip"
    const val markOut = baseUrl + "mark_out"
    const val markMovie = baseUrl + "mark_movie"
    const val apk = baseUrl + "download/apk"
    const val checkVersion = baseUrl + "check_version"

    //movie
    const val movie_last = "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html"

    //tv
    const val tv_host = "http://www.zhiboo.net/"

    //dyg
    const val dyg = "http://www.dygang.net/"
    const val dyg_zuixin = dyg + "ys/"
    const val dyg_jingdian = dyg + "bd/"
    const val dyg_guopei = dyg + "gy/"
    const val dyg_xianggang = dyg + "gp/"
    const val dyg_dianshiju = dyg + "dsj/"
    const val dyg_dianshiju_korea = dyg + "dsj1/"
    const val dyg_dianshiju_us = dyg + "yx/"
    const val dyg_zongyi = dyg + "zy/"
    const val dyg_dongman = dyg + "dmq/"
    const val dyg_jilupian = dyg + "jilupian/"
    const val dyg_1080p = dyg + "1080p/"
    const val dyg_4k = dyg + "4K/"
    const val dyg_3d = dyg + "3d/"
    const val dyg_dyzt = dyg + "dyzt/"
}