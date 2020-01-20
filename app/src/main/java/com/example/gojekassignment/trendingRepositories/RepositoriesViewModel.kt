package com.example.gojekassignment.trendingRepositories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gojekassignment.Database.RepositoriesDatabase
import com.example.gojekassignment.repository.TrendingReposRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

enum class RepositoriesApiStatus { LOADING, ERROR, SUCCESS }

class RepositoriesViewModel (application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    val database = RepositoriesDatabase.getInstance(application)
    val trendingReposRepository = TrendingReposRepository(database)

    val repositories = trendingReposRepository.repositories

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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}