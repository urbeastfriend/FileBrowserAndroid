package com.example.filebrowserandroid.usecases

import java.io.File
import javax.inject.Inject

class CheckPathIsDirectoryUseCase @Inject constructor(){

    operator fun invoke(path: String): Boolean{
        val file = File(path)
        return file.isDirectory
    }
}