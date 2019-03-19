package com.moviegetter.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.bean.UserCenterOption
import com.moviegetter.config.DBConfig
import com.moviegetter.extentions.AccountManager
import com.moviegetter.ui.ipz.activity.IPZActivity
import com.moviegetter.ui.main.activity.SettingActivity
import com.moviegetter.ui.main.activity.VersionActivity
import com.moviegetter.ui.main.adapter.UserFragmentAdapter
import kotlinx.android.synthetic.main.frg_main_user.view.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import rx.Subscription

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:用户中心
 */
class MainUserFragment : MGBaseFragment() {
    private val optionsList = UserCenterOption.getOptions().toMutableList()
    private val adapter = UserFragmentAdapter(optionsList, -1)

    private lateinit var userSubscription: Subscription

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main_user, container, false)
        initView()
        setListener()
        return mRootView
    }

    private fun setListener() {
        mRootView.list_user.setOnItemClickListener { parent, view, position, id ->
            when (optionsList[position].id) {
                4 -> startActivity<VersionActivity>()
                5 -> startActivity<SettingActivity>()
                UserCenterOption.OPTION_ID_NEWWORLD -> {
                    startActivity<IPZActivity>()
                }
                else -> {
                    toast("敬请期待")
                }
            }
        }
    }

    private fun initView() {
        mRootView.list_user.adapter = adapter
        Glide.with(this).load(AccountManager.getAvatarResId()).into(mRootView.user_avatar)
        mRootView.user_name_display.text = AccountManager.getUserDisplayName()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSubscription = AccountManager.getUserSubscription {
            refreshOptions(it.role)
        }
        refreshOptions(AccountManager.getRole())
    }

    private fun refreshOptions(role: String) {
        if (role != DBConfig.USER_ROLE_NORMAL) {
            if (UserCenterOption.getNewWorldOption() !in optionsList){
                optionsList.add(UserCenterOption.getNewWorldOption())
            }
        } else {
            optionsList.remove(UserCenterOption.getNewWorldOption())
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        userSubscription.unsubscribe()
        super.onDestroy()
    }
}