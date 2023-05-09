package com.example.filebrowserandroid.ui.filebrowser

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filebrowserandroid.common.FileInfoMapper
import com.example.filebrowserandroid.common.Response
import com.example.filebrowserandroid.data.FilesHashDao
import com.example.filebrowserandroid.usecases.CheckPathIsDirectoryUseCase
import com.example.filebrowserandroid.usecases.GetDirectoryFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FileBrowserViewModel @Inject constructor(
    private val getDirectoryFilesUseCase: GetDirectoryFilesUseCase,
    private val checkPathIsDirectoryUseCase: CheckPathIsDirectoryUseCase,
    private val fileInfoMapper: FileInfoMapper,
    private val fileInfoDao: FilesHashDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _screenState = MutableStateFlow(FileBrowserScreenState())
    val screenState = _screenState.asStateFlow()

    private val _eventChannel = Channel<FileBrowserScreenEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        val path = state.get<String?>("filePath")
        path.let {
            if(!it.isNullOrEmpty()) {
                _screenState.value = _screenState.value.copy(path = it)
            }
        }
        viewModelScope.launch {
            getDirectoryFiles()
        }
    }

    private suspend fun getDirectoryFiles() = withContext(Dispatchers.IO) {

        getDirectoryFilesUseCase(_screenState.value.path).collect { result ->
            when (result) {
                is Response.Success -> {
                    result.data.let {
                        if (it != null) {
                            _screenState.value = _screenState.value.copy(
                                fileList = fileInfoMapper.mapFromFileList(
                                    it
                                ), isLoading = false
                            )
                        }
                    }
                }
                is Response.Error -> {
                    _screenState.value =
                        _screenState.value.copy(isLoading = false, fileList = emptyList())
                    result.message.let {
                        if (it != null) {
                            _eventChannel.send(FileBrowserScreenEvent.ShowSnackBarMessage(it))
                        }
                    }
                }
                is Response.Loading -> {
                    _screenState.value = _screenState.value.copy(isLoading = true)
                }

            }
        }
    }


    fun onFileClick(path: String){
        if(checkPathIsDirectoryUseCase(path)) {
            viewModelScope.launch {
                _eventChannel.send(FileBrowserScreenEvent.NavigateToDirectory(path))
            }
        }
        else{
            viewModelScope.launch {
                _eventChannel.send(FileBrowserScreenEvent.OpenFile(path))
            }
        }
    }
}