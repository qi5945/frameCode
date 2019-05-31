package com.micropole.librarybase.interceptor

import com.micropole.librarybase.common.SPConstants
import com.xx.baseutilslibrary.common.SPManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * EpSignInterceptor
 * 沉迷学习不能自拔
 * Describe： Sign拦截器
 * Created by 雷小星🍀 on 2018/7/31 18:45.
 */
class EpSignInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
                .newBuilder()
                .addHeader(SPConstants.HEARD_SHORT_TOKEN, SPManager[SPConstants.SHORT_TOKEN, ""] as String)
//                .addHeader("apisign", SPConstants.loginResult!!.signApi)
                .build()

        return chain.proceed(request)
    }
}
