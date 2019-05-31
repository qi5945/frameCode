package com.xx.baseuilibrary

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.xx.baseutilslibrary.status_bar.StatusBarUtils

/**
 * BaseActivity
 * (。・∀・)ノ
 * Describe：封装AppCompatActivity一级基类
 * Created by 雷小星🍀 on 2017/10/30 15:21.
 */

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mContext: Context

    /**
     * 获取布局资源文件id
     *
     * @return 布局资源文件id
     */
    protected abstract fun getActivityLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //强制竖屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mContext = this
        beforeSetContentView()
        setContentView(getActivityLayoutId())
        afterSetContentView()
        window.setBackgroundDrawableResource(R.color.colorWhite)
        StatusBarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        initGaoDeMapView(savedInstanceState)
    }

    /**
     * 初始化高德地图显示
     * @param savedInstanceState
     */
    open fun initGaoDeMapView(savedInstanceState: Bundle?) {}

    /**
     * 在设置ContenView之前执行的操作
     * 需要时复写
     */
    open fun beforeSetContentView() {}


    /**
     * 在设置ContenView之后执行的操作
     * 需要时复写
     */
    open fun afterSetContentView() {
        initView()
        initData()
        initEvent()
    }

    /**
     * 初始化控件及设置
     */
    open fun initView() {}

    /**
     * 初始化数据状态
     */
    protected abstract fun initData()


    /**
     * 初始化事件
     */
    protected abstract fun initEvent()

}
