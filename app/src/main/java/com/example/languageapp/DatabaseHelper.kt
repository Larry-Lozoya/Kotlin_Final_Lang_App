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
        val sqLiteDatabase = this.writableDatabase
        val queryInsert = ContentValues().apply {
            put("englishWord", engWord)
            put("spanishWord", spanWord)
            put("isCorrect", correct)
        }
        sqLiteDatabase.insertWithOnConflict("translation", null, queryInsert, SQLiteDatabase.CONFLICT_IGNORE)
    }
    fun isEmpty(): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM translation", null)
        return cursor
    }
    fun isEmptyActTwo(): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM translation WHERE _id > 8", null)
        return cursor
    }
    fun isEmptyActThree(): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM translation WHERE _id > 11", null)
        return cursor
    }

    fun getSpanAndEnglishWord(randomNumber: Int): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT englishWord, spanishWord FROM translation WHERE _id = $randomNumber AND isCorrect != 1", null)
        return cursor
    }

    fun getSpanAndEnglishWordActTwo(randomNumber: Int): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT englishWord, spanishWord FROM translation WHERE _id > 8 AND isCorrect != 1 AND _id = $randomNumber", null)
        return cursor
    }
    fun getSpanAndEnglishWordActThree(randomNumber: Int): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT englishWord, spanishWord FROM translation WHERE _id > 11 AND isCorrect != 1 AND _id = $randomNumber", null)
        return cursor
    }
    fun getArray(): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT _id FROM translation WHERE isCorrect != 1", null)
        return cursor
    }

    fun getArrayActTwo(): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT _id FROM translation WHERE isCorrect != 1 AND _id > 8", null)
        return cursor
    }

    fun getArrayActThree(): Cursor{
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery("SELECT _id FROM translation WHERE isCorrect != 1 AND _id > 11", null)
        return cursor
    }
    fun updateIsCorrect(currentIndex: Int){
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("isCorrect", 1)
        sqLiteDatabase.update("translation", contentValues, "_id = $currentIndex", null)
    }
    fun updateIsCorrectToReset(currentIndex: Int){
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("isCorrect", 0)
        sqLiteDatabase.update("translation", contentValues, "_id = $currentIndex", null)
    }
}