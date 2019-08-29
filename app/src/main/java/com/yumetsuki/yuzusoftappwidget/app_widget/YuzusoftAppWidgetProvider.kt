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
import kotlin.random.Random


class YuzusoftAppWidgetProvider: AppWidgetProvider() {

    private var player: MediaPlayer? = null

    private lateinit var remoteViews: RemoteViews

    private var characters = Wife.values().groupBy { wife -> wife.wifeName }.mapValues {
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

    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        //创建远程视图
        remoteViews = RemoteViews(context.packageName, R.layout.app_widget_layout)


        //注册广播
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
     * 每删除一次窗口小部件就调用一次
     */
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    /**
     * 当小部件大小改变时
     */
    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    override fun onRestored(context: Context, oldWidgetIds: IntArray, newWidgetIds: IntArray) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }

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