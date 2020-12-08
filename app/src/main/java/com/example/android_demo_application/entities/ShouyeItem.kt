package com.example.android_demo_application.entities

import java.text.SimpleDateFormat

class ShouyeItem(val articleId:String?,
        val author: String?,
                 publishTimeStamp: String?,
                 val title: String?,
                 val content: String?,
                 val superChapterName: String?,
                 val link: String?) {
    val publishTime: String? = if (publishTimeStamp == null) null else SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(publishTimeStamp.toLong())
}