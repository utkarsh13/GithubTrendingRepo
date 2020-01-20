package com.example.gojekassignment.trendingRepositories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gojekassignment.Database.RepositoriesDatabase
import com.example.gojekassignment.domain.Repository
import com.example.gojekassignment.repository.TrendingReposRepository
import kotlinx.coroutines.*

enum class RepositoriesApiStatus { LOADING, ERROR, SUCCESS }

enum class RepositorySort{ SORT_NAME, SORT_STAR, DEFAULT }

class RepositoriesViewModel (application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    val database = RepositoriesDatabase.getInstance(application)
    val trendingReposRepository = TrendingReposRepository(database)

    var repositories = trendingReposRepository.repositories

    var repositoriesSorted: MutableLiveData<List<Repository>> = MutableLiveData()

    private val _repositoriesApiStatus by lazy {
        trendingReposRepository.apiStatus
    }
    val repositoriesApiStatus: LiveData<RepositoriesApiStatus>
        get() = _repositoriesApiStatus

    var expandedPosition: Int = -1

    init {
        viewModelScope.launch {
            trendingReposRepository.refreshRepositories()
        }
    }

    fun updateSort(sortType: RepositorySort) {
        viewModelScope.launch {
            repositoriesSorted.postValue(trendingReposRepository.getRepositories(sortType))
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}