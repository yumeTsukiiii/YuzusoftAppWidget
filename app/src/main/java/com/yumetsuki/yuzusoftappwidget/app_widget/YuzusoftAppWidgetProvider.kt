package com.yumetsuki.yuzusoftappwidget.app_widget

import android.appwidget.AppWidgetProvider
import android.os.Bundle
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.media.MediaPlayer
import android.widget.RemoteViews
import com.yumetsuki.yuzusoftappwidget.*
import com.yumetsuki.yuzusoftappwidget.config.Wife
import com.yumetsuki.yuzusoftappwidget.utils.applicationPref
import com.yumetsuki.yuzusoftappwidget.utils.toast
import kotlin.random.Random

/**
 * 老婆小组件，可以戳哦！可以戳哦！可以戳哦！
 * */
class YuzusoftAppWidgetProvider: AppWidgetProvider() {

    private var player: MediaPlayer? = null

    private lateinit var remoteViews: RemoteViews

    //TODO("遗留代码，待优化")
    private var characters = Wife.values().groupBy { wife -> wife.wifeName }.mapValues {
        it.value[0].res.find { wifeClothes ->
            wifeClothes.clothesName == AppContext.applicationPref()
                .getWifeClothesName(it.value[0].wifeName)
        }?.run {
            voiceMap to normalImage
        }?: error("no clothes")
    }

    /**
     * 每次窗口小部件被更新都调用一次该方法(被拖出来的时候)
     */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        //创建远程视图
        remoteViews = RemoteViews(context.packageName, R.layout.app_widget_layout)


        //注册点击事件
        val intent = Intent(CLICK_ACTION).apply {
            component = ComponentName(
                context,
                YuzusoftAppWidgetProvider::class.java
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            R.id.yuzusoft_charactor_image_view,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        remoteViews.setOnClickPendingIntent(
            R.id.yuzusoft_charactor_image_view,
            pendingIntent
        )

        remoteViews.setImageViewResource(R.id.yuzusoft_charactor_image_view,
            (characters[CharacterConfig.mostLikeWife] ?: error("no charactar")).second)

        //对所有该应用的小部件进行刷新
        for (appWidgetId in appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    /**
     * 接收窗口小部件点击时发送的广播
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when(intent.action) {
            CLICK_ACTION -> {
                onWidgetClick(context)
            }
            UPDATE_ACTION -> {
                onUpdateWife(context)
            }
        }

    }

    /**
     * 点击老婆时，随机产生反应
     * */
    private fun onWidgetClick(context: Context) {
        if (!Status.isCharacterPlaying) {

            Status.isCharacterPlaying = true

            characters[CharacterConfig.mostLikeWife]?.also { wifeEnum ->
                wifeEnum.first[Random.nextInt(0, wifeEnum.first.size)].apply {

                    updateRemoteViewImage(context, first)

                    player = MediaPlayer.create(context, second)

                    player?.setOnCompletionListener {
                        updateRemoteViewImage(context, wifeEnum.second)
                        Status.isCharacterPlaying = false
                        player?.stop()
                        player?.release()
                        player = null
                    }

                    player?.start()
                }
            }
        }

    }

    /**
     * 更换角色时触发
     * */
    private fun onUpdateWife(context: Context) {

        characters = Wife.values().groupBy { wife -> wife.wifeName }.mapValues {
            it.value[0].res.find { wifeClothes ->
                wifeClothes.clothesName == AppContext.applicationPref()
                    .getString(
                        "${it.value[0].wifeName}${CharacterConfig.clothesSufix}",
                        it.value[0].res[0].clothesName
                    )
            }?.run {
                voiceMap to normalImage
            }?: error("no clothes")
        }

        updateRemoteViewImage(context,
            (characters[CharacterConfig.mostLikeWife] ?: error("2333")).second
        )
    }

    /**
     * 更新角色立绘
     * */
    private fun updateRemoteViewImage(context: Context, resourceId: Int) {

        if (!this@YuzusoftAppWidgetProvider::remoteViews.isInitialized) {
            remoteViews = RemoteViews(context.packageName, R.layout.app_widget_layout)
        }

        remoteViews.setImageViewResource(
            R.id.yuzusoft_charactor_image_view,
            resourceId
        )

        val componentName = ComponentName(context, YuzusoftAppWidgetProvider::class.java)
        AppWidgetManager.getInstance(context)
            .updateAppWidget(componentName, remoteViews)
    }

    companion object {
        const val CLICK_ACTION = "com.yumetsuki.yuzusoftappwidget.action.CLICK" // 点击事件的广播ACTION
        const val UPDATE_ACTION = "com.yumetsuki.yuzusoftappwidget.action.UPDATE" // 点击事件的广播ACTION
    }
}