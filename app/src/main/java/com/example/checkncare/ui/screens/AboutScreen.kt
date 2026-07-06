package com.example.checkncare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.checkncare.R
import com.example.checkncare.ui.language.AppStrings
import com.example.checkncare.ui.language.LocalLanguage
import com.example.checkncare.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val lang    = LocalLanguage.current
    val strings = AppStrings(lang.isEnglish)

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
        containerColor = OffWhite
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Hero logo banner ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(PureWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(Brush.radialGradient(listOf(BrightRed, DeepRed))),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter            = painterResource(id = R.drawable.checkncare_icon),
                                contentDescription = "CheckNCare logo",
                                modifier           = Modifier.size(58.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "CheckNCare",
                        style      = MaterialTheme.typography.headlineMedium,
                        color      = PureWhite,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        strings.aboutAiTagline,
                        style     = MaterialTheme.typography.bodyMedium,
                        color     = PureWhite.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Column(
                modifier            = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AboutSectionCard(
                    icon    = Icons.Default.Info,
                    title   = strings.aboutPurposeTitle,
                    content = strings.aboutPurposeContent
                )

                AboutSectionCard(
                    icon    = Icons.Default.Build,
                    title   = strings.aboutTechTitle,
                    bullets = listOf(
                        "Kotlin & Jetpack Compose",
                        "TensorFlow Lite (MobileNetV3)",
                        "SQLite Database",
                        "CameraX",
                        "Microphone (Audio Recording)"
                    )
                )

                AboutSectionCard(
                    icon    = Icons.Default.Person,
                    title   = strings.aboutDevTitle,
                    content = strings.aboutDevContent,
                    bullets = listOf(
                        "Addam Geronga",
                        "Eitan Goyal",
                        "Floreine Santos",
                        "Jiro Yap"
                    )
                )
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = DividerGray, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(12.dp))

            // Version badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(LightGray)
                    .padding(horizontal = 20.dp, vertical = 7.dp)
            ) {
                Text(
                    strings.aboutVersion,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecond
                )
            }
            Spacer(Modifier.height(24.dp))
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
        colors    = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier          = Modifier.padding(18.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PaleRed),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary
                )
                Spacer(Modifier.height(6.dp))
                if (content.isNotBlank()) {
                    Text(content, style = MaterialTheme.typography.bodyMedium, color = TextSecond)
                }
                if (bullets.isNotEmpty()) {
                    bullets.forEach { bullet ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonRed)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(bullet, style = MaterialTheme.typography.bodyMedium, color = TextSecond)
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
