package com.xx.baseuilibrary

import android.os.Bundle
import android.view.View
import com.xx.baseuilibrary.mvp.BaseMvpFragment
import com.xx.baseuilibrary.mvp.BaseMvpView
import com.xx.baseuilibrary.mvp.presenter.BaseMvpPresenter

/**
 * Description:
 * Created by DarkHorse on 2018/7/5.
 */
abstract class LazyMvpFragment<M, V : BaseMvpView, P : BaseMvpPresenter<M, V>> : BaseMvpFragment<M, V, P>() {

    private var isViewCreated: Boolean = false  //判断是否已经创建View
    private var isUIVisible: Boolean = false    //判断View是否已经显示

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        startLazyLoad()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isUIVisible = isVisibleToUser
        startLazyLoad()
    }

    private fun startLazyLoad() {
        if (isViewCreated && isUIVisible) {
            lazyLoad()
            isViewCreated = false
        }
    }

    abstract fun lazyLoad()
}
