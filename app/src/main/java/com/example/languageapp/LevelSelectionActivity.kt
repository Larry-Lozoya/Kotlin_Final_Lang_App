package com.example.languageapp

import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LevelSelectionActivity : AppCompatActivity(), ItemAdapter.ItemAdapterListener{
    private lateinit var levelsRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_level_selection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        levelsRecyclerView = findViewById(R.id.LevelSelectionRecyclerView)
        val itemAdapter = ItemAdapter(this)
        levelsRecyclerView.adapter = itemAdapter
        levelsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun click(position: Int) {
        if (position == 0){
            val intent = Intent(applicationContext, SpanishLanguageLevelOneActivity::class.java)
            intent.putExtra("ID", position)
            startActivity(intent)
        }
        else if (position == 1){
            val intent = Intent(applicationContext, SpanishLanguageLevelTwoActivity::class.java)
            intent.putExtra("ID", position)
            startActivity(intent)
        }
        else{
            val intent = Intent(applicationContext, SpanishLanguageLevelThreeActivity::class.java)
            intent.putExtra("ID", position)
            startActivity(intent)
        }
    }

}