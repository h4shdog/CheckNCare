package com.example.checkncare.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.checkncare.ui.language.AppStrings
import com.example.checkncare.ui.language.LocalLanguage
import com.example.checkncare.ui.theme.*
import com.example.checkncare.ui.viewmodels.AudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioDetectionScreen(
    navController: NavController,
    viewModel: AudioViewModel = viewModel()
) {
    val state   by viewModel.state.collectAsState()
    val context = LocalContext.current
    val lang    = LocalLanguage.current
    val strings = AppStrings(lang.isEnglish)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.startRecording()
        else Toast.makeText(context, strings.permMicRequired, Toast.LENGTH_SHORT).show()
    }

    fun onMicButtonClicked() {
        if (state.isRecording) {
            viewModel.stopRecording()
        } else {
            val granted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PermissionChecker.PERMISSION_GRANTED
            if (granted) viewModel.startRecording()
            else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.audioScreenTitle, fontWeight = FontWeight.Bold) },
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Info card ─────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier          = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = null,
                            tint     = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text  = strings.audioInfoText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Recording status label ────────────────────────────────
            AnimatedVisibility(visible = state.isRecording) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text       = strings.audioRecordingLabel,
                        style      = MaterialTheme.typography.labelLarge,
                        color      = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // ── Analyzing indicator ───────────────────────────────────
            AnimatedVisibility(visible = state.isAnalyzing) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier            = Modifier.padding(vertical = 8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(48.dp),
                        color       = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        strings.audioAnalyzingLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Mic / Stop button ─────────────────────────────────────
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
                if (state.isRecording) {
                    CircularProgressIndicator(
                        modifier    = Modifier.fillMaxSize(),
                        strokeWidth = 6.dp,
                        color       = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                    )
                }
                FloatingActionButton(
                    onClick        = { onMicButtonClicked() },
                    modifier       = Modifier.size(120.dp),
                    shape          = CircleShape,
                    containerColor = if (state.isRecording) MaterialTheme.colorScheme.error
                                     else MaterialTheme.colorScheme.primary,
                    elevation      = FloatingActionButtonDefaults.elevation(
                        defaultElevation = if (state.isRecording) 2.dp else 10.dp
                    )
                ) {
                    Icon(
                        imageVector        = if (state.isRecording) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = if (state.isRecording) "Stop" else "Record",
                        modifier           = Modifier.size(52.dp),
                        tint               = PureWhite
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text  = if (state.isRecording) strings.audioTapToStop else strings.audioTapToRecord,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            // ── Too-short warning ─────────────────────────────────────
            AnimatedVisibility(
                visible = state.recordingTooShort,
                enter   = fadeIn() + expandVertically()
            ) {
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier          = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text     = "⚠",
                            style    = MaterialTheme.typography.titleLarge,
                            color    = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text       = strings.audioTooShort,
                            style      = MaterialTheme.typography.bodyMedium,
                            color      = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.SemiBold,
                            textAlign  = TextAlign.Start,
                            modifier   = Modifier.weight(1f)
                        )
                    }
                }
            }

            // ── Result ────────────────────────────────────────────────
            AnimatedVisibility(
                visible = state.result != null,
                enter   = fadeIn() + expandVertically()
            ) {
                state.result?.let {
                    PredictionResultCard(
                        label            = it.label,
                        recommendationEn = it.recommendationEn,
                        recommendationTl = it.recommendationTl,
                        isEnglish        = lang.isEnglish,
                        strings          = strings
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Shared prediction result card (used by Audio & Fecal screens)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PredictionResultCard(
    label           : String,
    recommendationEn: String,
    recommendationTl: String,
    isEnglish       : Boolean,
    strings         : AppStrings,
    descriptionEn   : String       = "",
    descriptionTl   : String       = "",
    signsEn         : List<String> = emptyList(),
    signsTl         : List<String> = emptyList(),
    detailedRecsEn  : List<String> = emptyList(),
    detailedRecsTl  : List<String> = emptyList(),
    preventionEn    : List<String> = emptyList(),
    preventionTl    : List<String> = emptyList(),
    treatmentEn     : List<String> = emptyList(),
    treatmentTl     : List<String> = emptyList()
) {
    val isNormal    = label.equals("Normal", ignoreCase = true)
    val isUnknown   = label.equals("Unknown", ignoreCase = true)
    val statusColor = when {
        isNormal  -> SuccessGreen
        isUnknown -> MaterialTheme.colorScheme.onSurfaceVariant
        else      -> MaterialTheme.colorScheme.error
    }
    val statusBg = when {
        isNormal  -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        isUnknown -> MaterialTheme.colorScheme.surfaceVariant
        else      -> MaterialTheme.colorScheme.errorContainer
    }
    val statusEmoji = when {
        isNormal  -> "✓"
        isUnknown -> "?"
        else      -> "⚠"
    }

    val recommendation = if (isEnglish) recommendationEn else recommendationTl
    val description    = if (isEnglish) descriptionEn    else descriptionTl
    val signs          = if (isEnglish) signsEn          else signsTl
    val detailedRecs   = if (isEnglish) detailedRecsEn   else detailedRecsTl
    val prevention     = if (isEnglish) preventionEn     else preventionTl
    val treatment      = if (isEnglish) treatmentEn      else treatmentTl

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // ── Status banner ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(statusBg)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (!isUnknown) {
                        Text(
                            text       = if (isEnglish) "Possibility:  " else "Posibilidad:  ",
                            style      = MaterialTheme.typography.titleMedium,
                            color      = statusColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text       = label,
                        style      = MaterialTheme.typography.titleMedium,
                        color      = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text  = statusEmoji,
                        style = MaterialTheme.typography.titleMedium,
                        color = statusColor
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (description.isNotBlank()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color    = MaterialTheme.colorScheme.outlineVariant
                )
                Text(
                    text       = strings.resultDescription,
                    style      = MaterialTheme.typography.titleMedium,
                    color      = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text      = description,
                    style     = MaterialTheme.typography.bodyMedium,
                    color     = MaterialTheme.colorScheme.onSurface
                )
            }

            if (signs.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color    = MaterialTheme.colorScheme.outlineVariant
                )
                Text(
                    text       = strings.resultClinicalSigns,
                    style      = MaterialTheme.typography.titleMedium,
                    color      = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                signs.forEach { sign ->
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 3.dp)) {
                        Text("•", style = MaterialTheme.typography.bodyMedium, color = statusColor,
                            fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp, top = 1.dp))
                        Text(
                            text       = buildBoldSign(sign),
                            style      = MaterialTheme.typography.bodyMedium,
                            color      = MaterialTheme.colorScheme.onSurface,
                            modifier   = Modifier.weight(1f)
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text       = strings.resultRecommendation,
                style      = MaterialTheme.typography.titleMedium,
                color      = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            if (detailedRecs.isNotEmpty()) {
                detailedRecs.forEachIndexed { index, rec ->
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 3.dp)) {
                        Text("${index + 1}.", style = MaterialTheme.typography.bodyMedium, color = statusColor,
                            fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp).padding(top = 1.dp))
                        Text(rec, style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                    }
                }
            } else {
                Text(recommendation, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface)
            }

            if (prevention.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                Text(strings.resultPrevention, style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                prevention.forEachIndexed { index, item ->
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 3.dp)) {
                        Text("${index + 1}.", style = MaterialTheme.typography.bodyMedium, color = statusColor,
                            fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp).padding(top = 1.dp))
                        Text(item, style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                    }
                }
            }

            if (treatment.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                Text(strings.resultTreatment, style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                treatment.forEachIndexed { index, item ->
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 3.dp)) {
                        Text("${index + 1}.", style = MaterialTheme.typography.bodyMedium, color = statusColor,
                            fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp).padding(top = 1.dp))
                        Text(item, style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Bold-highlight specific phrases inside clinical sign strings.
// Add the phrase (or its start) to the set below to bold it.
// ─────────────────────────────────────────────────────────────────────────────
private val boldSignPhrases = setOf(
    // English
    "Bloody or watery droppings",
    "White, pasty diarrhea sticking to the vent area",
    "Greenish, watery droppings",
    // Tagalog equivalents
    "Madugo o matubig na dumi",
    "Puting at malagkit na dumi na dumidikit sa puwitan",
    "Maberde at matubig na dumi"
)

private fun buildBoldSign(sign: String): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        val matchedPhrase = boldSignPhrases.firstOrNull { phrase ->
            sign.contains(phrase, ignoreCase = true)
        }
        if (matchedPhrase != null) {
            val start = sign.indexOf(matchedPhrase, ignoreCase = true)
            val end   = start + matchedPhrase.length
            append(sign.substring(0, start))
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(sign.substring(start, end))
            }
            append(sign.substring(end))
        } else {
            append(sign)
        }
    }
}
