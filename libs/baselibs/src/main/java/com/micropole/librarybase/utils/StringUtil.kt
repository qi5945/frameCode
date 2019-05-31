package com.micropole.librarybase.utils

import android.text.TextUtils
import com.xx.baseutilslibrary.network.retrofit.Retrofit2Manager

/**
 * @ClassName       StringUtil
 * @Model           todo
 * @Description     todo
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2018/11/1 9:31
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
object StringUtil {

    /**
     * 处理逗号分隔的字符串
     * string 转 list
     */
    fun strToList(string: String): ArrayList<String> {
        val list = ArrayList<String>()
        if (TextUtils.isEmpty(string))
            return list
        if (string.contains(","))
            return string.split(",") as ArrayList<String>
        else
            list.add(string)
        return list
    }

    /**
     * 处理逗号分隔的字符串(并添加图片路径前缀)
     * string 转 list
     */
    fun strToListAndHost(string: String): ArrayList<String> {
        var list = ArrayList<String>()
        if (TextUtils.isEmpty(string))
            return list
        if (string.contains(",")) {
            list = string.split(",") as ArrayList<String>
            for (i in 0 until list.size)
                list[i] = Retrofit2Manager.instance.apiConfigProvider!!.releaseHost + list[i]
            return list
        } else
            list.add(Retrofit2Manager.instance.apiConfigProvider!!.releaseHost + string)
        return list
    }

    /**
     * 处理逗号分隔的字符串
     * list 转 String
     */
    fun listToString(list: ArrayList<String>): String {
        if (list == null || list.size == 0)
            return ""
        val sb = StringBuilder()
        for (i in 0 until list.size) {
            sb.append(list[i]).append(",")
        }
        return sb.toString().substring(0, sb.toString().length - 1)
    }
}