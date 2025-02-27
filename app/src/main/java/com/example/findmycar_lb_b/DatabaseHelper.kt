package com.example.findmycar_lb_b

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "parkingDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE ParkingLocations (id INTEGER PRIMARY KEY AUTOINCREMENT, latitude REAL, longitude REAL, date TEXT)")
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
        db.insert("ParkingLocations", null, values)
        db.close()
    }

    fun getParkingHistory(): List<Triple<Double, Double, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ParkingLocations ORDER BY id DESC", null)
        val list = mutableListOf<Triple<Double, Double, String>>()

        while (cursor.moveToNext()) {
            val lat = cursor.getDouble(1)
            val lon = cursor.getDouble(2)
            val date = cursor.getString(3)
            list.add(Triple(lat, lon, date))
        }

        cursor.close()
        db.close()
        return list
    }
}
