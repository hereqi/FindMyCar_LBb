package com.example.findmycar_lb_b

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var navigationManager: NavigationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ OSMDroid korrekt konfigurieren
        val osmConfig = Configuration.getInstance()
        osmConfig.load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        osmConfig.userAgentValue = packageName

        // ✅ SQLite Datenbank initialisieren
        databaseHelper = DatabaseHelper(this)

        // ✅ Standortberechtigung prüfen
        checkLocationPermission()

        // ✅ LocationManager initialisieren
        locationManager = LocationManager(this)

        navigationManager = NavigationManager(this)


        setContent {
            var showHistory by remember { mutableStateOf(false) }
            var currentLocation by remember { mutableStateOf<GeoPoint?>(null) }

            // 📌 Standort abrufen beim Start
            LaunchedEffect(Unit) {
                locationManager.getCurrentLocation { latitude, longitude ->
                    currentLocation = GeoPoint(latitude, longitude)
                    Log.d("DEBUG", "📍 Standort erhalten: Lat=$latitude, Lon=$longitude")
                }
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 📍 Kartenansicht anzeigen
                OpenStreetMapView(
                    locationManager = locationManager,
                    onSaveParking = { latitude, longitude ->
                        saveParkingLocation(latitude, longitude)
                        vibratePhone() // ✅ Vibration auslösen
                    },
                    databaseHelper = databaseHelper,
                    currentLocation = currentLocation,
                    navigationManager = navigationManager
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 📌 Parkplatz speichern Button
                Button(
                    onClick = {
                        locationManager.getCurrentLocation { latitude, longitude ->
                            saveParkingLocation(latitude, longitude)
                            vibratePhone()
                            currentLocation = GeoPoint(latitude, longitude) // Standort aktualisieren
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("📍 Parkplatz speichern")
                }
                Button(
                    onClick = {
                        locationManager.getCurrentLocation { latitude, longitude ->
                            navigationManager.openGoogleMaps(latitude, longitude)
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("🌍 Standort in Maps öffnen")
                }


                // 📜 Verlauf anzeigen Button
                Button(
                    onClick = { showHistory = !showHistory },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(if (showHistory) "📜 Verlauf ausblenden" else "📜 Verlauf anzeigen")
                }
                Button(
                    onClick = {
                        currentLocation?.let {
                            navigationManager.openGoogleMaps(it.latitude, it.longitude)
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("🌍 In Google Maps öffnen")
                }


                // 📝 Popup für gespeicherte Parkplätze
                if (showHistory) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f))
                            .clickable { showHistory = false }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .padding(16.dp)
                                .align(Alignment.Center)
                        ) {
                            Text("📜 Gespeicherte Parkplätze", fontSize = 20.sp)

                            val history = databaseHelper.getParkingHistory()
                            history.forEach { (lat, lon, date) ->
                                Text("📍 $date - Lat: $lat, Lon: $lon", fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { showHistory = false },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("❌ Verlauf schließen")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    private fun saveParkingLocation(latitude: Double, longitude: Double) {
        val sdf = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault())
        val currentDate = sdf.format(Date())
        databaseHelper.saveParkingLocation(latitude, longitude, currentDate)
        Log.d("DEBUG", "📌 Parkplatz gespeichert: Lat=$latitude, Lon=$longitude am $currentDate")
    }

    private fun vibratePhone() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val vibrationEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
        } else {
            @Suppress("DEPRECATION")
            VibrationEffect.createOneShot(300, -1)
        }

        vibrator.vibrate(vibrationEffect)
    }
}
