package com.micropole.librarybase.api

import com.micropole.librarybase.common.SPConstants
import com.xx.baseutilslibrary.entity.BaseResponseEntity
import retrofit2.Call
import retrofit2.http.*

/**
 * api接口
 */
interface ApiService {
    //刷新短token
    @POST("login/get_short_token")
    fun refreshToken(@Header(SPConstants.HEARD_LONG_TOKEN) longToken: String): Call<BaseResponseEntity<LoginEntity>>
}