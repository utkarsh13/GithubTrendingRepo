package com.utkarsh.githubtrending.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.utkarsh.githubtrending.data.database.RepositoriesDatabase
import com.utkarsh.githubtrending.data.repository.TrendingReposRepository
import com.utkarsh.githubtrending.utils.IS_CACHE_AVAILABLE
import com.utkarsh.githubtrending.utils.defaultSharedPreferences
import com.utkarsh.githubtrending.utils.putBoolean
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
            applicationContext.defaultSharedPreferences.putBoolean(IS_CACHE_AVAILABLE, false)
            repository.refreshRepositories()
            return Payload(Result.SUCCESS)
        } catch (e: HttpException) {
            return Payload(Result.RETRY)
        }
    }

}