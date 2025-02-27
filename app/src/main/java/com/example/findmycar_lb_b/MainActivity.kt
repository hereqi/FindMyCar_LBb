package com.example.findmycar_lb_b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(applicationContext, getPreferences(MODE_PRIVATE))

        setContent {
            OpenStreetMapView()
        }
    }
}

@Composable
fun OpenStreetMapView() {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setZoomRounding(true)
                    zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
                    setMultiTouchControls(true)
                    setMultiTouchControls(true)
                    val mapController = controller
                    mapController.setZoom(15.0)
                    mapController.setCenter(GeoPoint(47.3769, 8.5417))
                }
            }
        )
    }
}