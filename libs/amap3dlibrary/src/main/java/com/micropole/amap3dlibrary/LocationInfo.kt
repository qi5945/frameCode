package com.micropole.amap3dlibrary

import android.content.Context

/**
 * @ClassName       LocationInfo
 * @Model           todo
 * @Description     定位的信息
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2019/4/2 11:56
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
object LocationInfo {
    private val spName = "location_info"
    //纬度
    const val LATITUDE = "LATITUDE"
    //经度
    const val LONGITUDE = "LONGITUDE"

    const val PROVINCE = "PROVINCE"
    const val CITY = "CITY"
    const val AREA = "AREA"
    const val ADDRESS = "ADDRESS"

    const val IS_LOCATION_SUCCESS = "IS_LOCATION_SUCCESS"

    /**
     * 保存定位的信息
     * @param lat 纬度
     * @param lon 经度
     * @param province 省份
     * @param city 城市
     * @param area 区县
     * @param ads 详细地址
     */
     fun putLocationInfo(
        context: Context,
        lat: Double,
        lon: Double,
        province: String,
        city: String,
        area: String,
        ads: String
    ) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit()
            .putString(LATITUDE, lat.toString())
            .putString(LONGITUDE, lon.toString())
            .putString(PROVINCE, province)
            .putString(CITY, city)
            .putString(AREA, area)
            .putString(ADDRESS, ads)
            .apply()
    }

    /**
     * 设置定位的状态
     */
    fun setLocationStatus(context: Context, isSuccess: Boolean) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit()
            .putBoolean(IS_LOCATION_SUCCESS, isSuccess)
            .apply()
    }

    /**
     * 获取定位的状态
     */
    fun getLocationStatus(context: Context): Boolean {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getBoolean(IS_LOCATION_SUCCESS, false)
    }

    /**
     * 根据Key获取定位信息，key在上面已经定义
     */
    fun getLocationInfoWithKey(context: Context, key: String): String? {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getString(key, "")
    }
}