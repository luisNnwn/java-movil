package com.example.medioambiente

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "env_unit.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE readings (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                timestamp INTEGER,
                temperature REAL,
                humidity REAL,
                pressure REAL,
                light REAL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldV: Int, newV: Int) {
        db.execSQL("DROP TABLE IF EXISTS readings")
        onCreate(db)
    }

    fun insertReading(
        ts: Long,
        temp: Double?,
        hum: Double?,
        pres: Double?,
        light: Double?
    ): Long {
        val cv = ContentValues()
        cv.put("timestamp", ts)
        temp?.let { cv.put("temperature", it) }
        hum?.let { cv.put("humidity", it) }
        pres?.let { cv.put("pressure", it) }
        light?.let { cv.put("light", it) }
        return writableDatabase.insert("readings", null, cv)
    }

    fun getLatestReadings(limit: Int): Cursor {
        return readableDatabase.query(
            "readings",
            null,
            null,
            null,
            null,
            null,
            "timestamp DESC",
            limit.toString()
        )
    }
}
