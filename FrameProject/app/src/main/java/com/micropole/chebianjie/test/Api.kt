package com.micropole.chebianjie.test

import com.xx.baseutilslibrary.entity.BaseResponseEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.*

/**
 * @ClassName       Api
 * @Model           todo
 * @Description     项目接口文件
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2018/12/19 15:19
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
interface Api {

    /**
     * 分类帖子
     *
     * @return
     */
    @GET("Article/article")
    fun getClassifyPost(@QueryMap map: HashMap<String, Any>): Observable<BaseResponseEntity<List<PostEntity>>>

}