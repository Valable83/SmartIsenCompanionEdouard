package fr.isen.claisse.isensmartcompanion

// Import de toute les biblothèque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import fr.isen.claisse.isensmartcompanion.Composables.*
import fr.isen.claisse.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

// CLASS MAIN
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
 // MAIN APP AVEC TOUT LES ELEMENTS
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
                "agenda" -> AgendaScreen()
                "history" -> HistoryScreen()
            }
        }
    }
}
