package com.example.filebrowserandroid.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "filehash_table")
@Parcelize
class FileHash(
    val filePath: String,
    val fileHash: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) :Parcelable