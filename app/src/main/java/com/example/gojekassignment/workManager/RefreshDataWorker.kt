package com.example.gojekassignment.workManager

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gojekassignment.Database.RepositoriesDatabase
import com.example.gojekassignment.repository.TrendingReposRepository
import com.example.gojekassignment.utils.IS_CACHE_AVAILABLE
import com.example.gojekassignment.utils.putBoolean
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Payload {
        val database = RepositoriesDatabase.getInstance(applicationContext)
        val repository = TrendingReposRepository(database, applicationContext)

        try {
            //To discard cache
            database.repositoriesDatabaseDao.clear()
            PreferenceManager.getDefaultSharedPreferences(applicationContext).putBoolean(IS_CACHE_AVAILABLE, false)
            repository.refreshRepositories()
            return Payload(Result.SUCCESS)
        } catch (e: HttpException) {
            return Payload(Result.RETRY)
        }
    }

}