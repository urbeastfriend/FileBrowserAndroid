package com.example.filebrowserandroid.common

import android.content.Context
import androidx.annotation.StringRes

sealed class StringUiWrapper {
    data class StringValue(
        val value: String
    ) : StringUiWrapper()

    data class StringResource(
        @StringRes val id: Int,
        val args: List<Any>
    ): StringUiWrapper()

    fun asString(context: Context): String{
        return when (this){
            is StringValue -> value
            is StringResource -> context.getString(id,*args.toTypedArray())
        }
    }
}