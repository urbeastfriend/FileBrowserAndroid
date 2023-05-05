package com.example.filebrowserandroid.ui.filebrowser

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.filebrowserandroid.data.FilesHashDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FilesBrowserViewModel @Inject constructor(
    private val FilesInfoDao: FilesHashDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _currentDirectoryFiles = MutableStateFlow(FilesBrowserScreenState())
    val currentDirectoryFiles = _currentDirectoryFiles.asStateFlow()


    private fun getCurrentDirectoryFiles(){

    }
}