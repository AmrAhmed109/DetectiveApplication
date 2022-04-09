package com.example.detectiveapplication.datastore.utils

sealed class Language(val key: String) {
    object Arabic : Language("ar")
    object English : Language("en")
}
