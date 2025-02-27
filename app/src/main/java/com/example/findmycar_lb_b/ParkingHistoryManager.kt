package com.example.findmycar_lb_b

import android.content.Context
import android.content.SharedPreferences
import org.osmdroid.util.GeoPoint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ParkingHistoryManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("parking_history", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveParkingSpot(location: GeoPoint) {
        val spots = getParkingHistory().toMutableList()
        spots.add(0, location) // Neuen Parkplatz oben hinzufügen

        // Maximal 5 Parkplätze speichern
        if (spots.size > 5) {
            spots.removeAt(spots.size - 1)
        }

        val json = gson.toJson(spots)
        sharedPreferences.edit().putString("history", json).apply()
    }

    fun getParkingHistory(): List<GeoPoint> {
        val json = sharedPreferences.getString("history", null) ?: return emptyList()
        val type = object : TypeToken<List<GeoPoint>>() {}.type
        return gson.fromJson(json, type)
    }
}
