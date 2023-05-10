package com.example.filebrowserandroid.data.filehash

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "filehash_table")
@Parcelize
data class FileHash(
    val filePath: String,
    val fileHash: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) :Parcelable
{
    override fun equals(other: Any?): Boolean {
        other as FileHash

        if (other.filePath == filePath && other.fileHash!= fileHash) return false

        return true
    }

    override fun hashCode(): Int {
        return filePath.length * 6 + fileHash.hashCode() + 666
    }
}
