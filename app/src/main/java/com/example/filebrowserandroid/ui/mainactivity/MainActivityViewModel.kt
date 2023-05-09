package com.example.filebrowserandroid.ui.mainactivity

import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.example.filebrowserandroid.data.FilesHashDao
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val filesHashDao: FilesHashDao
) : ViewModel() {

    fun calculateFileHashes(){

    }
}