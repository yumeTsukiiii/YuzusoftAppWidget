package com.yumetsuki.yuzusoftappwidget.utils

import com.yumetsuki.yuzusoftappwidget.model.*
import com.yumetsuki.yuzusoftappwidget.repo.entity.*

fun AlarmSetting.toNoId() = NoIdAlarmSetting(icon, alarmHour, alarmMinute, isEnable)

fun AlarmSettingDays.toNoId() = NoIdAlarmSettingDays(day, settingId)

fun Story.toNoId() = NoIdStory(storyName)

fun StoryChapter.toNoId() = NoIdStoryChapter(storyId, chapterName)

fun StoryPage.toNoId() = NoIdStoryPage(chapterId, background, content, header)

fun StoryCharacter.toNoId() = NoIdStoryCharacter(pageId, indexInPageView, wifeName, clothesIndex, expressionIndex, scale, translateX, translateY)