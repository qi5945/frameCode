package com.xx.baseuilibrary.mvp.lcec

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xx.baseuilibrary.LazyLeceMvpFragment
import com.xx.baseuilibrary.R
import com.xx.baseuilibrary.mvp.presenter.BaseMvpPresenter
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * @ClassName       BaseMvpLeceListFragment
 * @Model           todo
 * @Description     此Fragment实现了懒加载功能，使用懒加载请使用lazyLoad()，反之自己手动调用加载数据的方法
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2019/4/1 17:04
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
abstract class BaseMvpLeceListFragment<D, M, V : BaseMvpLcecView<List<D>>, P : BaseMvpPresenter<M, V>> :
        LazyLeceMvpFragment<SwipeRefreshLayout, List<D>, M, V, P>() {

    protected var mPage = 1

    override fun getFragmentLayoutId(): Int = R.layout.fragment_list

    protected val mAdapter by lazy {
        createAdapter()
    }

    override fun initEvent(view: View?) {
        view_content!!.setOnRefreshListener { loadData(true) }
    }

    override fun initView(view: View?) {
        super.initView(view)
        recyclerView.layoutManager = getRecyclerViewLayoutManager()
        mAdapter.bindToRecyclerView(recyclerView)
        if (openLoadMore())
            mAdapter.setOnLoadMoreListener { loadData(false) }
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
            view_content.isRefreshing -> {
                mAdapter.setNewData(data)
                view_content.isRefreshing = false
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