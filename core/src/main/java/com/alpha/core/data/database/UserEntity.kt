package com.alpha.core.data.database


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
)

@Entity(tableName = "manga")
data class MangaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subTitle: String,
    val thumb: String,
    val summary: String,
    val genres: String
)

fun Manga.toEntity() = MangaEntity(id, title, subTitle, thumb, summary, genres.joinToString(","))