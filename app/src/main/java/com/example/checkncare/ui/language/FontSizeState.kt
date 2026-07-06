package com.example.checkncare.ui.language

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue

/**
 * Holds the user-selected base font size in sp (range 1–50).
 * Default is 14sp which matches the app's original bodyMedium size.
 * Theme.kt scales the full typography proportionally from this value.
 */
class FontSizeState {
    var sizeSp by mutableFloatStateOf(14f)
}

val LocalFontSize = compositionLocalOf { FontSizeState() }
