package com.example.gojekassignment.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repository_table")
data class Repository(
    @PrimaryKey
    val author: String,

    val name: String,

    val avatar: String?,

    val url: String,

    val description: String?,

    val language: String,

    val languageColor: String,

    val stars: Int,

    val forks: Int,

    val currentPeriodStars: Int
)
