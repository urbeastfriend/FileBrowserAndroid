package com.example.filebrowserandroid.ui.modifiedfileslist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filebrowserandroid.data.filemodified.FilesModifiedDao
import com.example.filebrowserandroid.usecases.GetFileInfoByPathUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ModifiedFileListViewModel @Inject constructor(
    private val getFileInfoByPathUseCase: GetFileInfoByPathUseCase,
    private val filesModifiedDao: FilesModifiedDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _screenState = MutableStateFlow(ModifiedFilesScreenState())
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            getModifiedFiles()
        }
    }
    private suspend fun getModifiedFiles() = withContext(Dispatchers.IO){
        _screenState.value = _screenState.value.copy(isLoading = true)
        val modifiedFiles = filesModifiedDao.getFileModifiedList()
        val modifiedFilesInfo = getFileInfoByPathUseCase(modifiedFiles.map { it.filePath })

        _screenState.value = _screenState.value.copy(fileList = modifiedFilesInfo, isLoading = false)

    }
}

