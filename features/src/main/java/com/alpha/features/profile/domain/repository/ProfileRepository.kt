package com.alpha.features.profile.domain.repository

import com.alpha.core.data.database.UserDao
import com.alpha.core.data.database.UserEntity
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val userDao: UserDao) {


    suspend fun getUser(email: String): UserEntity? {
        // Assuming you're querying the user by email
        return userDao.getUserByEmail(email)
    }


}
