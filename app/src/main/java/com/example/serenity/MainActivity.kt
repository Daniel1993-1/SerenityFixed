package com.example.serenity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.serenity.navigation.AppNavigation
import com.example.serenity.ui.theme.SerenityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SerenityTheme {
                // AppNavigation ya tiene su propio Scaffold con el BottomNav
                // NO envolver en otro Scaffold aquí
                AppNavigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}