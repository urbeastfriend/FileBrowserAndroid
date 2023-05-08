package com.example.filebrowserandroid.ui.featurepicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturePickerViewModel @Inject constructor(

) : ViewModel() {

    private val _eventChannel = Channel<FeaturePickerScreenEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()



    fun onNavigateFileBrowserClick() {
        viewModelScope.launch {
            _eventChannel.send(FeaturePickerScreenEvent.NavigateFileBrowserScreen)
        }
    }

    fun onNavigateModifiedFilesClick() {
        viewModelScope.launch {
            _eventChannel.send(FeaturePickerScreenEvent.NavigateModifiedFilesScreen)
        }
    }
}