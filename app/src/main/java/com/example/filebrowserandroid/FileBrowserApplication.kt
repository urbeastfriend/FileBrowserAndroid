package com.example.filebrowserandroid

import android.app.Application
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FileBrowserApplication: Application() {


    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun onCreate() {
        super.onCreate()

        WorkManager.initialize(
            this, Configuration.Builder().setWorkerFactory(
                workerFactory
            ).build()
        )
    }
}