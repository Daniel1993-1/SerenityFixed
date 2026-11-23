package com.example.serenity.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Canvas
import com.example.serenity.ui.MoodViewModel
import com.example.serenity.ui.components.SerenityBackground
import com.example.serenity.ui.theme.*

// ── Tabs ──────────────────────────────────────────────────────────────────────
private val tabs = listOf("PHQ-9", "GAD-7", "PSS-10", "Animo")

@Composable
fun ProgressScreen(viewModel: MoodViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) { viewModel.loadAll() }

    SerenityBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 24.dp)
        ) {
            Text("Progreso", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text("Evolucion de tus evaluaciones", fontSize = 14.sp, color = TextWhite50)

            Spacer(Modifier.height(20.dp))

            // ── Tabs ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceGlass)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tabs.forEachIndexed { index, label ->
                    val selected = selectedTab == index
                    val color = when (index) {
                        0    -> LavenderEnd
                        1    -> AuroraStart
                        2    -> TealStart
                        else -> Color(0xFFFF9F43)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (selected) color.copy(alpha = 0.25f) else Color.Transparent)
                            .border(
                                width = if (selected) 1.dp else 0.dp,
                                color = if (selected) color.copy(alpha = 0.5f) else Color.Transparent,
                                shape = RoundedCornerShape(9.dp)
                            )
                            .clickable { selectedTab = index }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            fontSize   = 12.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color      = if (selected) color else TextWhite50
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            when (selectedTab) {
                0 -> TestProgressSection(
                    title       = "Depresion — PHQ-9",
                    subtitle    = "Escala de 0 a 27 puntos",
                    results     = viewModel.phq9Results.map { it.score to it.severity },
                    maxScore    = 27,
                    accentColor = LavenderEnd,
                    ranges      = listOf(
                        ScoreRange(0..4,   "Minima",       SeverityMinimal),
                        ScoreRange(5..9,   "Leve",         SeverityMild),
                        ScoreRange(10..14, "Moderada",     SeverityModerate),
                        ScoreRange(15..19, "Mod. severa",  Color(0xFFFF9F43)),
                        ScoreRange(20..27, "Severa",       SeveritySevere)
                    )
                )
                1 -> TestProgressSection(
                    title       = "Ansiedad — GAD-7",
                    subtitle    = "Escala de 0 a 21 puntos",
                    results     = viewModel.gad7Results.map { it.score to it.severity },
                    maxScore    = 21,
                    accentColor = AuroraStart,
                    ranges      = listOf(
                        ScoreRange(0..4,   "Minima",   SeverityMinimal),
                        ScoreRange(5..9,   "Leve",     SeverityMild),
                        ScoreRange(10..14, "Moderada", SeverityModerate),
                        ScoreRange(15..21, "Severa",   SeveritySevere)
                    )
                )
                2 -> TestProgressSection(
                    title       = "Estres — PSS-10",
                    subtitle    = "Escala de 0 a 40 puntos",
                    results     = viewModel.pss10Results.map { it.score to it.severity },
                    maxScore    = 40,
                    accentColor = TealStart,
                    ranges      = listOf(
                        ScoreRange(0..13,  "Rango 0-13",     SeverityMinimal),
                        ScoreRange(14..26, "Rango 14-26", SeverityModerate),
                        ScoreRange(27..40, "Rango 27-40",     SeveritySevere)
                    )
                )
                3 -> MoodProgressSection(viewModel = viewModel)
            }
        }
    }
}

// ── Modelo de rango ───────────────────────────────────────────────────────────
data class ScoreRange(val range: IntRange, val label: String, val color: Color)

// ── Seccion de progreso por test ──────────────────────────────────────────────
@Composable
fun TestProgressSection(
    title: String,
    subtitle: String,
    results: List<Pair<Int, String>>,
    maxScore: Int,
    accentColor: Color,
    ranges: List<ScoreRange>
) {
    if (results.isEmpty()) {
        EmptyProgressCard(title = title, accentColor = accentColor)
        return
    }

    val last          = results.last()
    val prev          = if (results.size >= 2) results[results.size - 2] else null
    val trend         = if (prev != null) last.first - prev.first else 0
    val currentRange  = ranges.firstOrNull { last.first in it.range }
    val severityColor = currentRange?.color ?: accentColor
    val severityLabel = currentRange?.label ?: "Puntaje continuo"

    // ── KPI ───────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(accentColor.copy(alpha = 0.12f))
            .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            .padding(18.dp)
    ) {
        Column {
            Text(title,    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text(subtitle, fontSize = 12.sp, color = TextWhite50)
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("${last.first}", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = accentColor)
                Text(
                    "/$maxScore", fontSize = 16.sp, color = TextWhite50,
                    modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                )
                if (prev != null) {
                    Spacer(Modifier.width(12.dp))
                    val trendIcon  = if (trend <= 0) "↓" else "↑"
                    val trendColor = if (trend <= 0) SeverityMinimal else SeveritySevere
                    val trendText  = if (trend == 0) "Sin cambio" else "${if (trend > 0) "+" else ""}$trend vs anterior"
                    Surface(shape = RoundedCornerShape(20.dp), color = severityColor.copy(alpha = 0.2f)) {
                        Text(
                            severityLabel, // <--- AHORA USA LA ETIQUETA DEL RANGO
                            fontSize   = 12.sp,
                            color      = severityColor,
                            fontWeight = FontWeight.SemiBold,
                            modifier   = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(20.dp), color = severityColor.copy(alpha = 0.2f)) {
                Text(
                    last.second.replaceFirstChar { it.uppercase() },
                    fontSize   = 12.sp,
                    color      = severityColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }

    Spacer(Modifier.height(20.dp))

    // ── Grafica de linea ──────────────────────────────────────────────────
    if (results.size >= 2) {
        Text("Evolucion", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
        Spacer(Modifier.height(10.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = SurfaceGlass) {
            Column(modifier = Modifier.padding(16.dp)) {
                LineChart(
                    dataPoints  = results.map { it.first.toFloat() },
                    maxValue    = maxScore.toFloat(),
                    accentColor = accentColor,
                    ranges      = ranges
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Cada punto = un test completado",
                    fontSize  = 11.sp,
                    color     = TextWhite30,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(Modifier.height(20.dp))
    }

    // ── Leyenda de rangos ─────────────────────────────────────────────────
    Text("Rangos de severidad", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
    Spacer(Modifier.height(10.dp))
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = SurfaceGlass) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ranges.forEach { range ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(range.color)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "${range.range.first}–${range.range.last}",
                        fontSize = 13.sp, color = TextWhite70,
                        modifier = Modifier.width(48.dp)
                    )
                    Text(range.label, fontSize = 13.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    if (last.first in range.range) {
                        Surface(shape = RoundedCornerShape(20.dp), color = range.color.copy(alpha = 0.2f)) {
                            Text(
                                "← Actual", fontSize = 10.sp, color = range.color,
                                fontWeight = FontWeight.SemiBold,
                                modifier   = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(20.dp))

    // ── Historial ─────────────────────────────────────────────────────────
    if (results.size >= 2) {
        Text("Historial", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
        Spacer(Modifier.height(10.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = SurfaceGlass) {
            Column(modifier = Modifier.padding(14.dp)) {
                results.reversed().forEachIndexed { index, (score, severity) ->
                    val currentHistRange = ranges.firstOrNull { score in it.range }
                    val rangeColor = currentHistRange?.color ?: accentColor
                    val histLabel  = currentHistRange?.label ?: "Puntaje continuo"

                    Row(
                        modifier          = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Test ${results.size - index}",
                            fontSize = 12.sp, color = TextWhite50,
                            modifier = Modifier.width(52.dp)
                        )
                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(BorderGlass)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(score.toFloat() / maxScore)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(rangeColor)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "$score",
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color      = TextWhite,
                            modifier   = Modifier.width(30.dp)
                        )
                        Surface(shape = RoundedCornerShape(20.dp), color = rangeColor.copy(alpha = 0.15f)) {
                            Text(
                                histLabel, // <--- AQUÍ SE MUESTRA "Rango 14-26"
                                fontSize = 10.sp, color = rangeColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (index < results.size - 1)
                        HorizontalDivider(color = BorderGlass.copy(alpha = 0.5f))
                }
            }
        }
    }
}

// ── Grafica de linea (Canvas puro, sin librerías externas) ───────────────────
@Composable
fun LineChart(
    dataPoints: List<Float>,
    maxValue: Float,
    accentColor: Color,
    ranges: List<ScoreRange>
) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
        val w      = size.width
        val h      = size.height
        val padL   = 36f
        val padR   = 16f
        val padT   = 16f
        val padB   = 24f
        val chartW = w - padL - padR
        val chartH = h - padT - padB

        // Zonas de color por rango
        ranges.forEach { range ->
            val yTop = padT + chartH * (1f - range.range.last.toFloat() / maxValue)
            val yBot = padT + chartH * (1f - range.range.first.toFloat() / maxValue)
            drawRect(
                color   = range.color.copy(alpha = 0.07f),
                topLeft = Offset(padL, yTop.coerceAtLeast(padT)),
                size    = androidx.compose.ui.geometry.Size(
                    chartW,
                    (yBot - yTop).coerceAtMost(chartH)
                )
            )
        }

        // Lineas horizontales de referencia
        (0..4).forEach { i ->
            val y = padT + chartH * i / 4f
            drawLine(
                color       = BorderGlass.copy(alpha = 0.3f),
                start       = Offset(padL, y),
                end         = Offset(padL + chartW, y),
                strokeWidth = 1f
            )
            drawText(
                textMeasurer = textMeasurer,
                text         = (maxValue * (1f - i / 4f)).toInt().toString(),
                style        = TextStyle(fontSize = 9.sp, color = TextWhite30),
                topLeft      = Offset(0f, y - 8f)
            )
        }

        if (dataPoints.size < 2) return@Canvas

        val points = dataPoints.mapIndexed { i, v ->
            Offset(
                padL + i.toFloat() / (dataPoints.size - 1) * chartW,
                padT + chartH * (1f - v / maxValue)
            )
        }

        // Area de relleno
        val path = Path().apply {
            moveTo(points.first().x, padT + chartH)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, padT + chartH)
            close()
        }
        drawPath(
            path  = path,
            brush = Brush.verticalGradient(
                colors = listOf(accentColor.copy(alpha = 0.3f), accentColor.copy(alpha = 0.0f)),
                startY = padT,
                endY   = padT + chartH
            )
        )

        // Linea principal
        for (i in 0 until points.size - 1) {
            drawLine(
                color       = accentColor,
                start       = points[i],
                end         = points[i + 1],
                strokeWidth = 2.5f,
                cap         = StrokeCap.Round
            )
        }

        // Puntos circulares
        points.forEach { pt ->
            drawCircle(SurfaceGlass, radius = 6f, center = pt)
            drawCircle(accentColor,  radius = 4f, center = pt)
        }
    }
}

// ── Seccion de animo ──────────────────────────────────────────────────────────
@Composable
fun MoodProgressSection(viewModel: MoodViewModel) {
    val moods = viewModel.moodHistory

    if (moods.isEmpty()) {
        EmptyProgressCard(title = "Registro de animo", accentColor = Color(0xFFFF9F43))
        return
    }

    val moodColors = mapOf(
        "Feliz"    to SeverityMinimal,
        "Bien"     to TealStart,
        "Neutral"  to Color(0xFFFF9F43),
        "Triste"   to SeverityModerate,
        "Ansioso"  to SeveritySevere
    )
    val moodValues = mapOf(
        "Feliz"   to 5,
        "Bien"    to 4,
        "Neutral" to 3,
        "Triste"  to 2,
        "Ansioso" to 1
    )

    val lastMood  = moods.last()
    val lastColor = moodColors[lastMood.emotion] ?: Color(0xFFFF9F43)

    // KPI
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFFF9F43).copy(alpha = 0.12f))
            .border(1.dp, Color(0xFFFF9F43).copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            .padding(18.dp)
    ) {
        Column {
            Text("Registro de animo", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text("${moods.size} registros totales", fontSize = 12.sp, color = TextWhite50)
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Ultimo:", fontSize = 13.sp, color = TextWhite50)
                Spacer(Modifier.width(8.dp))
                Surface(shape = RoundedCornerShape(20.dp), color = lastColor.copy(alpha = 0.2f)) {
                    Text(
                        lastMood.emotion,
                        fontSize   = 13.sp,
                        color      = lastColor,
                        fontWeight = FontWeight.SemiBold,
                        modifier   = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(20.dp))

    // Grafica si hay suficientes datos
    if (moods.size >= 2) {
        Text("Evolucion del animo", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
        Spacer(Modifier.height(10.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = SurfaceGlass) {
            Column(modifier = Modifier.padding(16.dp)) {
                LineChart(
                    dataPoints  = moods.map { moodValues[it.emotion]?.toFloat() ?: 3f },
                    maxValue    = 5f,
                    accentColor = Color(0xFFFF9F43),
                    ranges      = listOf(
                        ScoreRange(4..5, "Bien",    SeverityMinimal),
                        ScoreRange(3..3, "Neutral", Color(0xFFFF9F43)),
                        ScoreRange(1..2, "Mal",     SeveritySevere)
                    )
                )
            }
        }
        Spacer(Modifier.height(20.dp))
    }

    // Distribucion
    Text("Distribucion", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
    Spacer(Modifier.height(10.dp))
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = SurfaceGlass) {
        Column(modifier = Modifier.padding(14.dp)) {
            moodColors.entries.forEach { (label, color) ->
                val count   = moods.count { it.emotion == label }
                val percent = if (moods.isNotEmpty()) count.toFloat() / moods.size else 0f
                if (count > 0) {
                    Row(
                        modifier          = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(label, fontSize = 13.sp, color = TextWhite70, modifier = Modifier.width(70.dp))
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(BorderGlass)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(percent)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "$count",
                            fontSize   = 12.sp,
                            color      = color,
                            fontWeight = FontWeight.Bold,
                            modifier   = Modifier.width(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────
@Composable
fun EmptyProgressCard(title: String, accentColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(accentColor.copy(alpha = 0.08f))
            .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(18.dp))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📊", fontSize = 40.sp)
            Spacer(Modifier.height(12.dp))
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
            Spacer(Modifier.height(6.dp))
            Text(
                "Completa al menos un test para ver tu progreso aqui.",
                fontSize  = 13.sp,
                color     = TextWhite50,
                textAlign = TextAlign.Center
            )
        }
    }
}