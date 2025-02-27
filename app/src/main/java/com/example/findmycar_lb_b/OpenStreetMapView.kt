package com.example.findmycar_lb_b

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
    onViewHistory: () -> Unit
) {
    var currentLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var mapView: MapView? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        locationManager.getCurrentLocation { latitude, longitude ->
            Log.d("DEBUG", "Aktueller Standort: Lat=$latitude, Lon=$longitude")
            currentLocation = GeoPoint(latitude, longitude)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)

                    val initialLocation = currentLocation ?: GeoPoint(47.3769, 8.5417)
                    controller.setCenter(initialLocation)

                    mapView = this
                }
            },
            update = { map ->
                currentLocation?.let { location ->
                    map.controller.setCenter(location)
                    map.overlays.clear()

                    val marker = Marker(map).apply {
                        position = location
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Mein Standort"
                    }
                    map.overlays.add(marker)
                    map.invalidate()
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    currentLocation?.let { loc ->
                        onSaveParking(loc.latitude, loc.longitude)
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "📍 Parkplatz speichern", color = Color.White)
            }

            Button(
                onClick = onViewHistory,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "📜 Historie anzeigen", color = Color.White)
            }
        }
    }
}
