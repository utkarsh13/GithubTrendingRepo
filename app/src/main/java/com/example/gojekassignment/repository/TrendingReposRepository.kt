package com.example.gojekassignment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.gojekassignment.Database.RepositoriesDatabase
import com.example.gojekassignment.Database.asDomainModel
import com.example.gojekassignment.domain.Repository
import com.example.gojekassignment.network.Network
import com.example.gojekassignment.network.asDBModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrendingReposRepository (private val database: RepositoriesDatabase) {

    val repositories: LiveData<List<Repository>> = Transformations.map(database.repositoriesDatabaseDao.getAllRepositories()) {
        it.asDomainModel()
    }

    suspend fun refreshRepositories() {
        withContext(Dispatchers.IO) {
            val repositories = Network.repositories.getAllRepositories().await()
            database.repositoriesDatabaseDao.insertAllRepositories(repositories.asDBModel())
        }
    }

}