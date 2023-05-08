package com.example.filebrowserandroid.common

import java.io.File
import java.util.*
import javax.inject.Inject

class FileInfoMapper @Inject constructor() {

    fun mapFromFile(file: File): FileInfo {
        return FileInfo(
            fileName = file.name,
            filePath = file.path,
            dateCreated = Date(file.lastModified()).toString(),
            fileSize = file.length()
        )
    }

    fun mapFromFileList(fileList: List<File>): List<FileInfo>{
        return fileList.map { mapFromFile(it) }
    }
}