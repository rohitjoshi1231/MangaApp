package com.alpha.core.data.database

data class Manga(
    val id: String,
    val title: String,
    val subTitle: String,
    val thumb: String,
    val summary: String,
    val genres: List<String>
)
