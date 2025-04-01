package fr.isen.colard.isensmartcompanion.Composables

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.colard.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Récupère l'objet Event envoyé via l'intent
        val event = intent.getParcelableExtra<Event>("event")

        if (event == null) {
            Toast.makeText(this, "Aucun événement reçu", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ✅ Affiche l'écran avec les détails de l'événement
        setContent {
            ISENSmartCompanionTheme {
                EventDetailScreen(event)
            }
        }
    }
}

@Composable
fun EventDetailScreen(event: Event) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Détails de l'événement", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        Text("Titre : ${event.title}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Date : ${event.date}", fontSize = 18.sp)
        Text("Lieu : ${event.location}", fontSize = 18.sp)
        Text("Catégorie : ${event.category}", fontSize = 18.sp)
        Text("Description :", fontWeight = FontWeight.SemiBold)
        Text(event.description)
    }
}
