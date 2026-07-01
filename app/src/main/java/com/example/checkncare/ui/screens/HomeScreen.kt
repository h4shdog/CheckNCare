package com.example.checkncare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.checkncare.R
import com.example.checkncare.ui.language.AppStrings
import com.example.checkncare.ui.language.LocalLanguage
import com.example.checkncare.ui.navigation.Screen
import com.example.checkncare.ui.theme.*

@Composable
fun HomeScreen(navController: NavController) {
    val lang    = LocalLanguage.current
    val strings = AppStrings(lang.isEnglish)

    val features = listOf(
        FeatureItem(strings.featureAudioTitle,   strings.featureAudioDesc,   Icons.Default.Mic,       Screen.AudioDetection.route, CrimsonRed),
        FeatureItem(strings.featureFecalTitle,   strings.featureFecalDesc,   Icons.Default.CameraAlt, Screen.FecalDetection.route, DeepRed),
        FeatureItem(strings.featureHistoryTitle, strings.featureHistoryDesc, Icons.Default.History,   Screen.History.route,        Color(0xFF795548)),
        FeatureItem(strings.featureAboutTitle,   strings.featureAboutDesc,   Icons.Default.Info,      Screen.About.route,          Color(0xFF546E7A))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        LogoHeader(strings = strings, isEnglish = lang.isEnglish, onToggle = { lang.isEnglish = !lang.isEnglish })

        LazyVerticalGrid(
            columns               = GridCells.Fixed(2),
            contentPadding        = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement   = Arrangement.spacedBy(16.dp),
            modifier              = Modifier.weight(1f)
        ) {
            items(features) { feature ->
                FeatureCard(feature) { navController.navigate(feature.route) }
            }
        }

        Text(
            text      = strings.footerLabel,
            style     = MaterialTheme.typography.labelSmall,
            color     = TextHint,
            modifier  = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Header / Logo
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun LogoHeader(strings: AppStrings, isEnglish: Boolean, onToggle: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
    ) {
        // Subtle decorative circles
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 70.dp, y = (-70).dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(PureWhite.copy(alpha = 0.06f))
        )
        Box(
            modifier = Modifier
                .size(130.dp)
                .offset(x = (-40).dp, y = 50.dp)
                .align(Alignment.BottomStart)
                .clip(CircleShape)
                .background(PureWhite.copy(alpha = 0.04f))
        )

        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Logo badge ──────────────────────────────────────────
            Image(
                painter            = painterResource(id = R.drawable.checkncare_icon),
                contentDescription = "CheckNCare logo",
                modifier           = Modifier
                    .size(100.dp)
                    .shadow(16.dp, RoundedCornerShape(20.dp), clip = false)
                    .clip(RoundedCornerShape(20.dp))
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text  = "CheckNCare",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight    = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                ),
                color = PureWhite
            )

            Spacer(Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(PureWhite.copy(alpha = 0.18f))
                    .padding(horizontal = 16.dp, vertical = 5.dp)
            ) {
                Text(
                    text      = strings.appTagline,
                    style     = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                    color     = PureWhite.copy(alpha = 0.92f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Language Toggle ─────────────────────────────────────
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(PureWhite.copy(alpha = 0.15f))
                    .clickable { onToggle() }
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // English pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isEnglish) PureWhite else Color.Transparent
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = "English",
                            style      = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isEnglish) FontWeight.Bold else FontWeight.Normal,
                            color      = if (isEnglish) CrimsonRed else PureWhite.copy(alpha = 0.80f)
                        )
                    }
                    // Divider
                    Text(
                        text  = "/",
                        style = MaterialTheme.typography.labelMedium,
                        color = PureWhite.copy(alpha = 0.60f),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                    // Tagalog pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (!isEnglish) PureWhite else Color.Transparent
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = "Tagalog",
                            style      = MaterialTheme.typography.labelMedium,
                            fontWeight = if (!isEnglish) FontWeight.Bold else FontWeight.Normal,
                            color      = if (!isEnglish) CrimsonRed else PureWhite.copy(alpha = 0.80f)
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Data + Card
// ─────────────────────────────────────────────────────────────────────────────
data class FeatureItem(
    val title      : String,
    val description: String,
    val icon       : ImageVector,
    val route      : String,
    val accent     : Color = CrimsonRed
)

@Composable
fun FeatureCard(feature: FeatureItem, onClick: () -> Unit) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .aspectRatio(0.88f)
            .clickable(onClick = onClick),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier         = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(feature.accent.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = feature.icon,
                    contentDescription = null,
                    tint               = feature.accent,
                    modifier           = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text       = feature.title,
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center,
                color      = TextPrimary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text      = feature.description,
                style     = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                textAlign = TextAlign.Center,
                color     = TextSecond
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(feature.accent)
            )
        }
    }
}
