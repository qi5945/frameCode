package com.xx.baseutilslibrary.extensions

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.blankj.utilcode.util.ActivityUtils

/**
 * author: Gubr
 * date: 2018/5/8
 * describe:
 */
fun Activity.startActivity(clazz: Class<*>){
    val intent = Intent(this, clazz)
    this.startActivity(intent)
}

fun toast(content: String, duration: Int=Toast.LENGTH_SHORT){
    Toast.makeText(ActivityUtils.getTopActivity(),content,duration).show()
}

