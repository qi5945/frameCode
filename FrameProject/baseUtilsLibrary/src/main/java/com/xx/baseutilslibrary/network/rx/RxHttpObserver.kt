package com.xx.baseutilslibrary.network.rx


import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.Utils
import com.xx.baseutilslibrary.common.SPManager
import com.xx.baseutilslibrary.entity.BaseResponseEntity
import com.xx.baseutilslibrary.entity.BaseResponseStatusEntity
import com.xx.baseutilslibrary.network.exception.APILoginException
import com.xx.baseutilslibrary.network.exception.ApiFaileException
import io.reactivex.observers.DefaultObserver
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * XxBaseHttpObserver
 * (๑• . •๑)
 * 类描述: RxJava的Observer封装
 * Created by 雷小星🍀 on 2017/6/2 11:01
 */

abstract class RxHttpObserver<T> : DefaultObserver<BaseResponseEntity<T>>(), RxHelper.OnLoadingStatusListener {

    override fun onStart() {
        onLoadingStart()
    }

    fun cannel() {
        cancel()
        onLoadingFinish()
    }

    override fun onComplete() {
        onLoadingFinish()
    }

    abstract fun onCompleted(msg: String?, entity: T?)

    abstract fun onError(error: String?, code: String? = "")

    override fun onError(throwable: Throwable) {
        onLoadingFinish()
        throwable.printStackTrace()
        if (throwable is HttpException) {
            val code = throwable.code()
            if (code == 500 || code == 404) {
                onError("服务器错误,请稍后重试")
            }
        } else if (throwable is ConnectException) {
            //断开网络
            onError("与网络断开连接")
        } else if (throwable is UnknownHostException) {
            //没有联网
            onError("当前没有可用网络")
        } else if (throwable is SocketTimeoutException) {
            onError("连接服务器超时,请稍后重试")
        } else if (throwable is ApiFaileException) {
            onError(throwable.message,throwable.cause!!.message)
        } else if (throwable is APILoginException) {
            onError("请重新登录")
            val c = Class.forName("com.micropole.cloudapp.MainActivity")
            val i = Intent(Utils.getApp(), c)
            i.putExtra("isLogout", 1)
            ActivityUtils.finishAllActivities()
            ActivityUtils.startActivity(i)
        } else {
            onError("异常:" + throwable.message)
        }
    }


    override fun onNext(responseBean: BaseResponseEntity<T>) {
        if (responseBean.status == BaseResponseStatusEntity.SUCCESS) {
            //正常从接口获取到数据
            onCompleted(responseBean.msg, responseBean.data)
        } else {
            //接口返回的错误描述
            onError(responseBean.msg, responseBean.code)
        }
    }
}
