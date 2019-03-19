package com.moviegetter.bean

import com.moviegetter.config.DBConfig
import com.moviegetter.extentions.AccountManager
import java.io.Serializable

/**
 * Created by lizhidan on 2019/2/13.
 */
data class UserCenterOption(val id: Int, val name: String) {

    companion object {
        const val OPTION_ID_NEWWORLD = 6

        fun getOptions(): List<UserCenterOption> {
            return listOf(
                    UserCenterOption(1, "下载过的电影"),
                    UserCenterOption(2, "喜欢的电视"),
                    UserCenterOption(3, "收藏的视频"),
                    UserCenterOption(4, "版本信息"),
                    UserCenterOption(5, "设置")
            )
        }

        fun getNewWorldOption(): UserCenterOption {
            return UserCenterOption(OPTION_ID_NEWWORLD, "新世界")
        }


    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserCenterOption

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}

data class SettingOption(val id: Int, val name: String, val type: Int, val description: String?) : Serializable {

    companion object {
        fun getOptions(versionName: String): List<SettingOption> {
            val list = mutableListOf(
                    SettingOption(1, "当前版本", 1, versionName),
                    SettingOption(2, "显示图片", 0, "显示新世界图片的开关"),
                    SettingOption(3, "标记已下载", 0, "打开后，会在点击下载按钮的时候标记并显示在页面上"),
                    SettingOption(4, "首页自动更新", 0, "首页的电影/网络电视/小视频会在app打开时自动更新")
            )
            if (AccountManager.getRole() != DBConfig.USER_ROLE_NORMAL) {
                list.add(SettingOption(5, "显示新世界", 0, "显示新世界的开关"))
            }
            return list
        }
    }
}