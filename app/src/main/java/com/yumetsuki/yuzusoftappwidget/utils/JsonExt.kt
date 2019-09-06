package com.yumetsuki.yuzusoftappwidget.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

val moshi = Moshi.Builder()
    // ... add your own JsonAdapters and factories ...
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * 将任意对象转换为Json字符串
 * */
inline fun <reified T> T.toJson(): String {
    return toJson(T::class.java)
}

fun <T> T.toJson(clazz: Class<T>): String {
    if (this is String) {
        return this
    }
    return moshi.adapter(clazz).toJson(this)
}

/**
 * 将Json字符串转换为任意对象
 * */
inline fun <reified T> String.fromJson(): T? {
    return fromJson(T::class.java)
}

fun <T> String.fromJson(clazz: Class<T>): T? {
    return moshi.adapter(clazz).fromJson(this)
}