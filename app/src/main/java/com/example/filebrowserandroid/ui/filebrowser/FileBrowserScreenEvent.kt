package com.example.filebrowserandroid.ui.filebrowser

import com.example.filebrowserandroid.common.StringUiWrapper

sealed class FileBrowserScreenEvent  {

    data class NavigateToTextDirectory(val path:String): FileBrowserScreenEvent()

    data class ShowSnackBarMessage(val message:StringUiWrapper): FileBrowserScreenEvent()
}