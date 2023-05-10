package com.example.filebrowserandroid.data.filehash

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilesHashDao {

    @Query("SELECT * FROM filehash_table")
    suspend fun getFileHashList(): List<FileHash>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fileHash: FileHash)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(fileHashList: List<FileHash>)

    @Query("DELETE FROM filehash_table")
    suspend fun dropTable()
}