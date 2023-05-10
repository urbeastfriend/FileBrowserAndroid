package com.example.filebrowserandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.filebrowserandroid.data.filehash.FileHash
import com.example.filebrowserandroid.data.filehash.FilesHashDao
import com.example.filebrowserandroid.data.filemodified.FileModified
import com.example.filebrowserandroid.data.filemodified.FilesModifiedDao

@Database(
    entities = [FileHash::class,FileModified::class],
    version = 1,
    exportSchema = false
)
abstract class FilesDatabase: RoomDatabase() {

    abstract fun filesHashDao(): FilesHashDao

    abstract fun filesModifiedDao(): FilesModifiedDao
}