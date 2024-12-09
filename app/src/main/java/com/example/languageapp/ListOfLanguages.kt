package com.example.languageapp

class ListOfLanguages(var specificLang: String) {
    companion object{
        val setOfLanguages = arrayListOf(
            ListOfLanguages("Spanish"),
            ListOfLanguages("French"),
            ListOfLanguages("Portuguese"),
            ListOfLanguages("Bambara"),
            ListOfLanguages("Mandarin"),
            ListOfLanguages("Russian"),
            ListOfLanguages("Arabic")
        )
    }
    override fun toString(): String {
        return specificLang
    }
}