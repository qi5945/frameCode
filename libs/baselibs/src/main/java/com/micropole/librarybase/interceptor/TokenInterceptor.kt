package com.micropole.librarybase.interceptor

import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.micropole.librarybase.api.ApiService
import com.micropole.librarybase.common.SPConstants
import com.xx.baseutilslibrary.common.SPManager
import com.xx.baseutilslibrary.entity.BaseResponseStatusEntity
import com.xx.baseutilslibrary.network.exception.APILoginException
import com.xx.baseutilslibrary.network.retrofit.Retrofit2Manager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset

/**
 * 刷新token
 */
class TokenInterceptor : Interceptor {
    private val TAG = "TokenInterceptor"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        //根据和服务端的约定判断token过期
        if (isTokenExpired(response)) {
            Log.d(TAG,"自动刷新Token,然后重新请求数据")
            SPManager.remove(SPConstants.SHORT_TOKEN)
            //同步请求方式，获取最新的Token
            val newToken = getNewToken()
            SPManager.put(SPConstants.SHORT_TOKEN, newToken)
            if (TextUtils.isEmpty(newToken)) {
                SPConstants.logout()
                throw APILoginException(444,"请重新登录")
            }
            //使用新的Token，创建新的请求
            val newRequest = chain.request()
                    .newBuilder()
                    .header("token", newToken)
                    .build()
            //重新请求
            return chain.proceed(newRequest)
        }
        return response
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private fun isTokenExpired(response: Response): Boolean {
        try {
            if (response.isSuccessful) {
                val responseBody = response.body()
                val source = responseBody!!.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer()
                var charset :Charset = Charset.forName("UTF-8")
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(Charset.forName("UTF-8"))!!
                }
                val bodyString = buffer.clone().readString(charset)
                val httpResult = Gson().fromJson(bodyString, BaseResponseStatusEntity::class.java)
                return when(httpResult.status){
                    "0" -> {
                        when (httpResult.code) {
                            "333" -> true
                            "444", "222" -> {
                                SPConstants.logout()
                                throw APILoginException(444, "请重新登录")
                            }
                            else -> false
                        }
                    }
                    else -> false
                }
            }
        } catch (e : IOException ) {
            e.printStackTrace()
        }
        return true
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */
    @Throws(IOException::class)
    private fun getNewToken(): String {
        // 通过获取token的接口，同步请求接口
        val longToken = SPManager[SPConstants.LONG_TOKEN, ""].toString().trim()
        val response = Retrofit2Manager.instance.createApi(ApiService::class.java).refreshToken(longToken).execute()
        return response.body()!!.data!!.short_token
    }
}