package com.example.filebrowserandroid.usecases

import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.common.Response
import com.example.filebrowserandroid.common.StringUiWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.IOException
import javax.inject.Inject

class GetFilesExcludingDirectoriesUseCase @Inject constructor(){

    operator fun invoke(path: String): List<File> {
        try{
            val filesExcludingDirectories = File(path).walkTopDown().filter { !it.isDirectory }.toList()


            filesExcludingDirectories.let {
                if(it.isNullOrEmpty()){
                    throw IOException()
                }
                else{

                    return filesExcludingDirectories
                }
            }

        }
        catch (e: IOException){
            return emptyList()
        }

    }
}