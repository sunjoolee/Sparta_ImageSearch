package com.sparta.imagesearch.util

import android.content.res.Resources

fun Float.fromDpToPx():Int = (this * Resources.getSystem().displayMetrics.density).toInt()