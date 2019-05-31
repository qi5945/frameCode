package com.micropole.chebianjie.test

import com.micropole.librarybase.AppApi
import com.xx.baseutilslibrary.entity.BaseResponseEntity
import com.xx.baseutilslibrary.network.rx.RxHelper
import io.reactivex.Observable
import java.util.*

/**
 * @ClassName       HomePostModel
 * @Model           todo
 * @Description     首页帖子
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2018/10/15 14:00
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
class HomePostModel : HomePostContract.Model {
    override fun loadData(map: HashMap<String, Any>?): Observable<BaseResponseEntity<List<PostEntity>>> {
        return AppApi.instance.createApi(Api::class.java).getClassifyPost(map!!).compose(RxHelper.io_main())
    }
}