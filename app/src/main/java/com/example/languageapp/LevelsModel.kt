package com.example.languageapp

class LevelsModel {
    companion object{
        val spanishLevels = arrayListOf<Levels>().apply {
            add(Levels( "Level 1 - Simple Words", R.drawable.levelone))
            add(Levels("Level 2 - Sentences", R.drawable.leveltwo))
            add(Levels("Level 3 - Voice", R.drawable.levelthree))
        }
    }
}