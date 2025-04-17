package fr.isen.claisse.isensmartcompanion.Composables

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import fr.isen.claisse.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class EventDetailActivity : ComponentActivity() {

    private lateinit var event: Event

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(this, "Permission de notification refusée", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        event = intent.getParcelableExtra("event") ?: run {
            Toast.makeText(this, "Aucun événement reçu", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
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
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("event_prefs", Context.MODE_PRIVATE)
    var isSubscribed by remember {
        mutableStateOf(prefs.getBoolean("event_${event.id}_subscribed", false))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Détails de l'événement", style = MaterialTheme.typography.titleLarge)

            IconButton(onClick = {
                isSubscribed = !isSubscribed
                prefs.edit().putBoolean("event_${event.id}_subscribed", isSubscribed).apply()

                Toast.makeText(
                    context,
                    if (isSubscribed) "Notification activée" else "Notification désactivée",
                    Toast.LENGTH_SHORT
                ).show()

                if (isSubscribed) {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        !alarmManager.canScheduleExactAlarms()
                    ) {
                        val settingsIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        context.startActivity(settingsIntent)
                        return@IconButton
                    }

                    val intent = Intent(context, ReminderReceiver::class.java).apply {
                        putExtra("event_title", event.title)
                    }

                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        event.id.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    val triggerAt = System.currentTimeMillis() + 10_000

                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        triggerAt,
                        pendingIntent
                    )
                }
            }) {
                Icon(
                    imageVector = if (isSubscribed) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                    contentDescription = "Rappel",
                    tint = if (isSubscribed) Color.Red else Color.Gray
                )
            }
        }

        Text("Titre : ${event.title}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Date : ${event.date}", fontSize = 18.sp)
        Text("Lieu : ${event.location}", fontSize = 18.sp)
        Text("Catégorie : ${event.category}", fontSize = 18.sp)
        Text("Description :", fontWeight = FontWeight.SemiBold)
        Text(event.description)
    }
}
