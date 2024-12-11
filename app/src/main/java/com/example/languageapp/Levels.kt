package com.example.languageapp

class Levels(var level: String? = null, var levelImage: Int? = null) {
    override fun toString(): String {
        return level?: ""
    }
}