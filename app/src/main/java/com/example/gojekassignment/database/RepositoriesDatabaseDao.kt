package com.example.gojekassignment.database

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Query("DELETE FROM repository_table")
    fun clear()

}