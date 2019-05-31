package com.micropole.chebianjie

import android.support.multidex.MultiDexApplication
import com.blankj.utilcode.util.Utils
import com.mob.MobSDK
import com.xx.anypay.WxAppIDProvider
import com.xx.anypay.XxAnyPay
import com.xx.baseutilslibrary.common.SPManager
import com.xx.baseutilslibrary.network.provider.XxApiConfigProvider
import com.xx.baseutilslibrary.network.retrofit.Retrofit2Manager
import com.xx.baseutilslibrary.status_bar.StatusBarUtils

/**
 * @ClassName       AppApplication
 * @Model           todo
 * @Description     todo
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2018/10/11 8:47
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
class AppApplication : MultiDexApplication() {

    companion object {
        var statusBarHeight: Int = 0
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

        //Mob
//        MobSDK.init(this)

        //bugly
//        CrashReport.initCrashReport(applicationContext, "fe781b689c", false)

        //初始化SP管理器
        SPManager.init(this, "chebianjie")

        //获取状态栏高度
        statusBarHeight = StatusBarUtils.getStatusBarHeight(this)

        //自定义支付SDK设置
//        XxAnyPay.getIntance().init(this)
//        XxAnyPay.getIntance().wxAppIDProvider = WxAppIDProvider { "wx658e61e6397edf43" }

        Retrofit2Manager.instance.apiConfigProvider = object : XxApiConfigProvider {

            override fun getApiRelativePath(): String = "/api/"

            override fun getDebugHost(): String = "http://caipiao.goodbooy.cn"

            override fun getReleaseHost(): String = "http://caipiao.goodbooy.cn"

            override fun isDebug(): Boolean = BuildConfig.DEBUG
        }
    }
}