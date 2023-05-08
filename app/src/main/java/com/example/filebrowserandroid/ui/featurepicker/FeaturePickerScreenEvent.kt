package com.example.filebrowserandroid.ui.featurepicker

sealed class FeaturePickerScreenEvent {

    object NavigateFileBrowserScreen : FeaturePickerScreenEvent()

    object NavigateModifiedFilesScreen : FeaturePickerScreenEvent()
}