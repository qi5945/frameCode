package com.micropole.chebianjie.test

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xx.baseuilibrary.mvp.lcec.BaseMvpLeceListFragment

/**
 * @ClassName       TestFragment
 * @Model           todo
 * @Description     todo
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2019/4/2 9:29
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
class TestFragment : BaseMvpLeceListFragment<PostEntity, HomePostContract.Model, HomePostContract.View, HomePostPresenter>() ,HomePostContract.View {
    override fun loadListData(isRefresh: Boolean) {

    }

    override fun lazyLoad() {

    }

    override fun initData() {

    }

    override fun isDefault(): Boolean = true

    override fun getSort(): String = ""

    override fun getOrder(): String =""

    override fun getClassifyId(): String = "1"

    override fun createAdapter(): BaseQuickAdapter<PostEntity, BaseViewHolder> = PostAdapter(arrayListOf())

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(mContext)

    override fun openLoadMore(): Boolean = true

    override fun createPresenter(): HomePostPresenter = HomePostPresenter()
}