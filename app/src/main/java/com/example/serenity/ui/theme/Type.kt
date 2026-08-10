package com.example.serenity.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.example.serenity.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs
)

val RobotoFamily = FontFamily(
    Font(GoogleFont("Roboto"), provider, FontWeight.Normal),
    Font(GoogleFont("Roboto"), provider, FontWeight.Medium),
    Font(GoogleFont("Roboto"), provider, FontWeight.SemiBold),
    Font(GoogleFont("Roboto"), provider, FontWeight.Bold),
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily    = RobotoFamily,
        fontWeight    = FontWeight.Bold,
        fontSize      = 48.sp,
        lineHeight    = 56.sp,
        letterSpacing = (-1).sp
    ),
    headlineLarge = TextStyle(
        fontFamily    = RobotoFamily,
        fontWeight    = FontWeight.Bold,
        fontSize      = 34.sp,
        lineHeight    = 42.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily    = RobotoFamily,
        fontWeight    = FontWeight.Bold,
        fontSize      = 28.sp,
        lineHeight    = 36.sp,
        letterSpacing = (-0.5).sp
    ),
    titleLarge = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 22.sp,
        lineHeight = 30.sp
    ),
    titleMedium = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 18.sp,
        lineHeight = 26.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 15.sp,
        lineHeight = 22.sp
    ),
    bodySmall = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 13.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 15.sp
    ),
    labelSmall = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 12.sp
    )
)