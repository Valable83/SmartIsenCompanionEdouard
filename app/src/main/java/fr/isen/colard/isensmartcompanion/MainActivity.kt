package fr.isen.colard.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import fr.isen.colard.isensmartcompanion.Composables.BottomNavigationBar
import fr.isen.colard.isensmartcompanion.Composables.EventsScreen
import fr.isen.colard.isensmartcompanion.Composables.MainScreen
import fr.isen.colard.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ISENSmartCompanionTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf("home") }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onScreenSelected = { selectedScreen -> currentScreen = selectedScreen }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "home" -> MainScreen()
                "events" -> EventsScreen()
                // "agenda" -> ... à venir
                // "history" -> ... à venir
            }
        }
    }
}
