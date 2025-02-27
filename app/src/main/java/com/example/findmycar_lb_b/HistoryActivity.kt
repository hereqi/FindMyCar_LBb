package com.example.findmycar_lb_b

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = DatabaseHelper(this)
        val parkingHistory = dbHelper.getParkingHistory()

        setContent {
            ParkingHistoryScreen(parkingHistory) { latitude, longitude ->
                val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(Parkplatz)")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
    }
}

@Composable
fun ParkingHistoryScreen(history: List<Triple<Double, Double, String>>, onClick: (Double, Double) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        history.forEach { (lat, lon, date) ->
            Text(
                text = "📍 $date - $lat, $lon",
                modifier = Modifier
                    .clickable { onClick(lat, lon) }
                    .padding(8.dp)
            )
        }
    }
}
