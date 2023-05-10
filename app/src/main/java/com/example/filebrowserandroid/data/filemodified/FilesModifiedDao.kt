package com.example.filebrowserandroid.data.filemodified

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilesModifiedDao {

    @Query("SELECT * FROM filemodified_table")
    suspend fun getFileModifiedList(): List<FileModified>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fileModified: FileModified)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(fileModifiedList: List<FileModified>)

    @Query("DELETE FROM filemodified_table")
    suspend fun dropTable()
}