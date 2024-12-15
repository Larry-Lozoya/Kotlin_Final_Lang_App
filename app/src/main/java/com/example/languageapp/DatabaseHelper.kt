package com.example.languageapp

import android.content.ContentValues
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
                isCorrect INTEGER,
                UNIQUE (englishWord, spanishWord)
            );
        """.trimIndent()
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
    fun putItemsIntoDB(engWord: String, spanWord: String, correct: Int){
        val sqLiteDatabase = this.readableDatabase
        val queryInsert = ContentValues().apply {
            put("englishWord", engWord)
            put("spanishWord", spanWord)
            put("isCorrect", correct)
        }
        sqLiteDatabase.insertWithOnConflict("translation", null, queryInsert, SQLiteDatabase.CONFLICT_IGNORE)
    }

    fun getSpanAndEnglishWord(randomNumber: Int): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT englishWord, spanishWord FROM translation WHERE _id = $randomNumber AND isCorrect != 1", null)
        return cursor
    }
    fun getArray(): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT _id FROM translation WHERE isCorrect != 1", null)
        return cursor
    }

    fun updateIsCorrect(currentIndex: Int){
        val sqLiteDatabase = this.readableDatabase
        val contentValues = ContentValues()
        contentValues.put("isCorrect", 1)
        sqLiteDatabase.update("translation", contentValues, "_id = $currentIndex", null)
    }
}