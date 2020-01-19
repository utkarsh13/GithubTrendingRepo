package com.example.gojekassignment.trendingRepositories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RepositoriesViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepositoriesViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }


}