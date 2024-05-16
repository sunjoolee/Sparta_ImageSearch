package com.sparta.imagesearch.presentation.util.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.sparta.imagesearch.presentation.theme.ImageSearchColorScheme

data class MyButtonColorSet(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

data class MyButtonColors(
    val primaryButton: MyButtonColorSet = MyButtonColorSet(
        containerColor = ImageSearchColorScheme.Main.lightScheme.primary,
        contentColor = ImageSearchColorScheme.Main.lightScheme.onPrimary,
        disabledContainerColor = ImageSearchColorScheme.Main.lightScheme.disabled,
        disabledContentColor = ImageSearchColorScheme.Main.lightScheme.onDisabled
    ),
    val tertiaryButton: MyButtonColorSet = MyButtonColorSet(
        containerColor = ImageSearchColorScheme.Main.lightScheme.tertiary,
        contentColor = ImageSearchColorScheme.Main.lightScheme.onTertiary,
        disabledContainerColor = ImageSearchColorScheme.Main.lightScheme.disabled,
        disabledContentColor = ImageSearchColorScheme.Main.lightScheme.onDisabled
    )
)

val LocalButtonColors = staticCompositionLocalOf { MyButtonColors() }