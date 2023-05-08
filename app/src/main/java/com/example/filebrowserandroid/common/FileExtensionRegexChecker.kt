package com.example.filebrowserandroid.common

operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)