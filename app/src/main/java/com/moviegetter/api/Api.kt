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

    //toutiao video
    fun tt_video_list(): String {
        val loc_time = System.currentTimeMillis() / 1000
        val min_behot_time = loc_time + 12061942
        val last_refresh_sub_entrance_interval = loc_time - 10498
        return "http://is.snssdk.com/api/news/feed/v75/?list_count=20&category=video&refer=1&refresh_reason=5&session_refresh_idx=1&count=20&min_behot_time=$min_behot_time&list_entrance=main_tab&last_refresh_sub_entrance_interval=$last_refresh_sub_entrance_interval&loc_mode=6&loc_time=$loc_time&latitude=39.985125&longitude=116.467418&city=%E5%8C%97%E4%BA%AC%E5%B8%82&tt_from=enter_auto&lac=4219&cid=5503&plugin_enable=3&iid=28559767826&device_id=32386258865&ac=wifi&channel=tengxun&aid=13&app_name=news_article&version_code=655&version_name=6.5.5&device_platform=android&ab_version=830855%2C859673%2C754087%2C770571%2C859429%2C662176%2C674053%2C751912%2C770480%2C170988%2C643893%2C374117%2C788446%2C550042%2C435216%2C654104%2C649427%2C677128%2C522766%2C416055%2C710077%2C801968%2C707372%2C603440%2C789246%2C868824%2C800208%2C783645%2C603381%2C603399%2C603404%2C603406%2C866773%2C833900%2C844799%2C869957%2C661902%2C668775%2C832706%2C848659%2C737594%2C802346%2C788012%2C795195%2C792681%2C607361%2C739392%2C764921%2C662099%2C775238%2C673223%2C812271%2C867976%2C668774%2C766806%2C864175%2C855440%2C773349%2C775311%2C765193%2C549647%2C615291%2C546703%2C798353%2C757280%2C853743%2C679100%2C735201%2C767991%2C861913%2C779958%2C660830%2C856474%2C661781%2C457480%2C649402%2C860529&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=94570%2C102752%2C181431&ab_feature=94570%2C102752&abflag=3&ssmix=a&device_type=Letv+X501&device_brand=Letv&language=zh&os_api=23&os_version=6.0&uuid=868897020889812&openudid=2aa1a7301fce5dc8&manifest_version_code=655&resolution=1080*1920&dpi=480&update_version_code=65509&_rticket=1557113240659&plugin=10575&fp=2rTqcrUWLzmSFlwSLSU1FYceJlGW&rom_version=23&ts=1557113194&as=a2e5ba9ccab6ec397f4340&cp=59cdc4fba9998q1"
    }
}