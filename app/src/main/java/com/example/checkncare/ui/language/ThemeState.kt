package com.example.checkncare.ui.language

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ThemeState {
    var isDark by mutableStateOf(false)
}

val LocalTheme = compositionLocalOf { ThemeState() }
