package com.example.filebrowserandroid.data.filemodified

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "filemodified_table")
@Parcelize
class FileModified (
    val filePath: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    ) : Parcelable
