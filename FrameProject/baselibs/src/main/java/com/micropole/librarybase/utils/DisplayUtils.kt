package com.micropole.librarybase.utils

import android.content.Context

object DisplayUtils {
    fun px2dp(context : Context, pxValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return pxValue / scale + 0.5f
    }

    fun dp2px(context : Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    fun px2dp(context : Context, pxValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5).toInt()
    }

    fun dp2px(context : Context, dpValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5).toInt()
    }
}
