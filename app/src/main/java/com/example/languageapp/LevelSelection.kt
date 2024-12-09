package com.example.languageapp

class LevelSelection(val level: String){
    companion object{
        val setOfLevels = arrayListOf(
            ListOfLanguages("Level 1 - Basic Words"),
            ListOfLanguages("Level 2 - Sentences"),
            ListOfLanguages("Level 3 - Voice"),
        )
    }
    override fun toString(): String {
        return level
    }
}