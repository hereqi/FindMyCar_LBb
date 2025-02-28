package com.example.findmycar_lb_b

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = DatabaseHelper(this)
        val parkingHistory = dbHelper.getParkingHistory()

        Log.d(
            "DEBUG", "🛠️ Anzahl Parkplätze aus DB: ${parkingHistory.size}" //Chatgpt generiert
        )
        setContent {
            ParkingHistoryScreen(parkingHistory) { latitude, longitude ->
                openGoogleMaps(latitude, longitude)
            }
        }
    }


    private fun openGoogleMaps(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(Parkplatz)")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
}

@Composable
fun ParkingHistoryScreen(
    history: List<Triple<Double, Double, String>>, onClick: (Double, Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Gespeicherte Parkplätze", style = MaterialTheme.typography.headlineMedium)

        if (history.isEmpty()) {
            Text(text = "Keine gespeicherten Parkplätze.", modifier = Modifier.padding(top = 20.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(history) { (lat, lon, date) ->
                    ParkingItem(lat, lon, date, onClick)
                }
            }
        }
    }
}

@Composable
fun ParkingItem(
    latitude: Double, longitude: Double, date: String, onClick: (Double, Double) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(latitude, longitude) },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "📍 Parkplatz am $date")
            Text(text = "Lat: $latitude, Lon: $longitude")
            Text(
                text = "➡️ Tippe, um in Google Maps zu öffnen",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
