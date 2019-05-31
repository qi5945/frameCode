package com.micropole.librarybase.common

import com.micropole.librarybase.api.LoginEntity
import com.xx.baseutilslibrary.common.SPManager
import org.greenrobot.eventbus.EventBus

/**
 * Constants
 * 沉迷学习不能自拔
 * Describe：
 * Created by 雷小星🍀 on 2018/2/26 10:55.
 */

object SPConstants {
    const val LONG_TOKEN = "long_token"//缓存的长TOKEN
    const val SHORT_TOKEN = "short_token"//缓存的短TOKEN
    const val HX_USER_NAME = "hx_user_name"//环信用户名
    const val USER_NAME = "nickname"//APP用户名
    const val USER_ID = "user_id"//用户id
    const val HEAD_IMG = "icon"//用户头像
    const val PHONE = "phone"//电话号码
    const val PWD = "pwd"//密码
    private const val SEARCH_HISTORY = "search_history" //搜索历史

    //这两个适用于请求接口时的头key
    const val HEARD_LONG_TOKEN = "token"
    const val HEARD_SHORT_TOKEN = "token"

    /**
     * 获取搜索历史
     */
    fun getSearchHistory():String{
        return SPManager[SEARCH_HISTORY, ""] as String
    }

    /**
     * 清空搜索历史
     */
    fun clearSearchHistory(){
        return SPManager.remove(SEARCH_HISTORY)
    }

    /**
     * 设置搜索历史
     */
    fun setSearchHistory(history :String){
        SPManager.put(SEARCH_HISTORY,history)
    }

    /**
     * 退出登录
     */
    fun logout(){
        SPManager.remove(LONG_TOKEN)
        SPManager.remove(SHORT_TOKEN)
        SPManager.remove(USER_ID)
        EventBus.getDefault().post(LoginEntity())
    }

    /**
     * 移除账号密码
     */
    fun removePwd(){
        SPManager.remove(PHONE)
        SPManager.remove(PWD)
    }

    /**
     * 记住账号密码
     */
    fun rememberPwd(phone : String,pwd : String){
        SPManager.put(PHONE,phone)
        SPManager.put(PWD,pwd)
    }

    /**
     * 是否登录
     */
    fun isLogin():Boolean{
        if(!(SPManager[LONG_TOKEN, ""] as String).isEmpty() &&
                !(SPManager[SHORT_TOKEN, ""] as String).isEmpty()){
            return true
        }
        return false
    }

}


