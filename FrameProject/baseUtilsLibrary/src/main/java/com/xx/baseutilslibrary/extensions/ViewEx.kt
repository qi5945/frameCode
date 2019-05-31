package com.xx.baseutilslibrary.extensions

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.xx.baseutilslibrary.network.retrofit.Retrofit2Manager
/**
 * @ClassName       Ex
 * @Model           todo
 * @Description     扩展方法
 * @Author          chen qi hao
 * @Sign            沉迷学习不能自拔
 * @Date            2018/12/19 16:32
 * @Email           371232886@qq.com
 * @Copyright       Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */


/**
 * textView 设置富文本
 */
fun TextView.setHtmlText(content: String) {
    this.text = Html.fromHtml(content)
}

/**
 * 动态设置view的宽高
 */
fun View.setWidthHeight(w: Int, h: Int) {
    val lp = this.layoutParams
    lp.height = h
    lp.width = w
    this.layoutParams = lp
}





