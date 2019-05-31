package com.xx.baseutilslibrary.network.exception

import java.io.IOException

/**
 * ApiFaileException
 * (。・∀・)ノ
 * Describe：API错误信息
 * Created by 雷小星🍀 on 2017/8/22 11:02.
 */

class ApiFaileException(code : String,message: String) : IOException(code,Throwable(message))
