package com.example.serenity.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// ── Esquema de color oscuro (único tema — dark immersive) ─────────────────────
private val SerenityDarkColorScheme = darkColorScheme(
    primary          = TealStart,
    onPrimary        = Color(0xFF001F1E),
    primaryContainer = Color(0xFF003735),
    onPrimaryContainer = TextWhite,

    secondary        = LavenderEnd,
    onSecondary      = Color(0xFF1A0040),
    secondaryContainer = Color(0xFF2D1050),
    onSecondaryContainer = TextWhite,

    tertiary         = SageStart,
    onTertiary       = Color(0xFF00201A),
    tertiaryContainer = Color(0xFF003829),
    onTertiaryContainer = TextWhite,

    background       = HomeBgStart,
    onBackground     = TextWhite,

    surface          = Color(0xFF0F1E2E),
    onSurface        = TextWhite,
    surfaceVariant   = Color(0x26FFFFFF),
    onSurfaceVariant = TextWhite70,

    outline          = BorderGlass,
    outlineVariant   = BorderGlassSubtle,

    error            = SeveritySevere,
    onError          = Color.White,
    errorContainer   = Color(0xFF3A0A0D),
    onErrorContainer = Color(0xFFFFB3B3),

    inverseSurface   = TextWhite,
    inverseOnSurface = HomeBgStart,
    inversePrimary   = TealDeep,

    scrim            = Color(0x99000000)
)

// ── Tipografía ────────────────────────────────────────────────────────────────
private val SerenityTypography = Typography(
    // Títulos grandes
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize   = 28.sp,
        lineHeight = 34.sp,
        color      = TextWhite
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 24.sp,
        lineHeight = 30.sp,
        color      = TextWhite
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 20.sp,
        lineHeight = 26.sp,
        color      = TextWhite
    ),
    // Títulos de sección
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 18.sp,
        lineHeight = 24.sp,
        color      = TextWhite
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        lineHeight = 22.sp,
        color      = TextWhite
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        color      = TextWhite70
    ),
    // Cuerpo
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
        color      = TextWhite
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        color      = TextWhite70
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp,
        lineHeight = 18.sp,
        color      = TextWhite50
    ),
    // Labels
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp,
        color      = TextWhite
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize   = 12.sp,
        color      = TextWhite70
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize   = 11.sp,
        color      = TextWhite50
    )
)

// ── Tema principal ─────────────────────────────────────────────────────────────
@Composable
fun SerenityTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = SerenityDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Fondo de status bar transparente — inmersivo
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars     = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = SerenityTypography,
        content     = content
    )
}