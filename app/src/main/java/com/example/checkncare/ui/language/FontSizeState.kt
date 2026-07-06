package com.example.checkncare.ui.language

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppFontSize(val scale: Float, val labelEn: String, val labelTl: String) {
    SMALL      (0.85f, "Small",       "Maliit"    ),
    MEDIUM     (1.00f, "Medium",      "Katamtaman"),
    LARGE      (1.15f, "Large",       "Malaki"    ),
    EXTRA_LARGE(1.30f, "Extra Large", "Napakalaki")
}

class FontSizeState {
    var current by mutableStateOf(AppFontSize.MEDIUM)
}

val LocalFontSize = compositionLocalOf { FontSizeState() }
