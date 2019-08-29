package com.yumetsuki.yuzusoftappwidget.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.yumetsuki.yuzusoftappwidget.App
import com.yumetsuki.yuzusoftappwidget.AppContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * SharedPreferences委托，用于简化本地存储操作
 * @author 原作者benny_huo
 * */
class Preference<T>(
    context: Context,
    private val name: String,
    private val default: T,
    prefName: String = "default"
) :ReadWriteProperty<Any?, T>{

    private val prefs by lazy {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(findProperName(property))
    }


    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(findProperName(property), value)
    }

    /**
     * 默认通过反射获取名称
     * */
    private fun findProperName(property: KProperty<*>) = if(name.isEmpty()) property.name else name

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    private fun findPreference(key: String): T{
        return when(default){
            is Long -> prefs.getLong(key, default)
            is Int -> prefs.getInt(key, default)
            is Boolean -> prefs.getBoolean(key, default)
            is String -> prefs.getString(key, default)
            else -> throw IllegalArgumentException("Unsupported type.")
        } as T
    }

    @SuppressLint("CommitPrefEdits")
    private fun putPreference(key: String, value: T){
        prefs.edit().apply {
            when(value){
                is Long -> putLong(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
        }.apply()
    }

}

/**
 * 扩展函数简化委托使用
 * */
inline fun <reified R, T> R.pref(default: T) = Preference(AppContext, "", default, R::class.java.simpleName)

fun Context.applicationPref(): SharedPreferences = getSharedPreferences("application", Context.MODE_PRIVATE)