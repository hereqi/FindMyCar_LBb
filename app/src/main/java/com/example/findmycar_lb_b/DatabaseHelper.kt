package com.example.findmycar_lb_b

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

//ChatGPT überarbeitet / bzw. Hilfe gebraucht

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "parkingDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE ParkingLocations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "latitude REAL, " +
                    "longitude REAL, " +
                    "date TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ParkingLocations")
        onCreate(db)
    }

    fun saveParkingLocation(latitude: Double, longitude: Double, date: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("latitude", latitude)
            put("longitude", longitude)
            put("date", date)
        }
        val result = db.insert("ParkingLocations", null, values)
        db.close()

        if (result == -1L) {
            Log.e("DEBUG", "❌ Fehler beim Speichern des Parkplatzes!") // ChatGPT generiert
        } else {
            Log.d(
                "DEBUG",
                "✅ Parkplatz gespeichert: Lat=$latitude, Lon=$longitude am $date"
            ) // ChatGPT generiert
        }
    }

    fun getParkingHistory(): List<Triple<Double, Double, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ParkingLocations ORDER BY id DESC", null)
        val list = mutableListOf<Triple<Double, Double, String>>()

        Log.d(
            "DEBUG",
            "📂 Starte Datenbankabfrage für gespeicherte Parkplätze..."
        ) //CahtGPT generiert

        while (cursor.moveToNext()) {
            val lat = cursor.getDouble(1)
            val lon = cursor.getDouble(2)
            val date = cursor.getString(3)
            list.add(Triple(lat, lon, date))

            Log.d("DEBUG", "✅ Parkplatz geladen: $date - Lat=$lat, Lon=$lon") // ChatGPT generiert
        }

        cursor.close()
        db.close()

        Log.d(
            "DEBUG",
            "🗂️ Gesamtanzahl gespeicherter Parkplätze: ${list.size}"
        ) // ChatGPT generiert

        return list
    }


}