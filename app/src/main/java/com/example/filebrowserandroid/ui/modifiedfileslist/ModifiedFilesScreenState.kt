package com.example.filebrowserandroid.ui.modifiedfileslist

import com.example.filebrowserandroid.common.FileInfo

data class ModifiedFilesScreenState(
    val fileList: List<FileInfo> = emptyList(),
    val isLoading: Boolean = true
)