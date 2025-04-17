package fr.isen.claisse.isensmartcompanion.Composables

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.claisse.isensmartcompanion.api.Gemini
import fr.isen.claisse.isensmartcompanion.data.AppDatabase
import fr.isen.claisse.isensmartcompanion.data.Interaction
import kotlinx.coroutines.launch

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var question by remember { mutableStateOf("") }
    var responses by remember { mutableStateOf(listOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("ISEN", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                Text("Smart Companion", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                items(responses) { response ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3))
                    ) {
                        Text(
                            text = response,
                            modifier = Modifier.padding(12.dp),
                            color = Color.Black
                        )
                    }
                }
            }

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = question,
                onValueChange = { question = it },
                placeholder = { Text("Posez votre question") },
                trailingIcon = {
                    IconButton(onClick = {
                        if (question.isNotBlank()) {
                            scope.launch {
                                isLoading = true
                                val response = Gemini.getGeminiResponse(question)
                                if (response != null) {
                                    responses = responses + "Q : $question\nA : $response"
                                    val interaction = Interaction(
                                        question = question,
                                        response = response,
                                        timestamp = System.currentTimeMillis()
                                    )
                                    db.interactionDao().insertInteraction(interaction)
                                    question = ""
                                } else {
                                    Toast.makeText(context, "Erreur IA", Toast.LENGTH_SHORT).show()
                                }
                                isLoading = false
                            }
                        } else {
                            Toast.makeText(context, "Veuillez écrire une question", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Envoyer", tint = Color.Red)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
