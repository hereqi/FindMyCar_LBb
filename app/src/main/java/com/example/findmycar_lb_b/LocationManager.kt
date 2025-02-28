package com.example.findmycar_lb_b

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class LocationManager(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(callback: (Double, Double) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("DEBUG", "Standort-Berechtigung fehlt!") // ChatGPT
            return
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(
                        "DEBUG",
                        "📍 Standort erhalten: Lat=${location.latitude}, Lon=${location.longitude}"
                    ) //ChatGPT
                    callback(location.latitude, location.longitude)
                } else {
                    Log.e(
                        "DEBUG",
                        "Kein Standort verfügbar, Standardwert wird genutzt!"
                    ) //ChatGPT generiert
                    callback(47.3769, 8.5417) // Standardwert: Zürich
                }
            }
    }
}
