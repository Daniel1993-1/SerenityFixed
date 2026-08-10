package com.example.serenity.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.serenity.ui.theme.*

// ── Tipos de fondo por sección ────────────────────────────────────────────────
enum class ScreenBackground {
    HOME, TEST, PROGRESS, RESOURCES, PROFILE, LOGIN
}

// ── Componente principal — reemplaza el SerenityBackground anterior ───────────
@Composable
fun SerenityBackground(
    screen: ScreenBackground = ScreenBackground.HOME,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val (start, end) = when (screen) {
        ScreenBackground.HOME      -> HomeBgStart      to HomeBgEnd
        ScreenBackground.TEST      -> TestBgStart      to TestBgEnd
        ScreenBackground.PROGRESS  -> ProgressBgStart  to ProgressBgEnd
        ScreenBackground.RESOURCES -> ResourceBgStart  to ResourceBgEnd
        ScreenBackground.PROFILE   -> ProfileBgStart   to ProfileBgEnd
        ScreenBackground.LOGIN     -> LoginBgStart     to LoginBgEnd
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        start,
                        blendColor(start, end, 0.5f),
                        end
                    )
                )
            )
    ) {
        // Capa de brillo sutil en la parte superior (efecto aurora)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            accentGlow(screen).copy(alpha = 0.07f),
                            Color.Transparent
                        ),
                        radius = 900f
                    )
                )
        )
        content()
    }
}

// Shortcut sin enum para mantener compatibilidad con pantallas anteriores
@Composable
fun SerenityBackground(
    content: @Composable BoxScope.() -> Unit
) = SerenityBackground(screen = ScreenBackground.HOME, content = content)

// ── Helpers privados ──────────────────────────────────────────────────────────
private fun blendColor(a: Color, b: Color, ratio: Float): Color {
    return Color(
        red   = a.red   * (1 - ratio) + b.red   * ratio,
        green = a.green * (1 - ratio) + b.green * ratio,
        blue  = a.blue  * (1 - ratio) + b.blue  * ratio,
        alpha = 1f
    )
}

private fun accentGlow(screen: ScreenBackground): Color = when (screen) {
    ScreenBackground.HOME      -> TealStart
    ScreenBackground.TEST      -> LavenderEnd
    ScreenBackground.PROGRESS  -> SageStart
    ScreenBackground.RESOURCES -> AuroraStart
    ScreenBackground.PROFILE   -> AuroraMid
    ScreenBackground.LOGIN     -> TealStart
}