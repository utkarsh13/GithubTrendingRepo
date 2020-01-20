package com.example.gojekassignment.repository

import android.content.Context
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.gojekassignment.Database.DatabaseRepository
import com.example.gojekassignment.Database.RepositoriesDatabase
import com.example.gojekassignment.Database.asDomainModel
import com.example.gojekassignment.domain.Repository
import com.example.gojekassignment.network.Network
import com.example.gojekassignment.network.asDBModel
import com.example.gojekassignment.trendingRepositories.RepositoriesApiStatus
import com.example.gojekassignment.trendingRepositories.RepositorySort
import com.example.gojekassignment.utils.IS_CACHE_AVAILABLE
import com.example.gojekassignment.utils.putBoolean
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
                    val getRepositoriesDeferred = Network.repositories.getAllRepositories()
                    val repositories = getRepositoriesDeferred.await()

                    database.repositoriesDatabaseDao.insertAllRepositories(repositories.asDBModel())
                    PreferenceManager.getDefaultSharedPreferences(context).putBoolean(IS_CACHE_AVAILABLE, true)
                    _apiStatus.postValue(RepositoriesApiStatus.SUCCESS)
                } catch (e: Exception) {
                    _apiStatus.postValue(RepositoriesApiStatus.ERROR)
                }
            }
        }
    }

    private fun getCacheStatus(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(IS_CACHE_AVAILABLE, false)
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