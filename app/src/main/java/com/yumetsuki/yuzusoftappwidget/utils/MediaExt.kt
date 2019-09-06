package com.yumetsuki.yuzusoftappwidget.utils

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*


fun Context.playMediaAsync(resId: Int): Deferred<MediaPlayer> {

    val result = CompletableDeferred<MediaPlayer>()

    MediaPlayer.create(this@playMediaAsync, resId).apply {

        setOnCompletionListener {
            it.stop()
            it.release()
            result.complete(it)
        }

    }.start()

    return result

}

fun Context.playMediaSequenceAsync(vararg resIds: Int): Flow<MediaPlayer> {
    return resIds.asFlow().map { playMediaAsync(it).await() }
}

fun Context.playMediaSequenceAsync(builder: suspend SequenceScope<Int>.() -> Unit): Flow<MediaPlayer> {
    return sequence(builder).asFlow().map {
        playMediaAsync(it).await()
    }
}