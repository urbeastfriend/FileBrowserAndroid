package com.example.filebrowserandroid.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FilesHashDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fileHash: FileHash)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(fileHashList: List<FileHash>)

    @Query("DELETE FROM filehash_table")
    suspend fun dropTable();
}