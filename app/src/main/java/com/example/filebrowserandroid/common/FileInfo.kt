package com.example.filebrowserandroid.common

import java.io.File

data class FileInfo (
    val fileName: String,
    val filePath: String,
    val dateCreated: String,
    val fileSize: Long
)