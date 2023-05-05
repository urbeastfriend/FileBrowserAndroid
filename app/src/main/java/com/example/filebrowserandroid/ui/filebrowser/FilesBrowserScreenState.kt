package com.example.filebrowserandroid.ui.filebrowser

import android.os.Environment.getExternalStorageDirectory
import com.example.filebrowserandroid.common.FileInfo

data class FilesBrowserScreenState(
    val filesList: List<FileInfo> = emptyList(),
    val currentPath: String = getExternalStorageDirectory().path
)