package com.example.checkncare.ui.language

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Simple mutable holder for the app-wide language preference.
 * true  = English
 * false = Tagalog
 */
class LanguageState {
    var isEnglish by mutableStateOf(true)
}

/** CompositionLocal so any composable can read/toggle the language. */
val LocalLanguage = compositionLocalOf { LanguageState() }
