package com.example.gojekassignment.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gojekassignment.domain.Repository

@Entity(tableName = "repository_table")
data class DatabaseRepository(
    @PrimaryKey
    val author: String,

    val name: String?,

    val avatar: String?,

    val url: String?,

    val description: String?,

    val language: String?,

    val languageColor: String?,

    val stars: Int?,

    val forks: Int?,

    val currentPeriodStars: Int?
)

fun List<DatabaseRepository>.asDomainModel(): List<Repository> {
    return map {
        Repository(
            author = it.author,
            name = it.name,
            avatar = it.avatar,
            url = it.url,
            description = it.description,
            language = it.language,
            languageColor = it.languageColor,
            stars = it.stars,
            forks = it.forks
        )
    }
}
