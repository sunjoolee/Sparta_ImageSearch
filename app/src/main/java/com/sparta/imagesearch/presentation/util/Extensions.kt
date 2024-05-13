package com.sparta.imagesearch.presentation.util

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.formatDate(): String = LocalDateTime
    .parse(this, DateTimeFormatter.ISO_DATE_TIME)
    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

fun String.hexToColor() : Color = Color(android.graphics.Color.parseColor(this))