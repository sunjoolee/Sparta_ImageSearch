package com.sparta.imagesearch.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalColors = staticCompositionLocalOf {
    ImageSearchColorScheme.Main.lightScheme
}

val MaterialTheme.scheme: ImageSearchColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current