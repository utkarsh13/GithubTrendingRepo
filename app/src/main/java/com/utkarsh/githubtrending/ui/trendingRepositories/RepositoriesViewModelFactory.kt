package com.utkarsh.githubtrending.trendingrepositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utkarsh.githubtrending.model.repository.TrendingReposRepository

class RepositoriesViewModelFactory(val trendingReposRepository: TrendingReposRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom( RepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepositoriesViewModel(trendingReposRepository) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }


}