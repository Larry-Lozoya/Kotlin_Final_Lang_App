package com.example.languageapp

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction

class LanguageSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_language_selection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listOfLangsFrag = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as? LanguageSelectionFragment
        if(listOfLangsFrag != null){
            listOfLangsFrag.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val intent = Intent(applicationContext, LevelSelectionActivity::class.java)
                intent.putExtra("ID", i)
                /*
                Later add a dialog here to make sure that if they click on other languages
                to Show that this is a work in progress and will be coming soon!!!!
                 */
                // println(i)
                startActivity(intent)
            })
        }
    }
}