package com.example.filebrowserandroid.usecases

import com.example.filebrowserandroid.common.FileInfo
import com.example.filebrowserandroid.common.FileInfoMapper
import java.io.File
import javax.inject.Inject

class GetFileInfoByPathUseCase @Inject constructor(
    private val fileInfoMapper: FileInfoMapper,
) {

    operator fun invoke(path: String): FileInfo{
        val file = File(path)
        return fileInfoMapper.mapFromFile(file)
    }

    operator fun invoke(pathList: List<String>): List<FileInfo> {
        val filesList = pathList.map { File(it) }
        return fileInfoMapper.mapFromFileList(filesList)
    }
}