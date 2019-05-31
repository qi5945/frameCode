package com.xx.baseutilslibrary.common

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by DarkHorse on 2018/2/6.
 */

object SPManager {

    /**
     * 保存在手机的文件名称，路径：Path = /data/data/<package name>/shared_prefs
    </package> */
    lateinit var mSharedPreferences: SharedPreferences

    fun init(context: Context, preferencesName: String) {
        mSharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    /**
     * 保存数据的方法，根据传入数据参数的类型使用不同的保存方法
     * @param key
     * @param valueObject
     */
    fun put(key: String, valueObject: Any) {
        try {
            val editor = mSharedPreferences.edit()
            if (valueObject is String) {
                editor.putString(key, valueObject)
            } else if (valueObject is Int) {
                editor.putInt(key, valueObject)
            } else if (valueObject is Boolean) {
                editor.putBoolean(key, valueObject)
            } else if (valueObject is Float) {
                editor.putFloat(key, valueObject)
            } else if (valueObject is Long) {
                editor.putLong(key, valueObject)
            } else {
                val gson = Gson()
                val json = gson.toJson(valueObject)
                editor.putString(key, json)
            }
            //        SharedPreferencesCompat.apply(editor);
            editor.apply()
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * 获取存储在SharedPreferences里的数据，根据默认数据类型调用不同的方法获取数据
     * @param key
     * @param defaultObject
     * @return
     */
    operator fun get(key: String, defaultObject: Any?): Any? {
        if (defaultObject == null) {
            return null
        }

        try {
            if (defaultObject is String) {
                return mSharedPreferences.getString(key, defaultObject as String?)
            } else if (defaultObject is Int) {
                return mSharedPreferences.getInt(key, (defaultObject as Int?)!!)
            } else if (defaultObject is Boolean) {
                return mSharedPreferences.getBoolean(key, (defaultObject as Boolean?)!!)
            } else if (defaultObject is Float) {
                return mSharedPreferences.getFloat(key, (defaultObject as Float?)!!)
            } else if (defaultObject is Long) {
                return mSharedPreferences.getLong(key, (defaultObject as Long?)!!)
            } else if (defaultObject is List<*>) {
                val json = mSharedPreferences.getString(key, "")
                if ("" != json) {
                    val gson = Gson()
                    return gson.fromJson<Any>(json, object : TypeToken<List<*>>() {

                    }.type)
                }
            } else if (defaultObject is Set<*>) {
                val json = mSharedPreferences.getString(key, "")
                if ("" != json) {
                    val gson = Gson()
                    return gson.fromJson<Any>(json, object : TypeToken<Set<*>>() {

                    }.type)
                }
            } else {
                val json = mSharedPreferences.getString(key, "")
                if ("" != json) {
                    val gson = Gson()
                    return gson.fromJson<Any>(json, defaultObject::class.java)
                }
            }
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }

        return defaultObject
    }

    /**
     * 查询SharedPreferences是否含有某个key值

     * @param key
     * @return
     */
    fun contains(key: String): Boolean {
        var isContains = false
        try {

            isContains = mSharedPreferences.contains(key)
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
        return isContains
    }

    /**
     * 获取SharedPreferences里所有的键值对
     * @return
     */
    fun getAll(): Map<String, *>? {
        return mSharedPreferences.all
    }

    /**
     * 移除某个key值对
     *
     * @param context
     * @param key
     */
    fun remove(key: String) {
        val editor = mSharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    /**
     * 清空SharedPreferences里的所有数据
     *
     * @param context
     */
    fun clear() {
        val editor = mSharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
