package com.example.languageapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class LanguageSelectionFragment : Fragment() {
    private lateinit var setOfLangs : ListView
    private var itemClickListener: AdapterView.OnItemClickListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_language_selection, container, false)
        setOfLangs = view.findViewById(R.id.setOfLearningLangs)
        setOfLangs.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            ListOfLanguages.setOfLanguages
        )
        setOfLangs.onItemClickListener = itemClickListener
        return view
    }
    fun setOnItemClickListener(onItemClickListener: AdapterView.OnItemClickListener) {
        itemClickListener = onItemClickListener
    }
}