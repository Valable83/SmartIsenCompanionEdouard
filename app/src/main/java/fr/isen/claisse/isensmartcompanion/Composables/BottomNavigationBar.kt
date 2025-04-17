package fr.isen.claisse.isensmartcompanion.Composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun BottomNavigationBar(
    currentScreen: String,
    onScreenSelected: (String) -> Unit
) {
    val context = LocalContext.current

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Accueil") },
            label = { Text("Accueil") },
            selected = currentScreen == "home",
            onClick = { onScreenSelected("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Event, contentDescription = "Événements") },
            label = { Text("Événements") },
            selected = currentScreen == "events",
            onClick = { onScreenSelected("events") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Agenda") },
            label = { Text("Agenda") },
            selected = currentScreen == "agenda",
            onClick = { onScreenSelected("agenda") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.History, contentDescription = "Historique") },
            label = { Text("Historique") },
            selected = currentScreen == "history",
            onClick = { onScreenSelected("history") }
        )
    }
}
