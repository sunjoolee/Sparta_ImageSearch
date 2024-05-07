package com.sparta.imagesearch.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.formatDate(): String = LocalDateTime
    .parse(this, DateTimeFormatter.ISO_DATE_TIME)
    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
