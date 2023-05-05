package com.example.filebrowserandroid.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
class FileHash(
    val fileName: String,
    val filePath: String,
    val fileHash: String,
    @PrimaryKey(autoGenerate = true) val id: Int
) :Parcelable