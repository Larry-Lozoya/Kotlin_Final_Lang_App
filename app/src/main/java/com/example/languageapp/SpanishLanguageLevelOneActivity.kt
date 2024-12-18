package com.example.languageapp

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import kotlin.random.Random
import kotlin.random.nextInt


class SpanishLanguageLevelOneActivity : AppCompatActivity() {
    private val ourInputs = arrayListOf("yes", "library", "where", "dog", "cat", "red", "green", "blue")
//    private val ourInputs = arrayListOf("cat", "red")
    private lateinit var spanishInput: EditText
    private lateinit var englishTarget: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var cursor: Cursor
    private var dbEnglishWord = 0
    private var dbSpanishWord = 0
    private var index = 0
    private var randomInt = 0
    private var ourNumbers = arrayListOf<Int>()
    private var dbEmpty = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spanish_language_level_one)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.actOneToolbar)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        println(ourInputs.size)
        spanishInput = findViewById(R.id.actOneEditText)
        // https://stackoverflow.com/questions/5715072/change-android-keyboard-language
        // This makes it so our keyboard will show up as a spanish keyboard first we still have
        // to manually get the spanish keyboard from our settings though
        spanishInput.setImeHintLocales(LocaleList(Locale("es", "MX")))
        englishTarget = findViewById(R.id.actOneText)
        databaseHelper = DatabaseHelper(applicationContext)
        cursor = databaseHelper.isEmpty()
        if(cursor.moveToFirst()){
            dbEmpty = true
            getOurArray()
            println(ourNumbers)
            if(ourNumbers.isNotEmpty()){
                println(ourNumbers)
                getRandomWord()
                println(randomInt)
            }else if(ourNumbers.isEmpty() && spanishInput.text.isEmpty()){
                println(ourNumbers.isEmpty())
                showAllCorrectDialog()
            }
        }else{
            dbEmpty = false
            fetchOurSpanishWords()
        }

    }

    private fun fetchOurSpanishWords() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if(index >= ourInputs.size){
                    break
                }else {
                    val eachWord = ourInputs[index]
//                    println(eachWord)
                    var result = ""
                    var httpURLConnection: HttpURLConnection? = null
                    try {
                        var url = URL("https://api.mymemory.translated.net/get?q=$eachWord&langpair=en|es")
                        httpURLConnection = url.openConnection() as HttpURLConnection

                        httpURLConnection.requestMethod = "GET"

                        if (httpURLConnection.responseCode != HttpURLConnection.HTTP_OK) {
                            result = "BAD CONNECTION"

                        } else {
                            val inputStreamReader = httpURLConnection.inputStream
                            val bufferedReader = BufferedReader(InputStreamReader(inputStreamReader))
                            result = bufferedReader.readText()
                            bufferedReader.close()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        httpURLConnection?.disconnect()
                    }
                    withContext(Dispatchers.Main) {
                        try {
                            val jsonObject = JSONObject(result)
                            val valueObject = jsonObject.getJSONObject("responseData")
                            val translatedResponse = valueObject.getString("translatedText")
                            // put translatedResponse into db we only have to fetchOurSpanishWords once
                            println(translatedResponse)
                            databaseHelper.putItemsIntoDB(ourInputs[index], translatedResponse, 0)
                        } catch (e: IOException) {
                            Log.d("ERROR", e.toString())
                        }
                    }
                    ++index
                }
            }
            delay(3000)
            getOurArray()
            println(ourNumbers)
            getRandomWord()
            println(randomInt)
        }
    }

    fun actOneSubmitButton(view: View) {
        if(spanishInput.text.toString() == cursor.getString(dbSpanishWord)){
//            println(ourNumbers)
            if(ourNumbers.isNotEmpty()){
                println(randomInt)
                Toast.makeText(applicationContext, "YOUR ARE CORRECT!!!", Toast.LENGTH_SHORT).show()
                databaseHelper.updateIsCorrect(randomInt)
                getRandomWord()
                spanishInput.text.clear()
            }else{
                databaseHelper.updateIsCorrect(randomInt)
//                println(randomInt)
                englishTarget.text = ""
                showAllCorrectDialog()
            }
        }else{
            Toast.makeText(applicationContext, "YOUR ARE INCORRECT!!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRandomWord(){
        randomInt = ourNumbers.random()
        cursor = databaseHelper.getSpanAndEnglishWord(randomInt)
        if(cursor.moveToFirst()){
            dbEnglishWord = cursor.getColumnIndexOrThrow("englishWord")
            englishTarget.text = cursor.getString(dbEnglishWord)
//            println(cursor.getString(englishWord))
            dbSpanishWord = cursor.getColumnIndexOrThrow("spanishWord")
            println(cursor.getString(dbSpanishWord))
        }
        ourNumbers.remove(randomInt)
    }

    private fun showAllCorrectDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Completed")
        builder.setMessage("You have gotten every entry correct!!")

        builder.setPositiveButton("BACK"){ dialog, which->
            Toast.makeText(this, "ourBackButton", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LevelSelectionActivity::class.java)
            startActivity(intent)
        }
        builder.show()
    }

    private fun getOurArray(){
        cursor = databaseHelper.getArray()
        while (cursor.moveToNext()){
            println(cursor.getString(0))
            ourNumbers.add(cursor.getString(0).toInt())
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_layout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.homeButton ->{
                // Where to go from here instead of just toasting later
                //Toast.makeText(this, "Back Button Clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, LevelSelectionActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}