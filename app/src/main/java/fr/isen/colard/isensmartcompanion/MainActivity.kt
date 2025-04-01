package fr.isen.colard.isensmartcompanion

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.colard.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import kotlinx.parcelize.Parcelize
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import android.util.Log
import fr.isen.colard.isensmartcompanion.RetrofitInstance


// ✅ Classe Event complète + Parcelable
@Parcelize
data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Parcelable

// ✅ Interface API Retrofit
interface EventApiService {
    @GET("events.json")
    fun getEvents(): Call<List<Event>>
}

// ✅ Objet Retrofit
object RetrofitInstance {
    private const val BASE_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

    val api: EventApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApiService::class.java)
    }
}

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
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Accueil") },
                    label = { Text("Accueil") },
                    selected = currentScreen == "home",
                    onClick = { currentScreen = "home" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Event, contentDescription = "Événements") },
                    label = { Text("Événements") },
                    selected = currentScreen == "events",
                    onClick = { currentScreen = "events" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Agenda") },
                    label = { Text("Agenda") },
                    selected = false,
                    onClick = {
                        Toast.makeText(context, "À venir", Toast.LENGTH_SHORT).show()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.History, contentDescription = "Historique") },
                    label = { Text("Historique") },
                    selected = false,
                    onClick = {
                        Toast.makeText(context, "À venir", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "home" -> HomeScreen()
                "events" -> EventsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    var question by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ISEN", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Smart Companion", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
        }

        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            placeholder = { Text("Posez votre question") },
            trailingIcon = {
                IconButton(onClick = {
                    Toast.makeText(context, "Question envoyée", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Filled.ArrowForward, contentDescription = "Envoyer", tint = Color.Red)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun EventsScreen() {
    val context = LocalContext.current
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // 🔁 Lance la requête une seule fois
    LaunchedEffect(Unit) {
        Log.d("EventsScreen", "🔄 Lancement de la requête Retrofit...")
        RetrofitInstance.api.getEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                Log.d("EventsScreen", "✅ Réponse reçue : code = ${response.code()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("EventsScreen", "📦 ${body?.size ?: 0} événements récupérés depuis l'API")
                    events = body ?: emptyList()
                } else {
                    Log.e("EventsScreen", "❌ Erreur serveur : code ${response.code()} - ${response.message()}")
                    Toast.makeText(context, "Erreur serveur", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.e("EventsScreen", "🚨 Erreur réseau : ${t.localizedMessage}")
                Toast.makeText(context, "Erreur réseau", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Événements", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn {
                items(events) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                Log.d("EventsScreen", "🧭 Clic sur événement : ${event.title}")
                                val intent = Intent(context, EventDetailActivity::class.java)
                                intent.putExtra("event", event)
                                context.startActivity(intent)
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFECECEC))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(event.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(event.date, fontSize = 14.sp)
                            Text(event.location, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}


class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val event = intent.getParcelableExtra<Event>("event")

        if (event == null) {
            Toast.makeText(this, "Erreur : aucun événement reçu", Toast.LENGTH_SHORT).show()
            finish() // ferme proprement l'activité
            return
        }

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
        Text("Titre : ${event.title}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Date : ${event.date}", fontSize = 18.sp)
        Text("Lieu : ${event.location}", fontSize = 18.sp)
        Text("Catégorie : ${event.category}", fontSize = 18.sp)
        Text("Description :", fontWeight = FontWeight.SemiBold)
        Text(event.description)
    }
}
