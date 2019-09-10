package com.yumetsuki.yuzusoftappwidget.app_widget

import android.appwidget.AppWidgetProvider
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
import com.yumetsuki.yuzusoftappwidget.utils.playMediaAsync
import com.yumetsuki.yuzusoftappwidget.utils.toast
import kotlinx.coroutines.*
import java.util.concurrent.FutureTask
import kotlin.random.Random

/**
 * 老婆小组件，可以戳哦！可以戳哦！可以戳哦！
 * */
class YuzusoftAppWidgetProvider: AppWidgetProvider() {

    private lateinit var remoteViews: RemoteViews

    /**
     * 每次窗口小部件被更新都调用一次该方法(被拖出来的时候)
     */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        //创建远程视图
        remoteViews = RemoteViews(context.packageName, R.layout.app_widget_layout)

        remoteViews

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
            CharacterConfig.getMostLikeWifeWithClothes().second.normalImage)

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

            CharacterConfig.getMostLikeWifeWithClothes().also { (_, clothes) ->

                val randomReaction = clothes.voiceMap[
                        Random.nextInt(0, clothes.voiceMap.size)
                ]

                updateRemoteViewImage(context, randomReaction.first)

                GlobalScope.launch {

                    context.playMediaAsync(
                        randomReaction.second
                    ).await()

                    Status.isCharacterPlaying = false
                    updateRemoteViewImage(context, clothes.normalImage)
                }
            }

        }

    }

    /**
     * 更换角色时触发
     * */
    private fun onUpdateWife(context: Context) {

        updateRemoteViewImage(
            context,
            CharacterConfig.getMostLikeWifeWithClothes().second.normalImage
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