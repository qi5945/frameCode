package com.xx.baseuilibrary.mvp.lcec

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xx.baseuilibrary.R
import com.xx.baseuilibrary.mvp.presenter.BaseMvpPresenter
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_del_toolbar.*

/**
 * @ClassName       BaseMvpLeceListActivity
 * @Model           todo
 * @Description     todo
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2019/4/1 17:04
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
abstract class BaseMvpLeceListActivity<D, M, V : BaseMvpLcecView<List<D>>, P : BaseMvpPresenter<M, V>> :
    BaseMvpLcecActivity<SwipeRefreshLayout, List<D>, M, V, P>() {

    protected var mPage = 1

    override fun getActivityLayoutId(): Int = R.layout.activity_list

    protected val mAdapter by lazy {
        createAdapter()
    }

    override fun initEvent() {
        iv_back.setOnClickListener { finish() }
        myContentView!!.setOnRefreshListener { loadData(true) }
    }

    override fun initView() {
        super.initView()
        recyclerView.layoutManager = getRecyclerViewLayoutManager()
        mAdapter.bindToRecyclerView(recyclerView)
        if (openLoadMore())
            mAdapter.setOnLoadMoreListener{loadData(false)}
    }

    override fun loadDataFailure() {
        super.loadDataFailure()
        mPage -= 1
        mAdapter.loadMoreFail()
    }

    override fun loadData(refresh: Boolean) {
        if (refresh)
            mPage = 1
        else
            mPage += 1
        loadListData(refresh)
    }

    override fun setData(data: List<D>?) {
        when {
            myContentView!!.isRefreshing -> {
                mAdapter.setNewData(data)
                myContentView!!.isRefreshing = false
            }
            mAdapter.isLoading -> {
                mAdapter.addData(data!!)
                mAdapter.loadMoreComplete()
                mAdapter.notifyDataSetChanged()
            }
            else -> mAdapter.setNewData(data)
        }
        if (data!!.isEmpty()) {
            mAdapter.loadMoreEnd()
        }
        showContent()
    }

    /**
     * 列表的适配器
     */
    abstract fun createAdapter(): BaseQuickAdapter<D, BaseViewHolder>

    /**
     * 列表的布局管理器
     */
    abstract fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager

    /**
     * 是否开启加载更多
     */
    abstract fun openLoadMore(): Boolean

    /**
     * 数据加载
     */
    abstract fun loadListData(isRefresh:Boolean)

}