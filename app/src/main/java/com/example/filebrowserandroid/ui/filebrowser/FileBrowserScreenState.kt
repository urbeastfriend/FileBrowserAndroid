package com.example.filebrowserandroid.ui.filebrowser

import android.content.Context
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.common.FileInfo
import kotlin.io.path.Path


data class FileBrowserScreenState(
    val fileList: List<FileInfo> = emptyList(),
    val path: String = getExternalStorageDirectory().path,
    val isLoading: Boolean = true
)