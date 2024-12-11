package com.example.languageapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SpanishLanguageLevelOneActivity : AppCompatActivity() {
    private val ourInputs = arrayListOf("yes", "library", "where", "dog", "cat", "red", "green", "blue")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spanish_language_level_one)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        println(ourInputs.size)
        var index = 0
        while(true){

            if(index >= ourInputs.size){
                break
            }else{
                println(ourInputs[index])
                ++index
            }
        }
        fetchJoke()

    }
    private fun fetchJoke() {
        CoroutineScope(Dispatchers.IO).launch {
            var result = ""
            var httpURLConnection : HttpURLConnection? = null
            try {
                var url = URL("https://api.mymemory.translated.net/get?q=mom&langpair=en|es")
                httpURLConnection = url.openConnection() as HttpURLConnection

                httpURLConnection.requestMethod = "GET"

                if(httpURLConnection.responseCode != HttpURLConnection.HTTP_OK){
                    result = "BAD CONNECTION"

                }
                else{
                    val inputStreamReader = httpURLConnection.inputStream
                    val bufferedReader = BufferedReader(InputStreamReader(inputStreamReader))
                    result = bufferedReader.readText()
                    bufferedReader.close()
                }
            }
            catch (e: IOException){
                e.printStackTrace()
            }finally {
                httpURLConnection?.disconnect()
            }
            withContext(Dispatchers.Main){
                try {
                    val jsonObject = JSONObject(result)
                    val valueObject = jsonObject.getJSONObject("responseData")
                    val translatedResponse = valueObject.getString("translatedText")
                    println(translatedResponse)

                }
                catch (e: IOException){
                    Log.d("ERROR", e.toString())
                }
            }
        }
    }
}