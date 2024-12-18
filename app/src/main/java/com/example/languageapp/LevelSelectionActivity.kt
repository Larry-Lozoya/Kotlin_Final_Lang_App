package com.example.languageapp

import android.content.ClipData.Item
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LevelSelectionActivity : AppCompatActivity(), ItemAdapter.ItemAdapterListener{
    private lateinit var levelsRecyclerView: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var cursor: Cursor
    private var dbEmpty = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_level_selection)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.levelSelectionToolbar)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        levelsRecyclerView = findViewById(R.id.LevelSelectionRecyclerView)
        val itemAdapter = ItemAdapter(this)
        levelsRecyclerView.adapter = itemAdapter
        levelsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        databaseHelper = DatabaseHelper(applicationContext)

    }


    override fun click(position: Int) {
        if (position == 0){
            val intent = Intent(applicationContext, SpanishLanguageLevelOneActivity::class.java)
            intent.putExtra("ID", position)
            startActivity(intent)
        }
        else if (position == 1){
            cursor = databaseHelper.isEmpty()
            if(cursor.moveToFirst()){
                dbEmpty = true
                val intent = Intent(applicationContext, SpanishLanguageLevelTwoActivity::class.java)
                intent.putExtra("ID", position)
                startActivity(intent)
            }else{
                levelDialog("Attempt Level 1 First", "You must Attempt level one first before you move to level 2!!!!", "1")
                dbEmpty = false
            }
        }
        else{
            cursor = databaseHelper.isEmptyActTwo()
            if(cursor.moveToFirst()){
                dbEmpty = true
                val intent = Intent(applicationContext, SpanishLanguageLevelThreeActivity::class.java)
                intent.putExtra("ID", position)
                startActivity(intent)
            }else{
                levelDialog("Attempt Level 1 and 2 First", "You must Attempt level one first and level 2 before you move to level 3!!!!" , "2 and 1")
                dbEmpty = false
            }
        }
    }
    private fun levelDialog(title: String, message: String, toast: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton("BACK"){ dialog, which->
            Toast.makeText(this, "PLEASE ATTEMPT LEVELS $toast", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.level_start_over, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.levelOneRestart ->{
//                println("Do we get here?")
                for(i in 1..7){
                    databaseHelper.updateIsCorrectToReset(i)
                    println(i)
                }
                true
            }
            R.id.levelTwoRestart ->{
                for(i in 8..11){
                    databaseHelper.updateIsCorrectToReset(i)
                    println(i)
                }
                true
            }
            R.id.levelThreeRestart ->{
                databaseHelper.updateIsCorrectToReset(12)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}