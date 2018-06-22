package com.moviegetter.base

import com.aramis.library.base.BasePresenter
import com.aramis.library.base.BaseView

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
open class MGBasePresenter<T : BaseView>(view: T) : BasePresenter<T>(view) {

}