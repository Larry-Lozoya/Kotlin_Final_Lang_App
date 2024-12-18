package com.example.languageapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction

class LanguageSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_language_selection)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.languageSelectionToolbar)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listOfLangsFrag = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as? LanguageSelectionFragment
        if(listOfLangsFrag != null){
            listOfLangsFrag.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
                if(i > 0){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("COMING SOON!!")
                    builder.setMessage("THIS IS A WORK IN PROGRESS AND WILL COME SOON...")

                    builder.setPositiveButton("BACK"){ dialog, which->
                    }
                    builder.show()
                }else{
                    val intent = Intent(applicationContext, LevelSelectionActivity::class.java)
                    intent.putExtra("ID", i)
                    startActivity(intent)
                }
                /*
                Later add a dialog here to make sure that if they click on other languages
                to Show that this is a work in progress and will be coming soon!!!!
                 */
                // println(i)
            })
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.language_selection_menu, menu)
        return true
    }
}