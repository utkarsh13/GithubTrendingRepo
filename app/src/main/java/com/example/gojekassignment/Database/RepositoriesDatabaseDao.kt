package com.example.gojekassignment.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepositoriesDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRepositories(databaseRepositories: List<DatabaseRepository>)

    @Query("SELECT * FROM repository_table")
    fun getAllRepositories(): LiveData<List<DatabaseRepository>>

    @Query("SELECT * FROM repository_table")
    fun getAllRepositoriesList(): List<DatabaseRepository>

    @Query("SELECT * FROM repository_table ORDER BY name ASC")
    fun getAllRepositoriesSortedByName(): List<DatabaseRepository>

    @Query("SELECT * FROM repository_table ORDER BY stars DESC")
    fun getAllRepositoriesSortedByStars(): List<DatabaseRepository>

}