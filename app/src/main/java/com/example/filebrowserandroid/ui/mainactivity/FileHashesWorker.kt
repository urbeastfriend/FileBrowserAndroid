package com.example.filebrowserandroid.ui.mainactivity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.filebrowserandroid.R
import com.example.filebrowserandroid.data.FileHash
import com.example.filebrowserandroid.data.FilesHashDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import com.example.filebrowserandroid.common.md5
import com.example.filebrowserandroid.usecases.GetFilesExcludingDirectoriesUseCase
import java.io.File

@HiltWorker
class FileHashesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getFilesExcludingDirectoriesUseCase: GetFilesExcludingDirectoriesUseCase,
    private val filesHashDao: FilesHashDao,
    private val path: String,

) : CoroutineWorker(context, params) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val filesToHash = getFilesExcludingDirectoriesUseCase(path)
        val fileHashesList = filesToHash.map { FileHash(it.path,it.md5()) }
        filesHashDao.dropTable()
        filesHashDao.insertList(fileHashesList)
        if(Build.VERSION.SDK_INT < 31)
        {
            notificationManager.notify(NOTIFICATION_ID,buildForegroundInfo().notification)
        }
        return Result.success()

    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return buildForegroundInfo()
    }

    private fun buildForegroundInfo(): ForegroundInfo{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
        }
        val id = applicationContext.getString(R.string.channel_id)
        val title = applicationContext.getString(R.string.notification_title)
        val text = applicationContext.getString(R.string.notification_text)
        val notification = NotificationCompat.Builder(applicationContext,id)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        return ForegroundInfo(NOTIFICATION_ID,notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(){
        val id = applicationContext.getString(R.string.channel_id)
        val name = applicationContext.getString(R.string.channel_name)
        val description = applicationContext.getString(R.string.channel_description)
        val channel = NotificationChannel(id,name,NotificationManager.IMPORTANCE_HIGH)
        channel.description = description
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        notificationManager.createNotificationChannel(channel)
    }

    companion object{
        private const val TAG = "FileHashesWorker"
        private const val NOTIFICATION_ID = 666

        fun start(context: Context){

            val worker = OneTimeWorkRequest.Builder(FileHashesWorker::class.java)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).build()

            WorkManager.getInstance(context).enqueue(worker)
        }
    }
}

