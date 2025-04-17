package fr.isen.colard.isensmartcompanion.Composables

import android.content.Intent
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import fr.isen.colard.isensmartcompanion.api.RetrofitInstance
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Parcelize
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Parcelable

@Composable
fun EventsScreen() {
    val context = LocalContext.current
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        RetrofitInstance.api.getEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    events = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "Erreur serveur", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Toast.makeText(context, "Erreur réseau", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
