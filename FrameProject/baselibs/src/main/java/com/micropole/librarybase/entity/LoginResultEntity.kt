package com.micropole.librarybase.entity

import com.google.gson.annotations.SerializedName
import com.xx.baseutilslibrary.entity.BaseGsonEntity


/**
 * LoginResultEntity
 * 沉迷学习不能自拔
 * Describe：
 * Created by 雷小星🍀 on 2018/7/31 18:20.
 */
data class LoginResultEntity(
        @SerializedName("sign_login") var signLogin: String = "",
        @SerializedName("sign_api") var signApi: String = ""
) : BaseGsonEntity() {
}