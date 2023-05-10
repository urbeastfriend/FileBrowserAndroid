package com.example.filebrowserandroid.common

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FileInfoMapper @Inject constructor() {
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm")

    fun mapFromFile(file: File): FileInfo {
        return FileInfo(
            fileName = file.name,
            filePath = file.path,
            dateCreated = format.format(Date(file.lastModified())),
            fileSize = file.length()
        )
    }

    fun mapFromFileList(fileList: List<File>): List<FileInfo>{
        return fileList.map { mapFromFile(it) }
    }
}