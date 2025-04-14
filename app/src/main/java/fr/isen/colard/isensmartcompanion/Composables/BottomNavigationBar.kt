package fr.isen.colard.isensmartcompanion.Composables

import android.widget.Toast
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
        // Onglet Accueil
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Accueil") },
            label = { Text("Accueil") },
            selected = currentScreen == "home",
            onClick = { onScreenSelected("home") }
        )

        // Onglet Événements
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Event, contentDescription = "Événements") },
            label = { Text("Événements") },
            selected = currentScreen == "events",
            onClick = { onScreenSelected("events") }
        )

        // Onglet Agenda (à venir)
        NavigationBarItem(
            icon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Agenda") },
            label = { Text("Agenda") },
            selected = false,
            onClick = { Toast.makeText(context, "À venir", Toast.LENGTH_SHORT).show() }
        )

        // Onglet Historique
        NavigationBarItem(
            icon = { Icon(Icons.Filled.History, contentDescription = "Historique") },
            label = { Text("Historique") },
            selected = currentScreen == "history",  // Sélectionner l'onglet Historique si l'écran actuel est "history"
            onClick = { onScreenSelected("history") } // Afficher l'écran Historique
        )
    }
}
