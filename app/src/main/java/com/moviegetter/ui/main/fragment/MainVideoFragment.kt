package com.moviegetter.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jzvd.Jzvd
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import kotlinx.android.synthetic.main.frg_main_video.view.*


/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:小视频
 */
class MainVideoFragment : MGBaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main_video, container, false)
        initView()
        return mRootView
    }

    private fun initView() {
        val test1="http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
        val test2="https://ww2.juji.tv/m3u8dp.php?url=https://cn2.zuidadianying.com/20171021/nYU2UYiY/index.m3u8"
        val test3="http://g.alicdn.com/de/prismplayer-flash/1.2.16/PrismPlayer.swf?vurl=http://cctv5.txty.5213.liveplay.myqcloud.com/live/cctv1_txty.m3u8&amp;autoPlay=true"
        val test4="http://221.120.177.59/hls/i3d7ragr.m3u8"
        val test5="http://main.gslb.ku6.com/broadcast/sub?channel=910"
        val jzvdStd = mRootView.videoplayer
        jzvdStd.setUp(test4, "饺子闭眼睛", Jzvd.SCREEN_WINDOW_NORMAL)
//        jzvdStd.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640")
//        jzvdStd.thumbImageView.setimage
    }
}