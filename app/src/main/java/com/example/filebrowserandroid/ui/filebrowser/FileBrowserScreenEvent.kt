package com.example.filebrowserandroid.ui.filebrowser

import com.example.filebrowserandroid.common.StringUiWrapper

sealed class FileBrowserScreenEvent  {

    data class OpenFile(val path:String): FileBrowserScreenEvent()

    data class NavigateToDirectory(val path:String): FileBrowserScreenEvent()

    data class ShowSnackBarMessage(val message:StringUiWrapper): FileBrowserScreenEvent()

    data class ShareFile(val path:String): FileBrowserScreenEvent()
}