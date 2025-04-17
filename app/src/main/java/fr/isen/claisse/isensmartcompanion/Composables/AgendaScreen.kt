package fr.isen.claisse.isensmartcompanion.Composables

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.claisse.isensmartcompanion.api.RetrofitInstance

import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun AgendaScreen() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("event_prefs", Context.MODE_PRIVATE)
    var favoriteEvents by remember { mutableStateOf(listOf<Event>()) }
    val studentCourses = remember { mockCourses() }
    var temperature by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {


        RetrofitInstance.api.getEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    favoriteEvents = response.body()?.filter {
                        prefs.getBoolean("event_${it.id}_subscribed", false)
                    } ?: emptyList()
                }
            }
            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                favoriteEvents = emptyList()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Agenda de l'étudiant", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            temperature?.let {
                Text("$it - Toulon", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            if (favoriteEvents.isNotEmpty()) {
                item {
                    Text("✨ Événements favoris", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(favoriteEvents) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(event.title, fontWeight = FontWeight.Bold)
                            Text(event.date)
                            Text(event.location)
                        }
                    }
                }
            }

            if (studentCourses.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🎓 Cours", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(studentCourses) { course ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(course.title, fontWeight = FontWeight.Bold)
                            Text("Prof : ${course.teacher}")
                            Text("Heure : ${course.time} - Salle : ${course.room}")
                        }
                    }
                }
            }
        }
    }
}

data class Course(
    val id: Int,
    val title: String,
    val time: String,
    val room: String,
    val teacher: String
)

fun mockCourses(): List<Course> {
    return listOf(
        Course(1, "Maths avancées", "08h30 - 10h00", "B203", "M. Dupont"),
        Course(2, "IoT", "10h15 - 12h00", "C105", "Mme. Bernard"),
        Course(3, "Systèmes embarqués", "13h30 - 15h00", "D301", "Dr. Martin")
    )
}
