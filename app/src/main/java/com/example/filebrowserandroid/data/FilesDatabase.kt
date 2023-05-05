package com.example.filebrowserandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FileHash::class],
    version = 1,
    exportSchema = false
)
abstract class FilesDatabase: RoomDatabase() {

    abstract fun filesHashDao(): FilesHashDao
}