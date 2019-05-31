package com.micropole.chebianjie.test

import com.xx.baseuilibrary.mvp.presenter.BaseRxMvpPresenter
import com.xx.baseutilslibrary.network.rx.RxHttpObserver

/**
 * @ClassName       HomePostPresenter
 * @Model           todo
 * @Description     首页帖子
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2018/10/15 14:00
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
class HomePostPresenter : BaseRxMvpPresenter<HomePostContract.Model, HomePostContract.View>(), HomePostContract.Presenter {
    override fun loadData(isRefresh: Boolean, page: Int) {
        val map = HashMap<String,Any>()
        map["cate_id"] = getView()?.classifyId!!
        map["page"] = page.toString()
        map["pnum"] = "10"
        map["sort"] = getView()?.sort!!
        map["order"] = getView()?.order!!
        getModel().loadData(map)
                .subscribe(object : RxHttpObserver<List<PostEntity>>() {
                    override fun onCompleted(msg: String?, entity: List<PostEntity>?) {
                        getView()?.setData(entity)
                    }

                    override fun onError(error: String?, code: String?) {
                        getView()?.showToast(error)
                        getView()?.showError(Throwable(error), isRefresh)
                    }

                    override fun onLoadingStart() {
                        attachObserver(this)
                    }

                    override fun onLoadingFinish() {
                    }

                })
    }

    override fun createModel(): HomePostContract.Model = HomePostModel()
}