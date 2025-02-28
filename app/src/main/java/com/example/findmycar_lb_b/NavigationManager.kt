package com.example.findmycar_lb_b

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

//ChatGpt generierte funktion

class NavigationManager(private val context: Context) {

    fun openGoogleMaps(latitude: Double, longitude: Double, context: Context) {
        val uri = Uri.parse("https://www.google.com/maps?q=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
            Log.d("DEBUG", "✅ Google Maps im Browser geöffnet.")
        } catch (e: Exception) {
            Log.e("DEBUG", "❌ Fehler beim Öffnen von Google Maps: ${e.message}")
        }
    }


}