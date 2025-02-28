package com.example.findmycar_lb_b

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OpenStreetMapView(
    locationManager: LocationManager,
    onSaveParking: (Double, Double) -> Unit,
    databaseHelper: DatabaseHelper,
    currentLocation: GeoPoint?,
    navigationManager: NavigationManager
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var isHistoryExpanded by remember { mutableStateOf(false) } // Popup Zustand
    var parkingHistory by remember { mutableStateOf(emptyList<Triple<Double, Double, String>>()) } // Datenliste

    Box(modifier = Modifier.fillMaxSize()) {
        // 🌍 MapView
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)

                    currentLocation?.let {
                        controller.setCenter(it)
                    }

                    mapView = this
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { map ->
                currentLocation?.let { location ->
                    map.controller.setCenter(location)
                    map.overlays.clear() // Entfernt alte Marker, damit sich keine duplizieren

                    val marker = Marker(map).apply {
                        position = location
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "📍 Mein Standort"
                    }
                    map.overlays.add(marker) // Füge den Marker hinzu
                    map.invalidate() // Map neu laden
                }
            }

        )

        // 📌 Buttons in einer Spalte über der Map platzieren
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 📍 Parkplatz speichern Button
            Button(
                onClick = {
                    currentLocation?.let {
                        onSaveParking(it.latitude, it.longitude)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "📍 Parkplatz speichern")
            }
            Button(
                onClick = {
                    currentLocation?.let {
                        Log.d("DEBUG", "✅ Button wurde gedrückt, Standort: ${it.latitude}, ${it.longitude}") // 🔥 TEST
                        navigationManager.openGoogleMaps(it.latitude, it.longitude)
                    } ?: Log.e("DEBUG", "❌ currentLocation ist NULL!") // 🔥 TEST
                },
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                Text("🌍 In Google Maps öffnen")
            }

            // 📜 Verlauf anzeigen Button
            Button(
                onClick = {
                    isHistoryExpanded = !isHistoryExpanded
                    if (isHistoryExpanded) {
                        parkingHistory = databaseHelper.getParkingHistory()
                        Log.d("DEBUG", "🛠️ Verlauf geöffnet, Anzahl Parkplätze: ${parkingHistory.size}")
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = if (isHistoryExpanded) "🔽 Verlauf ausblenden" else "📜 Verlauf ansehen")
            }
        }

        // 📝 Popup für gespeicherte Parkplätze
        if (isHistoryExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Popup Höhe
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Text(text = "📜 Gespeicherte Parkplätze", style = MaterialTheme.typography.headlineMedium)

                    if (parkingHistory.isEmpty()) {
                        Text(text = "Keine gespeicherten Parkplätze.", modifier = Modifier.padding(top = 20.dp))
                    } else {
                        parkingHistory.forEach { (lat, lon, date) ->
                            Text(text = "📍 $date - Lat: $lat, Lon: $lon")
                        }
                    }
                }
            }
        }
    }
}
