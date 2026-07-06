package com.example.checkncare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.checkncare.R
import com.example.checkncare.ui.language.AppStrings
import com.example.checkncare.ui.language.LocalFontSize
import com.example.checkncare.ui.language.LocalLanguage
import com.example.checkncare.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val lang      = LocalLanguage.current
    val fontState = LocalFontSize.current
    val strings   = AppStrings(lang.isEnglish)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.aboutScreenTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor             = CrimsonRed,
                    titleContentColor          = PureWhite,
                    navigationIconContentColor = PureWhite
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier            = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Hero banner ───────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(72.dp).clip(CircleShape).background(PureWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.size(58.dp).clip(CircleShape)
                                .background(Brush.radialGradient(listOf(BrightRed, DeepRed))),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(painterResource(id = R.drawable.checkncare_icon),
                                contentDescription = "CheckNCare logo", modifier = Modifier.size(58.dp))
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("CheckNCare", style = MaterialTheme.typography.headlineMedium,
                        color = PureWhite, fontWeight = FontWeight.ExtraBold)
                    Text(strings.aboutAiTagline, style = MaterialTheme.typography.bodyMedium,
                        color = PureWhite.copy(alpha = 0.85f), textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp))
                }
            }

            Spacer(Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {

                AboutSectionCard(icon = Icons.Default.Info,
                    title   = strings.aboutPurposeTitle,
                    content = strings.aboutPurposeContent)

                AboutSectionCard(icon = Icons.Default.Build,
                    title   = strings.aboutTechTitle,
                    bullets = listOf(
                        "Kotlin & Jetpack Compose",
                        "TensorFlow Lite (MobileNetV3)",
                        "SQLite Database",
                        "Camera (Image Capturing)",
                        "Microphone (Audio Recording)"
                    ))

                // ── Font Size Slider card ─────────────────────────────
                FontSizeCard(strings = strings, fontState = fontState)

                AboutSectionCard(icon = Icons.Default.Person,
                    title   = strings.aboutDevTitle,
                    content = strings.aboutDevContent,
                    bullets = listOf("Addam Geronga", "Eitan Goyal", "Floreine Santos", "Jiro Yap"))
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 20.dp, vertical = 7.dp)
            ) {
                Text(strings.aboutVersion, style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun FontSizeCard(
    strings  : AppStrings,
    fontState: com.example.checkncare.ui.language.FontSizeState
) {
    val sizeSp = fontState.sizeSp.toInt()

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // ── Header row ────────────────────────────────────────────
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(listOf(CrimsonRed, DeepRed))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.TextFields,
                        contentDescription = null,
                        tint     = PureWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = strings.fontSizeLabel,
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text  = "Drag to adjust text size",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Size pill — glows with gradient
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(listOf(CrimsonRed, DeepRed))
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text       = "$sizeSp sp",
                        style      = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color      = PureWhite
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Live preview box ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text      = "Aa — CheckNCare",
                    style     = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = fontState.sizeSp.sp
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface,
                    textAlign  = TextAlign.Center
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Min / Max row ─────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text  = "A",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text  = "A",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 22.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }

            // ── Slider ────────────────────────────────────────────────
            Slider(
                value         = fontState.sizeSp,
                onValueChange = { fontState.sizeSp = it },
                valueRange    = 1f..50f,
                steps         = 48,
                modifier      = Modifier.fillMaxWidth(),
                colors        = SliderDefaults.colors(
                    thumbColor            = CrimsonRed,
                    activeTrackColor      = CrimsonRed,
                    inactiveTrackColor    = MaterialTheme.colorScheme.outlineVariant,
                    activeTickColor       = CrimsonRed.copy(alpha = 0f),
                    inactiveTickColor     = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0f)
                )
            )

            // ── Preset quick-select chips ─────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(10 to "S", 14 to "M", 20 to "L", 28 to "XL").forEach { (size, label) ->
                    val isActive = sizeSp == size
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isActive)
                                    Brush.horizontalGradient(listOf(CrimsonRed, DeepRed))
                                else
                                    Brush.horizontalGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                            )
                            .clickable { fontState.sizeSp = size.toFloat() }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = label,
                            style      = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color      = if (isActive) PureWhite
                                         else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AboutSectionCard(
    icon   : ImageVector,
    title  : String,
    content: String       = "",
    bullets: List<String> = emptyList()
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(6.dp))
                if (content.isNotBlank()) {
                    Text(content, style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (bullets.isNotEmpty()) {
                    bullets.forEach { bullet ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(CrimsonRed))
                            Spacer(Modifier.width(8.dp))
                            Text(bullet, style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
