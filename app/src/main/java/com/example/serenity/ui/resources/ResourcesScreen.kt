package com.example.serenity.ui.resources

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serenity.ui.components.SerenityBackground
import com.example.serenity.ui.theme.*

// ── Modelos ───────────────────────────────────────────────────────────────────
data class GuideStep(val icon: String, val title: String, val description: String)

data class ResourceGuide(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val accentColor: Color,
    val scienceBadge: String,         // evidencia cientifica
    val targetCondition: String,      // para que sirve
    val duration: String,             // tiempo estimado
    val steps: List<GuideStep>,       // pasos guiados
    val scienceNote: String,          // nota clinica con fuente
    val tip: String                   // consejo practico
)

// ── Contenido científicamente validado ──────────────────────────────────────
private val guides = listOf(

    ResourceGuide(
        emoji           = "🫁",
        title           = "Respiracion 4-7-8",
        subtitle        = "Tecnica de regulacion del sistema nervioso",
        accentColor     = TealStart,
        scienceBadge    = "Evidencia clinica",
        targetCondition = "Ansiedad · Estres · Insomnio",
        duration        = "5 minutos",
        steps = listOf(
            GuideStep("1️⃣", "Postura", "Sientate con la espalda recta o recuestate. Coloca la punta de la lengua contra el paladar, detras de los dientes superiores. Mantienla ahi durante todo el ejercicio."),
            GuideStep("2️⃣", "Exhala completamente", "Exhala todo el aire por la boca con un sonido suave. Vacia completamente tus pulmones antes de comenzar el ciclo."),
            GuideStep("3️⃣", "Inhala 4 segundos", "Cierra la boca e inhala silenciosamente por la nariz mientras cuentas mentalmente: 1... 2... 3... 4. Siente como tu abdomen se expande."),
            GuideStep("4️⃣", "Sostiene 7 segundos", "Retiene el aire contando: 1... 2... 3... 4... 5... 6... 7. No te fuerces. Si sientes presion, exhala y reduce el tiempo."),
            GuideStep("5️⃣", "Exhala 8 segundos", "Exhala completamente por la boca contando: 1... 2... 3... 4... 5... 6... 7... 8. Siente como la tension abandona tu cuerpo."),
            GuideStep("🔁", "Repite 4 veces", "Ese es un ciclo completo. Repite 3 ciclos mas (4 en total). Con la practica regular, aumenta a 8 ciclos. Practica minimo 2 veces al dia.")
        ),
        scienceNote = "Una revision sistematica (PRISMA 2025, 15 estudios) confirma que la tecnica 4-7-8 activa el nervio vago, mejora la variabilidad de frecuencia cardiaca y reduce significativamente ansiedad y estres. Efectiva tanto en poblacion clinica como general.",
        tip         = "El momento ideal: justo antes de dormir y cuando sientas que la ansiedad sube. A las 2 semanas de practica diaria los efectos se vuelven notables."
    ),

    ResourceGuide(
        emoji           = "📦",
        title           = "Respiracion en Caja (Box Breathing)",
        subtitle        = "Tecnica de control del estres usada por SEAL de la Marina de EE.UU.",
        accentColor     = AuroraStart,
        scienceBadge    = "Alta evidencia",
        targetCondition = "Estres agudo · Ansiedad · Concentracion",
        duration        = "4 minutos",
        steps = listOf(
            GuideStep("1️⃣", "Postura y preparacion", "Sientate derecho/a. Relaja los hombros. Coloca las manos sobre los muslos. Cierra los ojos si te sientes comodo/a."),
            GuideStep("2️⃣", "Inhala 4 segundos", "Inhala lentamente por la nariz contando 4 segundos. Siente como tu pecho y abdomen se llenan de aire de abajo hacia arriba."),
            GuideStep("3️⃣", "Sostiene 4 segundos", "Sostiene el aire sin tensarte. Solo cuenta: 1... 2... 3... 4. Tu cuerpo esta en pausa, tranquilo."),
            GuideStep("4️⃣", "Exhala 4 segundos", "Exhala lentamente por la nariz o boca contando 4 segundos. Libera suavemente, sin forzar."),
            GuideStep("5️⃣", "Pausa 4 segundos", "Mantente sin aire 4 segundos antes de volver a inhalar. Esta es la 'caja': 4-4-4-4."),
            GuideStep("🔁", "Repite 4-6 ciclos", "Cuatro ciclos toman aproximadamente 4 minutos. Puedes hacerlo en cualquier lugar: trabajo, transporte, antes de una reunion dificil.")
        ),
        scienceNote = "Un estudio en Cell Reports Medicine (2023) comparó box breathing con otras tecnicas y meditacion mindfulness. El resultado: breathwork produjo mayores mejoras en estado de animo y mayor reduccion de la frecuencia respiratoria que la meditacion sola.",
        tip         = "Usar antes de situaciones de alto estres (examen, conflicto, reunion). Es discreta: nadie nota que la estas practicando."
    ),

    ResourceGuide(
        emoji           = "🌿",
        title           = "Tecnica 5-4-3-2-1 (Grounding)",
        subtitle        = "Ancla tu mente al presente y corta el ciclo de ansiedad",
        accentColor     = Color(0xFF4ECDC4),
        scienceBadge    = "TCC validada",
        targetCondition = "Ansiedad · Ataques de panico · Disociacion",
        duration        = "3-5 minutos",
        steps = listOf(
            GuideStep("👀", "5 cosas que VES", "Mira a tu alrededor y nombra mentalmente (o en voz alta) 5 objetos. Se especifico: no 'una silla', sino 'una silla azul con ruedas'. El detalle ancla tu atencion."),
            GuideStep("✋", "4 cosas que TOCAS", "Lleva tu atencion a 4 texturas fisicas. La tela de tu ropa, el frio del celular, la superficie de la mesa. Siente cada una conscientemente 2-3 segundos."),
            GuideStep("👂", "3 cosas que ESCUCHAS", "Cierra los ojos y escucha. Identifica 3 sonidos distintos: el trafico, el ventilador, tu propia respiracion. No los juzgues, solo registralos."),
            GuideStep("👃", "2 cosas que HUELES", "Inhala y detecta 2 aromas. Puede ser tu ropa, el ambiente, un alimento cercano. Si no hay olores evidentes, recuerda un olor que te guste."),
            GuideStep("👅", "1 cosa que SABOREAS", "Nota el sabor actual en tu boca. Un sorbo de agua, una menta, o simplemente el sabor neutro de tu boca. Un solo foco, bien definido."),
            GuideStep("🧘", "Respira y regresa", "Toma 3 respiraciones lentas. Nota como tu mente esta mas aqui, en el presente. La ansiedad vive en el futuro: el grounding te trae al ahora.")
        ),
        scienceNote = "El grounding sensoriai es un componente central de la Terapia Cognitivo-Conductual (TCC) y la Terapia Dialectico-Conductual (DBT). Actua interrumpiendo el ciclo de hiperactivacion del sistema nervioso simpatico al redirigir los recursos atencionales al momento presente.",
        tip         = "Guarda este recurso en favoritos. Cuando empieces a sentir ansiedad, abrir la app y seguir los 5 pasos puede cortar el ciclo antes de que escale."
    ),

    ResourceGuide(
        emoji           = "🧘",
        title           = "Escaneo Corporal (Body Scan)",
        subtitle        = "Mindfulness basado en evidencia para estres y sueno",
        accentColor     = LavenderEnd,
        scienceBadge    = "MBSR validado",
        targetCondition = "Estres · Insomnio · Depresion leve",
        duration        = "10-15 minutos",
        steps = listOf(
            GuideStep("🛏️", "Postura inicial", "Recustate boca arriba con los brazos a los lados, palmas hacia arriba. Si no puedes acostarte, sientate con la espalda apoyada. Cierra los ojos."),
            GuideStep("🌬️", "Tres respiraciones profundas", "Toma 3 respiraciones lentas y profundas. Con cada exhalacion, siente como tu cuerpo se hunde un poco mas en la superficie. Deja ir la tension."),
            GuideStep("🦶", "Pies y tobillos", "Lleva tu atencion a los pies. Nota sensaciones: calor, frio, hormigueo, peso. No juzgues, solo observa. Permanece ahi 30-60 segundos, luego sube."),
            GuideStep("🦵", "Piernas y caderas", "Sube lentamente por pantorrillas, rodillas, muslos, caderas. Nota donde hay tension. No intentes relajar a la fuerza, solo observa con curiosidad."),
            GuideStep("💪", "Abdomen, pecho y brazos", "Nota el movimiento del abdomen al respirar. Siente el pecho. Recorre cada brazo hasta las yemas de los dedos. Cualquier sensacion es valida."),
            GuideStep("😌", "Cuello, cara y cabeza", "Recorre cuello, mandibula (zona de mucha tension), ojos, frente y cuero cabelludo. Termina con una imagen mental de todo tu cuerpo, integrado y en calma."),
            GuideStep("🌅", "Regreso gradual", "Mueve suavemente los dedos de manos y pies. Estira si lo necesitas. Abre los ojos despacio. Toma un momento antes de levantarte.")
        ),
        scienceNote = "Una revision sistematica publicada en Psychiatry Research (2022) con poblacion psiquiatrica muestra que las intervenciones basadas en mindfulness mejoran significativamente el sueno (SMD -0.52), reducen estres (SMD -0.89) y depresion (SMD -0.77). El Body Scan es el componente central del programa MBSR (Mindfulness-Based Stress Reduction) de Jon Kabat-Zinn.",
        tip         = "Practicamente SIEMPRE antes de dormir tiene el mayor impacto. Si tu mente divaga, es normal: vuelve suavemente a la parte del cuerpo donde ibas. Eso es exactamente la practica."
    ),

    ResourceGuide(
        emoji           = "🌙",
        title           = "Rutina de Higiene del Sueno",
        subtitle        = "Protocolo CBT-I para restaurar el sueno natural",
        accentColor     = Color(0xFF6C63FF),
        scienceBadge    = "CBT-I recomendada OMS",
        targetCondition = "Insomnio · Depresion · Ansiedad",
        duration        = "Implementar 2 semanas",
        steps = listOf(
            GuideStep("⏰", "Ancla tu horario", "Elige una hora fija para levantarte TODOS los dias, incluso fines de semana. Esta es la base del ritmo circadiano. No cambies la hora de levantarte aunque hayas dormido mal."),
            GuideStep("📵", "Zona sin pantallas -30 min", "30 minutos antes de dormir apaga TV, celular y computador. La luz azul suprime la melatonina hasta 2 horas. Si debes usar el celular, activa el modo noche."),
            GuideStep("🌡️", "Ambiente optimo", "La temperatura ideal para dormir es 18-20°C. Oscuridad total o antifaz. Si hay ruido, usa tapones o ruido blanco. Tu cuarto debe asociarse SOLO con dormir y descanso."),
            GuideStep("📓", "Diario de preocupaciones", "10 minutos antes de dormir, escribe en papel todo lo que te preocupa del dia siguiente. Cierra el cuaderno fisicamente. Este ritual le dice al cerebro: 'ya fue registrado, puedo descansar'."),
            GuideStep("🚫", "La regla del 20 minutos", "Si llevas mas de 20 minutos en cama sin dormir, levantate. Haz algo tranquilo (leer en papel, escuchar musica suave) en penumbra hasta sentir sueno real. Vuelve a la cama solo cuando tengas sueno."),
            GuideStep("☕", "Control de estimulantes", "Evita cafeina despues de las 2pm (su vida media es 5-7 horas). Evita alcohol: aunque ayuda a 'caer dormido', fragmenta las fases profundas del sueno y empeora la calidad general.")
        ),
        scienceNote = "La Terapia Cognitivo-Conductual para el Insomnio (CBT-I) es el tratamiento de primera linea recomendado por la OMS, APA y AASM por encima de la medicacion. Sus componentes (control de estimulos, restriccion de sueno, higiene) tienen tasas de exito del 70-80% en insomnio cronico.",
        tip         = "Los primeros 3-4 dias pueden ser dificiles. El beneficio real empieza en la semana 2. La consistencia lo es todo: un fin de semana de desorden puede reiniciar el proceso."
    ),

    ResourceGuide(
        emoji           = "💪",
        title           = "Activacion Conductual",
        subtitle        = "La herramienta #1 de la TCC contra la depresion",
        accentColor     = Color(0xFF4ECDC4),
        scienceBadge    = "TCC primera linea",
        targetCondition = "Depresion · Anhedonia · Aislamiento",
        duration        = "Iniciar hoy, 15 min",
        steps = listOf(
            GuideStep("📋", "Identifica 3 actividades", "Piensa en 3 actividades que antes disfrutabas o que sabes que son buenas para ti (aunque ahora no tengas ganas). Ejemplo: caminar, llamar a un amigo, dibujar, cocinar algo rico."),
            GuideStep("📅", "Agenda UNA para hoy", "No intentes hacerlas todas. Elige UNA sola y agendala para hoy con hora especifica. La depresion espera que 'tengas ganas'; la activacion conductual actua primero y las ganas llegan despues."),
            GuideStep("⏱️", "Compromiso minimo", "Si la tarea se siente enorme, reducela. En vez de 'hacer ejercicio 1 hora', compromete 'salir a caminar 10 minutos'. El umbral bajo es la clave para comenzar."),
            GuideStep("🚶", "Haz la actividad", "Haz la actividad aunque no tengas ganas, aunque no la disfrutes completamente. La accion precede a la motivacion en la depresion. Esto no es falso: es como funciona la neurobiologia."),
            GuideStep("📊", "Registra como te sentiste", "Despues de la actividad, pon una nota de 1-10 sobre como te sentiste. La mayoria de personas se sorprende: el 'antes' es mas bajo que el 'despues'. Este registro construye evidencia contra los pensamientos depresivos."),
            GuideStep("🔄", "Aumenta gradualmente", "Cada semana agrega una actividad mas. El objetivo no es 'sentirte bien' inmediatamente, sino ir reconstruyendo el circuito de recompensa cerebral (dopamina) que la depresion apaga.")
        ),
        scienceNote = "La Activacion Conductual (AC) tiene evidencia de nivel A en el tratamiento de la depresion mayor (meta-analisis Cochrane, 26 ensayos clinicos). Es tan efectiva como la terapia cognitiva completa y mas facil de implementar de forma autonoma. El ejercicio aerobico como forma de AC tiene efecto antidepresivo equivalente a medicacion leve (Harvard Medical School, 2023).",
        tip         = "El mayor error: esperar 'tener ganas'. En depresion las ganas NO vienen antes de actuar, sino DESPUES. Actuar primero aunque no quieras es exactamente lo que funciona."
    ),

    ResourceGuide(
        emoji           = "🤝",
        title           = "Activacion de Red de Apoyo",
        subtitle        = "Conexion social: el factor protector mas poderoso",
        accentColor     = Color(0xFFFF9F43),
        scienceBadge    = "Evidencia robusta",
        targetCondition = "Depresion · Aislamiento · Estres",
        duration        = "Una accion hoy",
        steps = listOf(
            GuideStep("🗺️", "Mapea tu red", "En un papel o mentalmente, identifica 3-5 personas en tu vida: familia, amigos, companeros, vecinos. No necesitan ser perfectas ni cercanas. Solo personas con quienes podrias tener contacto."),
            GuideStep("📱", "Elige UNA persona", "Elige a la persona con quien tengas menos barrera de contacto. No tiene que ser la mas cercana, sino la mas accesible hoy."),
            GuideStep("💬", "Mensaje simple", "Enviable un mensaje hoy. No tienes que explicar como te sientes. Puede ser: 'Hola, estaba pensando en ti. Como estas?' El contacto importa, no el contenido."),
            GuideStep("🗣️", "Si quieres hablar", "Si decides abrirte, no necesitas un discurso. Basta con: 'He estado un poco mal ultimamente y necesitaba hablar con alguien'. La mayoria de personas responde con mas calidez de lo que esperamos."),
            GuideStep("📅", "Agenda un encuentro", "Propone algo concreto: 'Tomamos un cafe esta semana?'. Los encuentros presenciales tienen mayor efecto protector que los mensajes. Una vez a la semana marca diferencia."),
            GuideStep("🌱", "Construye el habito", "El aislamiento se retroalimenta. Cada contacto social que inicias, aunque pequeno, va reconstruyendo el habito de conexion. No esperes sentirte listo/a para conectar.")
        ),
        scienceNote = "La soledad cronica tiene un impacto en la salud comparable a fumar 15 cigarrillos al dia (meta-analisis Holt-Lunstad, 150 estudios, 308,000 participantes). El apoyo social reduce el riesgo de depresion en un 50% y es el predictor mas consistente de recuperacion en trastornos del estado de animo.",
        tip         = "No tienes que 'sentirte social' para conectar. El impulso de aislarte cuando estas mal es real pero contraproducente. Un mensaje de 10 segundos puede romper el ciclo."
    ),

    ResourceGuide(
        emoji           = "📖",
        title           = "Registro de Pensamientos (TCC)",
        subtitle        = "Aprende a identificar y cuestionar pensamientos automaticos negativos",
        accentColor     = Color(0xFF6C63FF),
        scienceBadge    = "TCC primera linea OMS",
        targetCondition = "Depresion · Ansiedad · Estres",
        duration        = "10 minutos",
        steps = listOf(
            GuideStep("🎯", "Identifica la situacion", "Cuando te sientas mal, para y escribe: ¿Que estaba pasando exactamente? ¿Donde estabas? ¿Con quien? Se especifico: 'Me llamo mi jefe al trabajo' no 'problema en el trabajo'."),
            GuideStep("😔", "Nombra la emocion", "¿Que emocion sentiste? Ponle nombre: tristeza, miedo, rabia, verguenza, culpa. Calificala del 0 al 100 en intensidad. Nombrar la emocion ya reduce su intensidad (efecto 'affect labeling')."),
            GuideStep("💭", "Captura el pensamiento automatico", "¿Que pensamientos cruzaron tu mente en ese momento? Escribelos tal como llegaron, sin filtrar. Ejemplo: 'Soy un fracasado', 'Nadie me quiere', 'Todo va a salir mal'."),
            GuideStep("🔍", "Busca evidencias", "Preguntate: ¿Que evidencia REAL tengo a favor de este pensamiento? ¿Que evidencia tengo EN CONTRA? Separa hechos de interpretaciones."),
            GuideStep("⚖️", "Genera un pensamiento alternativo", "Basandote en las evidencias, escribe un pensamiento mas equilibrado. No tiene que ser 'positivo', solo mas justo y realista. Ejemplo: 'Cometi un error, no soy un fracasado'."),
            GuideStep("📉", "Reevalua la emocion", "Ahora vuelve a calificar la emocion del 0 al 100. ¿Bajo? Eso es la TCC funcionando. Con practica, este proceso se vuelve automatico y tarda segundos, no minutos.")
        ),
        scienceNote = "La Terapia Cognitivo-Conductual (TCC) es la intervencion psicologica con mayor respaldo empirico para depresion y ansiedad (OMS 2023, recomendacion de alta calidad). El registro de pensamientos es su herramienta nuclear. En formato autoguiado digital ha demostrado efectividad moderada-alta en meta-analisis con mas de 30,000 participantes.",
        tip         = "No necesitas hacerlo perfecto. El simple acto de escribir el pensamiento ya crea distancia entre 'yo' y 'el pensamiento'. Esa distancia es el inicio del cambio."
    )
)

// ── Pantalla principal ────────────────────────────────────────────────────────
@Composable
fun ResourcesScreen() {
    var selectedGuide by remember { mutableStateOf<Int?>(null) }

    SerenityBackground {
        if (selectedGuide != null) {
            // Vista detallada de la guia
            GuideDetailView(
                guide     = guides[selectedGuide!!],
                onBack    = { selectedGuide = null }
            )
        } else {
            // Lista de recursos
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 24.dp)
            ) {
                Text("Recursos", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text("Guias paso a paso con base cientifica", fontSize = 14.sp, color = TextWhite50)

                Spacer(Modifier.height(16.dp))

                // Aviso legal
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                        .background(BorderGlass.copy(alpha = 0.3f))
                        .border(1.dp, BorderGlass, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        "Contenido psicoeducativo basado en evidencia clinica. No reemplaza atencion profesional. Si te sientes en crisis, llama a la Linea 106.",
                        fontSize = 11.sp, color = TextWhite50, lineHeight = 17.sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                guides.forEachIndexed { index, guide ->
                    ResourceCard(guide = guide, onClick = { selectedGuide = index })
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

// ── Tarjeta de recurso ────────────────────────────────────────────────────────
@Composable
fun ResourceCard(guide: ResourceGuide, onClick: () -> Unit) {
    Surface(
        modifier  = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape     = RoundedCornerShape(16.dp),
        color     = SurfaceGlass,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp))
                    .background(guide.accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) { Text(guide.emoji, fontSize = 26.sp) }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(guide.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
                }
                Spacer(Modifier.height(2.dp))
                Text(guide.subtitle, fontSize = 12.sp, color = TextWhite50, lineHeight = 17.sp)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Badge evidencia
                    Surface(shape = RoundedCornerShape(20.dp), color = guide.accentColor.copy(alpha = 0.15f)) {
                        Text(guide.scienceBadge, fontSize = 10.sp, color = guide.accentColor,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                    }
                    // Badge duracion
                    Surface(shape = RoundedCornerShape(20.dp), color = BorderGlass) {
                        Text("⏱ ${guide.duration}", fontSize = 10.sp, color = TextWhite50,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(guide.targetCondition, fontSize = 11.sp, color = TextWhite30)
            }

            Spacer(Modifier.width(8.dp))
            Text("›", fontSize = 22.sp, color = guide.accentColor, fontWeight = FontWeight.Bold)
        }
    }
}

// ── Vista detallada de la guia ────────────────────────────────────────────────
@Composable
fun GuideDetailView(guide: ResourceGuide, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 32.dp)
    ) {
        // Boton volver
        Row(
            modifier = Modifier.clickable(onClick = onBack).padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", fontSize = 22.sp, color = guide.accentColor, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(6.dp))
            Text("Volver", fontSize = 14.sp, color = guide.accentColor)
        }

        Spacer(Modifier.height(16.dp))

        // Header
        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(listOf(guide.accentColor.copy(alpha = 0.25f), guide.accentColor.copy(alpha = 0.05f))))
                .border(1.dp, guide.accentColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(guide.emoji, fontSize = 32.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(guide.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text(guide.subtitle, fontSize = 12.sp, color = TextWhite70)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(20.dp), color = guide.accentColor.copy(alpha = 0.2f)) {
                        Text(guide.scienceBadge, fontSize = 11.sp, color = guide.accentColor, fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                    }
                    Surface(shape = RoundedCornerShape(20.dp), color = BorderGlass) {
                        Text("⏱ ${guide.duration}", fontSize = 11.sp, color = TextWhite70,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text("Para: ${guide.targetCondition}", fontSize = 12.sp, color = TextWhite50)
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Guia paso a paso", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextWhite)
        Spacer(Modifier.height(12.dp))

        // Pasos
        guide.steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Linea conectora + icono
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp))
                            .background(guide.accentColor.copy(alpha = 0.15f))
                            .border(1.dp, guide.accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text(step.icon, fontSize = 18.sp) }
                    if (index < guide.steps.size - 1) {
                        Box(modifier = Modifier.width(2.dp).height(12.dp).background(guide.accentColor.copy(alpha = 0.2f)))
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(step.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = guide.accentColor)
                    Spacer(Modifier.height(4.dp))
                    Text(step.description, fontSize = 13.sp, color = TextWhite70, lineHeight = 20.sp)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Nota cientifica
        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                .background(guide.accentColor.copy(alpha = 0.08f))
                .border(1.dp, guide.accentColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔬", fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("Que dice la ciencia", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = guide.accentColor)
                }
                Spacer(Modifier.height(8.dp))
                Text(guide.scienceNote, fontSize = 12.sp, color = TextWhite70, lineHeight = 19.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        // Consejo practico
        Box(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                .background(AuroraStart.copy(alpha = 0.08f))
                .border(1.dp, AuroraStart.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💡", fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("Consejo practico", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AuroraStart)
                }
                Spacer(Modifier.height(8.dp))
                Text(guide.tip, fontSize = 12.sp, color = TextWhite70, lineHeight = 19.sp)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Aviso legal al final
        Text(
            "Este contenido es psicoeducativo. Si los sintomas persisten o son severos, busca atencion profesional. Linea 106 disponible 24 horas.",
            fontSize = 11.sp, color = TextWhite30, lineHeight = 17.sp
        )
    }
}