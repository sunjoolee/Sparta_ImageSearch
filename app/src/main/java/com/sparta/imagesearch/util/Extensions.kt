package com.sparta.imagesearch.util

import android.content.res.Resources
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Float.fromDpToPx():Int = (this * Resources.getSystem().displayMetrics.density).toInt()
fun String.formatDate(): String = LocalDateTime
    .parse(this, DateTimeFormatter.ISO_DATE_TIME)
    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))