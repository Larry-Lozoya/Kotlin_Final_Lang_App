package com.example.languageapp

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

class SpanishLanguageLevelThreeActivity : AppCompatActivity() {
    private val webInputs = arrayListOf("How%20are%20you")
    private val dbInputs = arrayListOf("How are you")
    private lateinit var spanishInput: TextView
    private lateinit var englishTarget: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private var dbEnglishWord = 0
    private var dbSpanishWord = 0
    private lateinit var cursor: Cursor
    private var index = 0
    private var randomInt = 0
    private var ourNumbers = arrayListOf<Int>()
    private var dbEmpty = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spanish_language_level_three)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.actThreeToolbar)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        spanishInput = findViewById(R.id.actThreeSpanishInput)
        englishTarget = findViewById(R.id.actThreeEnglishTarget)
        databaseHelper = DatabaseHelper(applicationContext)
        cursor = databaseHelper.isEmptyActThree()
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

    fun actThreeSubmitButton(view: View) {
        if(spanishInput.text.toString() == cursor.getString(dbSpanishWord)){
//            println(ourNumbers)
            if(ourNumbers.isNotEmpty()){
                println(randomInt)
                Toast.makeText(applicationContext, "YOUR ARE CORRECT!!!", Toast.LENGTH_SHORT).show()
                databaseHelper.updateIsCorrect(randomInt)
                getRandomWord()
                spanishInput.text = ""
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

    fun actThreeVoiceToSpeechButton(view: View) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", "es_ES");
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS, Locale("es", "ES"))
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something In Spanish...")
        activityResultLauncher.launch(intent)
    }
    private val activityResultLauncher :ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == RESULT_OK && result.data != null){
                val resultData = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = resultData?.get(0)
                spanishInput.text = spokenText
            }
        }

    private fun fetchOurSpanishWords() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if(index >= webInputs.size){
                    break
                }else {
                    val eachWord = webInputs[index]
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
                            val newTranslatedResponse = translatedResponse.replace("¿", "")
                                .replace("?", "")
                                .replace("ó", "o")
                                .replace("á", "a")
                            println(newTranslatedResponse.lowercase())
                            databaseHelper.putItemsIntoDB(dbInputs[index], newTranslatedResponse.lowercase(), 0)
                        } catch (e: IOException) {
                            Log.d("ERROR", e.toString())
                        }
                    }
                    ++index
                }
            }
            delay(1000)
            getOurArray()
            println(ourNumbers)
            getRandomWord()
            println(randomInt)
        }
    }

    private fun getRandomWord(){
        randomInt = ourNumbers.random()
        cursor = databaseHelper.getSpanAndEnglishWordActThree(randomInt)
        if(cursor.moveToFirst()){
            dbEnglishWord = cursor.getColumnIndexOrThrow("englishWord")
            englishTarget.text = cursor.getString(dbEnglishWord)
//            println(cursor.getString(englishWord))
            dbSpanishWord = cursor.getColumnIndexOrThrow("spanishWord")
            println(cursor.getString(dbSpanishWord))
        }
        ourNumbers.remove(randomInt)
    }

    private fun getOurArray(){
        cursor = databaseHelper.getArrayActThree()
        while (cursor.moveToNext()){
//            println(cursor.getString(0))
            ourNumbers.add(cursor.getString(0).toInt())
        }
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