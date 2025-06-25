package com.dev.storeapp

import android.app.Application
import com.dev.storeapp.app.utils.AppLogger
import dagger.hilt.android.HiltAndroidApp
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dev.storeapp.app.worker.MasterDataSyncWorker
import javax.inject.Inject
import com.google.firebase.FirebaseApp

@HiltAndroidApp
class StoreApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        AppLogger.d("StoreApp", "Application started")
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    private fun syncImmediately(){
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<MasterDataSyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
        AppLogger.e("AlarmReceiver", "Worker Started...")
    }
}