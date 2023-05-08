package com.example.filebrowserandroid.common

sealed class Response<T>(val data: T? = null, val message: StringUiWrapper? = null){
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(message: StringUiWrapper, data: T? = null) : Response<T>(data, message)
    class Loading<T>(data: T? = null) : Response<T>(data)
}