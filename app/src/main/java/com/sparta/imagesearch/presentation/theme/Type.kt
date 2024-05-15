package com.sparta.imagesearch.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.sparta.imagesearch.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
val fontName = GoogleFont("Jua")
val fontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

val AppTypography = Typography(
    titleMedium = TextStyle(
        fontSize = 22.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    titleSmall = TextStyle(
        fontSize = 20.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    bodyMedium = TextStyle(
        fontSize = 18.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    ),
    bodySmall = TextStyle(
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
        fontFamily = fontFamily
    )
)
