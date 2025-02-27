package com.example.findmycar_lb_b

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var locationManager: LocationManager
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SQLite Datenbank initialisieren
        databaseHelper = DatabaseHelper(this)

        // Standortberechtigung prüfen
        checkLocationPermission()

        // LocationManager initialisieren
        locationManager = LocationManager(this)

        setContent {
            OpenStreetMapView(
                locationManager,
                onSaveParking = { latitude, longitude -> saveParkingLocation(latitude, longitude) },
                onViewHistory = { openParkingHistory() }
            )
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
        val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        databaseHelper.saveParkingLocation(latitude, longitude, currentDate)
        Log.d("DEBUG", "Parkplatz gespeichert: Lat=$latitude, Lon=$longitude am $currentDate")
    }

    private fun openParkingHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
}
