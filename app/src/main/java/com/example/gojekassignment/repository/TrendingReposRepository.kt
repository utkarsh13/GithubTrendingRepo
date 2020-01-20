package com.example.gojekassignment.repository

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class TrendingReposRepository (private val database: RepositoriesDatabase) {

    val repositories: LiveData<List<Repository>> = Transformations.map(database.repositoriesDatabaseDao.getAllRepositories()) {
        it.asDomainModel()
    }

    private val _apiStatus = MutableLiveData<RepositoriesApiStatus>()

    val apiStatus: LiveData<RepositoriesApiStatus>
        get() = _apiStatus

    suspend fun refreshRepositories() {
        withContext(Dispatchers.IO) {
            try {
                _apiStatus.postValue(RepositoriesApiStatus.LOADING)
                val getRepositoriesDeferred = Network.repositories.getAllRepositories()
                val repositories = getRepositoriesDeferred.await()

                database.repositoriesDatabaseDao.insertAllRepositories(repositories.asDBModel())
                _apiStatus.postValue(RepositoriesApiStatus.SUCCESS)
            } catch (e: Exception) {
                _apiStatus.postValue(RepositoriesApiStatus.ERROR)
            }
        }
    }

    suspend fun getRepositories(sortType: RepositorySort): List<Repository> {
        return withContext(Dispatchers.IO) {
            val repos: List<DatabaseRepository> = when (sortType) {
                RepositorySort.SORT_NAME -> database.repositoriesDatabaseDao.getAllRepositoriesSortedByName()
                RepositorySort.SORT_STAR -> database.repositoriesDatabaseDao.getAllRepositoriesSortedByStars()
                else -> database.repositoriesDatabaseDao.getAllRepositoriesSortedByName()
            }

            repos.asDomainModel()
        }
    }
}