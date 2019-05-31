package com.micropole.librarybase

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.micropole.librarybase.interceptor.EpSignInterceptor
import com.micropole.librarybase.interceptor.TokenInterceptor
import com.xx.baseutilslibrary.network.exception.NullProviderException
import com.xx.baseutilslibrary.network.gson.XxGsonConverterFactory
import com.xx.baseutilslibrary.network.retrofit.Retrofit2Manager
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @ClassName       AppApi
 * @Model           todo
 * @Description     网络访问工具
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2018/10/11 10:08
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
class AppApi {
    companion object {
        private var appApi: AppApi? = null

        val instance: AppApi
            get() {
                if (appApi == null)
                    appApi = AppApi()
                return appApi!!
            }
    }

    /**
     * 创建Api对象
     *
     * @param service Api接口
     * @param <T>     接口
     * @return Api对象
    </T> */
    @Throws(NullProviderException::class)
    open fun <T> createApi(service: Class<T>): T {
        return Retrofit2Manager.instance.createApi(service)
    }

    init {
        initData()
    }

    private fun initData() {
        val okHttpClient = Retrofit2Manager
                .instance
                .okHttpClient!!
                .newBuilder()
                .addInterceptor(EpSignInterceptor())
                .addInterceptor(TokenInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))//添加网络日志
                .build()

        Retrofit2Manager
                .instance
                .okHttpClient = okHttpClient//设置新配置的httpClient

        //设置新的Retrofit
        val retrofit = Retrofit.Builder()
                .baseUrl(Retrofit2Manager.instance.apiConfigProvider!!.apiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(XxGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        Retrofit2Manager.instance.retrofit = retrofit
    }
}