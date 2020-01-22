package com.example.gojekassignment.repository

import android.content.Context
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.gojekassignment.database.DatabaseRepository
import com.example.gojekassignment.database.RepositoriesDatabase
import com.example.gojekassignment.database.asDomainModel
import com.example.gojekassignment.domain.Repository
import com.example.gojekassignment.network.Network
import com.example.gojekassignment.network.asDBModel
import com.example.gojekassignment.trendingrepositories.RepositoriesApiStatus
import com.example.gojekassignment.trendingrepositories.RepositorySort
import com.example.gojekassignment.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TrendingReposRepository(
    private val database: RepositoriesDatabase,
    private val context: Context
) {

    val repositories: LiveData<List<Repository>> = Transformations.map(database.repositoriesDatabaseDao.getAllRepositories()) {
        it.asDomainModel()
    }

    private val _apiStatus = MutableLiveData<RepositoriesApiStatus>()

    val apiStatus: LiveData<RepositoriesApiStatus>
        get() = _apiStatus

    suspend fun refreshRepositories() {
        val isConnected = checkForInternetConnection()
        val isCacheAvailable = getCacheStatus()

        if (!isConnected && isCacheAvailable) {
            _apiStatus.postValue(RepositoriesApiStatus.OFFLINE)
        } else {
            withContext(Dispatchers.IO) {
                try {
                    _apiStatus.postValue(RepositoriesApiStatus.LOADING)
                    val getRepositoriesDeferred = Network.repositories.getAllRepositoriesAsync()
                    val repositories = getRepositoriesDeferred.await()

                    database.repositoriesDatabaseDao.insertAllRepositories(repositories.asDBModel())
                    PreferenceManager.getDefaultSharedPreferences(context).putBoolean(IS_CACHE_AVAILABLE, true)
                    PreferenceManager.getDefaultSharedPreferences(context).putLong(LAST_CACHE_TIME, System.currentTimeMillis())
                    _apiStatus.postValue(RepositoriesApiStatus.SUCCESS)
                } catch (e: Exception) {
                    _apiStatus.postValue(RepositoriesApiStatus.ERROR)
                }
            }
        }
    }

    private fun getCacheStatus(): Boolean {
        val isCacheAvailable = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_CACHE_AVAILABLE, false)
        val lastCacheTime = PreferenceManager.getDefaultSharedPreferences(context).getLong(LAST_CACHE_TIME, 0)
        val isCacheGood = isCacheGood(lastCacheTime, System.currentTimeMillis())
        return isCacheAvailable && isCacheGood
    }

    private fun checkForInternetConnection(): Boolean {
        val connectivityManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    suspend fun getRepositories(sortType: RepositorySort): List<Repository> {
        return withContext(Dispatchers.IO) {
            val repos: List<DatabaseRepository> = when (sortType) {
                RepositorySort.SORT_NAME -> database.repositoriesDatabaseDao.getAllRepositoriesSortedByName()
                RepositorySort.SORT_STAR -> database.repositoriesDatabaseDao.getAllRepositoriesSortedByStars()
                else -> database.repositoriesDatabaseDao.getAllRepositoriesList()
            }
            repos.asDomainModel()
        }
    }
}