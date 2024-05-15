package com.sparta.imagesearch.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val primary = Color(0xFF14293E)
val onPrimary = Color(0xFFFFFFFF)
val tertiary = Color(0xFFF95B19)
val onTertiary = Color(0xFF14293E)
val background = Color(0xFF1F334B)
val onBackground = Color(0xFFFFFFFF)
val surface = Color(0xFF14293E)
val onSurface = Color(0xFFFFFFFF)

val disabled = Color.LightGray
val onDisabled = Color.DarkGray
val dropdown = Color.Gray
val onDropDown = Color.White

data class ImageSearchColors(
    val material: ColorScheme,
    val disabled: Color,
    val onDisabled: Color,
    val dropdown: Color,
    val onDropDown: Color
) {
    val primary get() = material.primary
    val onPrimary get() = material.onPrimary
    val tertiary get() = material.tertiary
    val onTertiary get() = material.onTertiary
    val background get() = material.background
    val onBackground get() = material.onBackground
    val surface get() = material.surface
    val onSurface get() = material.onSurface
}

sealed class ImageSearchColorScheme {
    abstract val lightScheme: ImageSearchColors
    abstract val darkScheme: ImageSearchColors

    data object Main : ImageSearchColorScheme() {
        override val lightScheme = ImageSearchColors(
            material = darkColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                tertiary = tertiary,
                onTertiary = onTertiary,
                background = background,
                onBackground = onBackground,
                surface = surface,
                onSurface = onSurface
            ),
            disabled = disabled,
            onDisabled = onDisabled,
            dropdown = dropdown,
            onDropDown = onDropDown
        )
        override val darkScheme = lightScheme
    }
}








