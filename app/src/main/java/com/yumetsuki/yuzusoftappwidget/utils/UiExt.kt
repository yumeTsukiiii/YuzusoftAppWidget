package com.yumetsuki.yuzusoftappwidget.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun Context.alert(builder: AlertDialog.Builder.() -> Unit) {
    AlertDialog.Builder(this).apply {
        builder()
    }.show()
}


