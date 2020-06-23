package com.utkarsh.githubtrending

import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.utkarsh.githubtrending.utils.defaultSharedPreferences
import com.utkarsh.githubtrending.workmanager.RefreshDataWorker
import java.util.concurrent.TimeUnit

open class BaseActivity : AppCompatActivity() {

    fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val hours: Long =
            applicationContext.defaultSharedPreferences.getString("sync_hours", "2").toString().toLong()
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(hours, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()


        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }

}