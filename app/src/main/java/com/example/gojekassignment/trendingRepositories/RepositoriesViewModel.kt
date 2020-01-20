package com.example.gojekassignment.trendingRepositories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gojekassignment.Database.RepositoriesDatabase
import com.example.gojekassignment.domain.Repository
import com.example.gojekassignment.repository.TrendingReposRepository
import kotlinx.coroutines.*

enum class RepositoriesApiStatus { LOADING, ERROR, SUCCESS, OFFLINE }

enum class RepositorySort{ SORT_NAME, SORT_STAR, DEFAULT }

class RepositoriesViewModel (application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    val database = RepositoriesDatabase.getInstance(application)
    val trendingReposRepository = TrendingReposRepository(database, application.applicationContext)

    var repositories = trendingReposRepository.repositories

    var repositoriesSorted: MutableLiveData<List<Repository>> = MutableLiveData()

    private val _repositoriesApiStatus by lazy {
        trendingReposRepository.apiStatus
    }
    val repositoriesApiStatus: LiveData<RepositoriesApiStatus>
        get() = _repositoriesApiStatus

    var expandedPosition: Int = -1

    init {
        refreshRepositories()
    }

    fun refreshRepositories() {
        resetExpandedPosition()
        viewModelScope.launch {
            trendingReposRepository.refreshRepositories()
        }
    }

    fun updateSort(sortType: RepositorySort) {
        resetExpandedPosition()
        viewModelScope.launch {
            repositoriesSorted.postValue(trendingReposRepository.getRepositories(sortType))
        }
    }

    private fun resetExpandedPosition() {
        expandedPosition = -1
    }

    fun retryClicked() {
        refreshRepositories()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}