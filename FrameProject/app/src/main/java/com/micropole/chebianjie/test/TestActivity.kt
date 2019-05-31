package com.micropole.chebianjie.test

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xx.baseuilibrary.mvp.lcec.BaseMvpLeceListActivity
import java.lang.StringBuilder

/**
 * @ClassName       TestActivity
 * @Model           todo
 * @Description     todo
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2019/4/1 18:59
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
class TestActivity :
    BaseMvpLeceListActivity<PostEntity, HomePostContract.Model, HomePostContract.View, HomePostPresenter>(),
    HomePostContract.View {
    override fun loadListData(isRefresh: Boolean) {
        presenter.loadData(isRefresh, mPage)
    }

    override fun isDefault(): Boolean = true

    override fun getSort(): String = ""

    override fun getOrder(): String = ""

    override fun getClassifyId(): String = "1"

    override fun createAdapter(): BaseQuickAdapter<PostEntity, BaseViewHolder> = PostAdapter(arrayListOf())

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(mContext)

    override fun openLoadMore(): Boolean = true

    override fun createPresenter(): HomePostPresenter = HomePostPresenter()

    override fun initData() {
        super.initData()
        loadData(true)
        Log.e("TAG",showNo("12547889962156325"))
        Log.e("TAG",showNo("125"))
        Log.e("TAG",showNo("1258"))
    }

    private fun showNo(no: String): String {
        if (TextUtils.isEmpty(no))
            return ""
        return if (no.length > 4) {
            val sb = StringBuilder()
            val count = no.length / 4
            val y = no.length % 4
            for (i in 1 until count + 1) {
                sb.append(no.substring((i - 1) * 4, i * 4)).append(" ")
            }
            sb.append(no.substring(no.length - y, no.length))
            sb.toString()
        } else
            no
    }
}