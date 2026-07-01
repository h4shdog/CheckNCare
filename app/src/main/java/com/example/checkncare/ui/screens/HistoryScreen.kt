package com.example.checkncare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.checkncare.data.PredictionRecord
import com.example.checkncare.ui.language.AppStrings
import com.example.checkncare.ui.language.LocalLanguage
import com.example.checkncare.ui.theme.*
import com.example.checkncare.ui.viewmodels.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel    : HistoryViewModel = viewModel()
) {
    val records      by viewModel.records.collectAsState(initial = emptyList())
    var searchQuery  by remember { mutableStateOf("") }
    var showClearDlg by remember { mutableStateOf(false) }

    val lang    = LocalLanguage.current
    val strings = AppStrings(lang.isEnglish)

    if (showClearDlg) {
        AlertDialog(
            onDismissRequest = { showClearDlg = false },
            title  = { Text(strings.historyClearTitle) },
            text   = { Text(strings.historyClearBody) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearAll(); showClearDlg = false },
                    colors  = ButtonDefaults.textButtonColors(contentColor = ErrorRed)
                ) { Text(strings.historyClearConfirm, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDlg = false }) {
                    Text(strings.historyClearCancel)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.historyScreenTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (records.isNotEmpty()) {
                        IconButton(onClick = { showClearDlg = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear All")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor             = CrimsonRed,
                    titleContentColor          = PureWhite,
                    navigationIconContentColor = PureWhite,
                    actionIconContentColor     = PureWhite
                )
            )
        },
        containerColor = OffWhite
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Search bar ────────────────────────────────────────────
            OutlinedTextField(
                value         = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.search(it)
                },
                modifier    = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                placeholder = { Text(strings.historySearch) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = CrimsonRed)
                },
                shape  = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = CrimsonRed,
                    unfocusedBorderColor    = MidGray,
                    focusedContainerColor   = PureWhite,
                    unfocusedContainerColor = PureWhite
                ),
                singleLine = true
            )

            // ── Summary chip ──────────────────────────────────────────
            if (records.isNotEmpty()) {
                val label = if (records.size == 1) strings.historyRecordSuffix
                            else strings.historyRecordsSuffix
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(50))
                        .background(SoftRed)
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text(
                        "${records.size} $label",
                        style      = MaterialTheme.typography.labelMedium,
                        color      = CrimsonRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            if (records.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint     = MidGray
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            strings.historyEmpty,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecond
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier            = Modifier.fillMaxSize(),
                    contentPadding      = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(records) { record ->
                        HistoryCard(
                            record    = record,
                            strings   = strings,
                            onDelete  = { viewModel.delete(record) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(
    record  : PredictionRecord,
    strings : AppStrings,
    onDelete: () -> Unit
) {
    val isAudio     = record.type == "Audio"
    val typeColor   = if (isAudio) CrimsonRed else Color(0xFF795548)
    val typeBg      = if (isAudio) SoftRed    else Color(0xFFEFEBE9)
    val typeIcon    = if (isAudio) Icons.Default.Mic else Icons.Default.CameraAlt
    val isNormal    = record.result.equals("Normal", ignoreCase = true)
    val resultColor = if (isNormal) SuccessGreen else ErrorRed

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Type icon badge
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(typeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(typeIcon, contentDescription = null, tint = typeColor, modifier = Modifier.size(24.dp))
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier              = Modifier.fillMaxWidth()
                ) {
                    Text(
                        record.type,
                        style      = MaterialTheme.typography.labelMedium,
                        color      = typeColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "${record.date}  ${record.time}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextHint
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    record.result,
                    style      = MaterialTheme.typography.titleMedium,
                    color      = resultColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(4.dp))

            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MidGray)
            }
        }
    }
}
