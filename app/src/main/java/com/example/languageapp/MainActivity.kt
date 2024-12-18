package com.example.languageapp

import PeriodicLogWorker
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startPeriodicWork()
    }

    fun selectionOfLanguageAcitivty(view: View){
        val intent = Intent(applicationContext, LanguageSelectionActivity::class.java)
        startActivity(intent)
    }

    fun startPeriodicWork(){
        val inputData = Data.Builder()
            .putString("key", "Periodic Work Started")
            .build()
        val periodicWorkRequest = PeriodicWorkRequestBuilder<PeriodicLogWorker>(
            15, TimeUnit.MINUTES)
            .setInputData(inputData)
            .build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }
}