package com.utkarsh.githubtrending.ui.trendingRepositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utkarsh.githubtrending.data.repository.TrendingReposRepository

class RepositoriesViewModelFactory(private val trendingReposRepository: TrendingReposRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom( RepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepositoriesViewModel(trendingReposRepository) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }


}