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

val basicFolderColor1 = Color(0xFFFCC233)
val basicFolderColor2 = Color(0xFFF95B19)
val basicFolderColor3 = Color(0xFFCCF919)
val basicFolderColor4 = Color(0xFFF919E2)
val basicFolderColor5 = Color(0xFF7CF6F6)

data class ImageSearchColors(
    val material: ColorScheme,
    val disabled: Color,
    val onDisabled: Color,
    val dropdown: Color,
    val onDropDown: Color,
    val basicFolderColor1: Color,
    val basicFolderColor2: Color,
    val basicFolderColor3: Color,
    val basicFolderColor4: Color,
    val basicFolderColor5: Color
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


data object ImageSearchColorScheme {
    val defaultScheme = ImageSearchColors(
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
        onDropDown = onDropDown,
        basicFolderColor1 = basicFolderColor1,
        basicFolderColor2 = basicFolderColor2,
        basicFolderColor3 = basicFolderColor3,
        basicFolderColor4 = basicFolderColor4,
        basicFolderColor5 = basicFolderColor5
    )
}








