package com.moviegetter.aac

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moviegetter.R
import com.moviegetter.aac.reddit.RedditPostRepository
import com.moviegetter.aac.reddit.SubRedditViewModel
import java.util.*

/**
 * Created by lizhidan on 2019/1/22.
 */
class RedditActivity : AppCompatActivity() {
    companion object {

        const val KEY_SUBREDDIT = "subreddit"
        const val DEFAULT_SUBREDDIT = "androiddev"
        const val KEY_REPOSITORY_TYPE = "repository_type"

        fun intentFor(context: Context, type: RedditPostRepository.Type): Intent {
            val intent = Intent(context, RedditActivity::class.java)
            intent.putExtra(KEY_REPOSITORY_TYPE, type.ordinal)
            return intent
        }
    }

    private lateinit var model: SubRedditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reddit)
//        model=getViewModel()
    }

//    private fun getViewModel(): SubRedditViewModel {
//        return ViewModelProviders.of(this,object :ViewModelProvider.Factory{
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                val repoTypeParam=intent.getIntExtra(KEY_REPOSITORY_TYPE,0)
//                val repoType=RedditPostRepository.Type.values()[repoTypeParam]
////                val repo=ServiceLoader.in
//            }
//
//        })[SubRedditViewModel::class.java]
//    }


}