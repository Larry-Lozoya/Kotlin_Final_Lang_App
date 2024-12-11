package com.example.languageapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private val DATABASE_NAME = "spanishToEnglish.db"
        private val DATABASE_VERSION = 1
    }
    override fun onCreate(p0: SQLiteDatabase?) {
        // Fill later with what columns we want in our table
        val query = """
            CREATE TABLE translation(
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                englishWord TEXT,
                spanishWord TEXT,
                isCorrect INTEGER
            );
        """.trimIndent()
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}