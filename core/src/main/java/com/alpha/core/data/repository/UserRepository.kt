package com.alpha.core.data.repository


import android.util.Log
import com.alpha.core.data.database.Manga
import com.alpha.core.data.database.MangaEntity
import com.alpha.core.data.database.UserDao
import com.alpha.core.data.database.UserEntity
import com.alpha.core.data.database.toEntity
import javax.inject.Inject

open class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun register(user: UserEntity) {
        Log.d("Database", "Inserting user: ${user.name}, ${user.email}")
        userDao.insertUser(user)
    }


    suspend fun cacheMangaList(mangaList: List<Manga>) {
        userDao.cacheManga(mangaList.map { it.toEntity() })
    }

    suspend fun getCachedManga(): List<Manga> {
        return userDao.getCachedManga().map { it.toDomainModel() }
    }

    suspend fun getAllUsers(): List<UserEntity> = userDao.getAllUsers()


    suspend fun login(email: String, password: String) = userDao.login(email, password)

}


fun MangaEntity.toDomainModel(): Manga = Manga(
    id = id,
    title = title,
    subTitle = subTitle,
    thumb = thumb,
    summary = summary,
    genres = genres.split(",")
)
