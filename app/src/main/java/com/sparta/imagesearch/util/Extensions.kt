package com.sparta.imagesearch.util

import android.content.res.Resources
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getCurrentDateTime():String = LocalDateTime.now()
    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

fun String.formatDate(): String = LocalDateTime
    .parse(this, DateTimeFormatter.ISO_DATE_TIME)
    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))