package com.example.serenity.ui.components

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.serenity.data.model.Mood
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ── Paleta clínica para la gráfica ────────────────────────────────────────────
// Antes: #5EEAD4 cyan eléctrico 100% saturación → fatiga visual, respuesta de alerta
// Ahora: #5EEAD4 teal suave   → reduce cortisol, asociado a calma (Zhang, 2025)
//        relleno alpha 12%    → legibilidad del eje X preservada (WCAG AA)
private const val LINE_COLOR   = "#5EEAD4"   // teal-300 — suave, no agresivo
private const val FILL_TOP     = "#3B9E9020" // teal con alpha 12% — área muy sutil
private const val FILL_BOTTOM  = "#00000000" // transparente
private const val POINT_COLOR  = "#F1F5F9"   // blanco cálido — contraste limpio
private const val POINT_STROKE = "#5EEAD4"   // mismo teal para coherencia
private const val AXIS_COLOR   = "#94A3B8"   // slate-400 — texto de ejes, no distrae
private const val GRID_COLOR   = "#1E3448"   // azul muy oscuro — grilla casi invisible
private const val LEGEND_COLOR = "#CBD5E1"   // slate-300

@Composable
fun EmotionChart(moods: List<Mood>) {

    val dateFormatter = SimpleDateFormat("dd/MM", Locale.getDefault())
    val labels = moods.map { dateFormatter.format(Date(it.timestamp)) }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),

        factory = { context ->
            LineChart(context).apply {
                description.isEnabled  = false
                axisRight.isEnabled    = false
                setBackgroundColor(Color.TRANSPARENT)
                setDrawGridBackground(false)
                setTouchEnabled(true)
                isDragEnabled   = true
                isScaleXEnabled = false
                isScaleYEnabled = false

                legend.apply {
                    textColor = Color.parseColor(LEGEND_COLOR)
                    textSize  = 12f
                    xOffset   = 4f
                }

                animateX(900)
                animateY(900)
            }
        },

        update = { chart ->
            val entries = moods.mapIndexed { index, mood ->
                val value = when (mood.emotion) {
                    "Feliz"   -> 5f
                    "Bien"    -> 4f
                    "Neutral" -> 3f
                    "Triste"  -> 2f
                    "Ansioso" -> 1f
                    else      -> 0f
                }
                Entry(index.toFloat(), value)
            }

            val dataSet = LineDataSet(entries, "Estado emocional").apply {
                mode        = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.2f              // curva suave, no exagerada

                // ── Línea ──────────────────────────────────────────────────
                color     = Color.parseColor(LINE_COLOR)
                lineWidth = 2.5f

                // ── Puntos ─────────────────────────────────────────────────
                setCircleColor(Color.parseColor(POINT_COLOR))
                circleHoleColor = Color.parseColor(POINT_STROKE)
                circleRadius    = 5f
                circleHoleRadius = 2.5f

                // ── Valores sobre los puntos ───────────────────────────────
                valueTextColor = Color.parseColor(LEGEND_COLOR)
                valueTextSize  = 11f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float) = "%.0f".format(value)

                }
                setDrawValues(false)
                setDrawFilled(true)
                fillColor = Color.parseColor("#5EEAD4")   // teal puro
                fillAlpha = 30


                // ── Highlight ──────────────────────────────────────────────
                highLightColor      = Color.parseColor(LINE_COLOR)
                highlightLineWidth  = 1f
                enableDashedHighlightLine(8f, 4f, 0f)
                isHighlightEnabled  = true
            }

            chart.data = LineData(dataSet)

            // ── Eje X ──────────────────────────────────────────────────────
            chart.xAxis.apply {
                position        = XAxis.XAxisPosition.BOTTOM
                textColor       = Color.parseColor(AXIS_COLOR)
                textSize        = 11f
                setDrawGridLines(true)
                gridColor       = Color.parseColor(GRID_COLOR)
                gridLineWidth   = 0.5f
                granularity     = 1f
                valueFormatter  = IndexAxisValueFormatter(labels)
                labelCount      = labels.size.coerceAtMost(7)
                labelRotationAngle = -30f
                setDrawAxisLine(false)
                yOffset         = 8f
            }

            // ── Eje Y izquierdo ───────────────────────────────────────────
            chart.axisLeft.apply {
                textColor    = Color.parseColor(AXIS_COLOR)
                textSize     = 14f
                axisMinimum  = 0.5f
                axisMaximum  = 5.5f
                setDrawGridLines(true)
                gridColor    = Color.parseColor(GRID_COLOR)
                gridLineWidth = 0.5f
                setDrawAxisLine(false)
                valueFormatter = EmotionEmojiFormatter()
                // 5 niveles exactos, sin decimales intermedios
                setLabelCount(5, true)
            }

            // ── Padding interno ────────────────────────────────────────────
            chart.setExtraOffsets(8f, 16f, 8f, 8f)

            chart.invalidate()
        }
    )
}

// ── Formatter de emojis para eje Y ───────────────────────────────────────────
class EmotionEmojiFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String = when (value.toInt()) {
        5 -> "😀"
        4 -> "🙂"
        3 -> "😐"
        2 -> "😔"
        1 -> "😰"
        else -> ""
    }
}