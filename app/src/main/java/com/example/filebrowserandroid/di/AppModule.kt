package com.example.filebrowserandroid.di

import android.app.Application
import android.os.Environment
import androidx.room.Room
import com.example.filebrowserandroid.data.FilesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDataBase(app: Application): FilesDatabase{
        return Room.databaseBuilder(app,FilesDatabase::class.java,"filesdb.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFilesHashDao(db: FilesDatabase) = db.filesHashDao()

    @Provides
    fun provideFilesModifiedDao(db: FilesDatabase) = db.filesModifiedDao()

    @Provides
    fun provideStoragePath() = Environment.getExternalStorageDirectory().path
}