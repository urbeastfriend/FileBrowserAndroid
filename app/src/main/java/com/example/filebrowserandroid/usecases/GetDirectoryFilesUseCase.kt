package com.example.filebrowserandroid.usecases

import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.common.Response
import com.example.filebrowserandroid.common.StringUiWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import java.io.File
import java.io.IOException
import javax.inject.Inject


class GetDirectoryFilesUseCase @Inject constructor(

){
    operator fun invoke(path: String): Flow<Response<List<File>>>  = flow{
        try{
            emit(Response.Loading())
            val root = File(path)
            val filesAndFolders = root.listFiles()

            filesAndFolders.let {
                if(it.isNullOrEmpty()){
                    throw IOException()
                }
                else{
                    emit(Response.Success(data = it.asList()))
                }
            }

        }
        catch (e: IOException){
            emit(Response.Error(
                message = StringUiWrapper.StringResource(id = R.string.directoryIsNullOrEmpty, args = emptyList()),
                data = emptyList()
            ))
        }

    }
}