package com.example.serenity.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Teal   = Color(0xFF2DD4BF)
private val Aurora = Color(0xFF818CF8)
private val BgDark = Color(0xFF071622)
private val BgCard = Color(0xFF0A2333)
private val White  = Color(0xFFF1F5F9)

@Composable
fun SerenityLogo(
    iconSize: Dp = 96.dp,
    showTagline: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── Ícono ─────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(iconSize * 0.28f))
                .background(BgCard)
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Teal.copy(alpha = 0.9f), Aurora.copy(alpha = 0.5f))
                    ),
                    shape = RoundedCornerShape(iconSize * 0.28f)
                )
                .drawBehind {
                    val w  = size.width
                    val h  = size.height
                    val cx = w / 2f
                    val cy = h / 2f
                    val r  = minOf(w, h) * 0.36f

                    // ── Hoja ──────────────────────────────────────────────
                    val leaf = Path().apply {
                        moveTo(cx, cy - r)
                        cubicTo(cx + r, cy - r * 0.5f, cx + r, cy + r * 0.5f, cx, cy + r)
                        cubicTo(cx - r, cy + r * 0.5f, cx - r, cy - r * 0.5f, cx, cy - r)
                        close()
                    }

                    // Relleno sutil
                    drawPath(
                        leaf,
                        brush = Brush.linearGradient(
                            colors = listOf(Teal.copy(alpha = 0.14f), Aurora.copy(alpha = 0.07f)),
                            start  = Offset(cx, cy - r),
                            end    = Offset(cx, cy + r)
                        )
                    )

                    // Borde de la hoja
                    drawPath(
                        leaf,
                        brush = Brush.linearGradient(
                            colors = listOf(Teal, Aurora),
                            start  = Offset(0f, 0f),
                            end    = Offset(w, h)
                        ),
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // Nervadura
                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(Teal.copy(alpha = 0.5f), Teal.copy(alpha = 0.05f)),
                            start  = Offset(cx, cy - r * 0.8f),
                            end    = Offset(cx, cy + r * 0.8f)
                        ),
                        start       = Offset(cx, cy - r * 0.8f),
                        end         = Offset(cx, cy + r * 0.8f),
                        strokeWidth = 1.2.dp.toPx(),
                        cap         = StrokeCap.Round
                    )

                    // ── ECG ───────────────────────────────────────────────
                    val eY  = cy + r * 0.2f
                    val eL  = cx - r * 0.75f
                    val eR  = cx + r * 0.75f

                    val ecg = Path().apply {
                        moveTo(eL, eY)
                        lineTo(cx - r * 0.35f, eY)
                        lineTo(cx - r * 0.18f, eY + r * 0.20f)
                        lineTo(cx,             eY - r * 0.65f)
                        lineTo(cx + r * 0.18f, eY + r * 0.25f)
                        lineTo(cx + r * 0.30f, eY - r * 0.12f)
                        lineTo(cx + r * 0.45f, eY)
                        lineTo(eR, eY)
                    }

                    // Glow
                    drawPath(
                        ecg,
                        color = Teal.copy(alpha = 0.2f),
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                    // Línea ECG
                    drawPath(
                        ecg,
                        brush = Brush.linearGradient(
                            colors = listOf(Teal, Color.White.copy(alpha = 0.9f), Teal),
                            start  = Offset(eL, eY),
                            end    = Offset(eR, eY)
                        ),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // Punto de pulso
                    drawCircle(color = Teal, radius = 3.dp.toPx(), center = Offset(eR, eY))
                    drawCircle(
                        color  = Teal.copy(alpha = 0.3f),
                        radius = 5.dp.toPx(),
                        center = Offset(eR, eY),
                        style  = Stroke(width = 1.dp.toPx())
                    )
                },
            contentAlignment = Alignment.Center
        ) {}

        Spacer(Modifier.height(18.dp))

        Text(
            text          = "Serenity",
            fontSize      = 34.sp,
            fontWeight    = FontWeight.Bold,
            color         = White,
            letterSpacing = 0.5.sp
        )

        if (showTagline) {
            Spacer(Modifier.height(5.dp))
            Text(
                text          = "Salud mental · Contigo siempre",
                fontSize      = 12.sp,
                color         = Teal.copy(alpha = 0.85f),
                textAlign     = TextAlign.Center,
                letterSpacing = 0.3.sp
            )
        }
    }
}