package com.sparta.imagesearch.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView


@Composable
fun ImageSearchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if(darkTheme) ImageSearchColorScheme.Main.darkScheme
        else ImageSearchColorScheme.Main.lightScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.material.primary.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme.material,
        typography = AppTypography,
        content = content
    )
}

