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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.checkncare.ui.language.LanguageState
import com.example.checkncare.ui.language.LocalLanguage
import com.example.checkncare.ui.navigation.Screen
import com.example.checkncare.ui.screens.*
import com.example.checkncare.ui.theme.CheckNCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckNCareTheme {
                // Single LanguageState instance for the entire app lifetime
                val languageState = remember { LanguageState() }
                CompositionLocalProvider(LocalLanguage provides languageState) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color    = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route)           { HomeScreen(navController) }
        composable(Screen.AudioDetection.route) { AudioDetectionScreen(navController) }
        composable(Screen.FecalDetection.route) { FecalDetectionScreen(navController) }
        composable(Screen.History.route)        { HistoryScreen(navController) }
        composable(Screen.About.route)          { AboutScreen(navController) }
    }
}
