package com.example.checkncare.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AudioDetection : Screen("audio_detection")
    object FecalDetection : Screen("fecal_detection")
    object History : Screen("history")
    object About : Screen("about")
}
