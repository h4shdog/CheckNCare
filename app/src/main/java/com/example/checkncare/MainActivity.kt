package com.example.checkncare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.checkncare.data.AppPreferences
import com.example.checkncare.ui.language.FontSizeState
import com.example.checkncare.ui.language.LanguageState
import com.example.checkncare.ui.language.LocalFontSize
import com.example.checkncare.ui.language.LocalLanguage
import com.example.checkncare.ui.language.LocalTheme
import com.example.checkncare.ui.language.ThemeState
import com.example.checkncare.ui.navigation.Screen
import com.example.checkncare.ui.screens.*
import com.example.checkncare.ui.theme.CheckNCareTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context       = LocalContext.current
            val languageState = remember { LanguageState() }
            val fontSizeState = remember { FontSizeState() }
            val themeState    = remember { ThemeState() }

            // Load the persisted dark mode preference on first launch
            LaunchedEffect(Unit) {
                themeState.isDark = AppPreferences.isDarkMode(context).first()
            }

            // Toggle callback — defined inside setContent so themeState is accessible,
            // but uses the activity's lifecycleScope which is always in scope here
            val onThemeToggle: () -> Unit = {
                themeState.isDark = !themeState.isDark
                lifecycleScope.launch {
                    AppPreferences.setDarkMode(context, themeState.isDark)
                }
            }

            CompositionLocalProvider(
                LocalLanguage provides languageState,
                LocalFontSize  provides fontSizeState,
                LocalTheme     provides themeState
            ) {
                CheckNCareTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color    = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation(onThemeToggle = onThemeToggle)
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(onThemeToggle: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route)           { HomeScreen(navController, onThemeToggle) }
        composable(Screen.AudioDetection.route) { AudioDetectionScreen(navController) }
        composable(Screen.FecalDetection.route) { FecalDetectionScreen(navController) }
        composable(Screen.History.route)        { HistoryScreen(navController) }
        composable(Screen.About.route)          { AboutScreen(navController) }
    }
}
