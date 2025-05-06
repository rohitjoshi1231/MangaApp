package com.alpha.features.signin.domain.repository

import com.alpha.features.signin.domain.model.User

interface AuthRepository {
    suspend fun signUp(user: User): Boolean
}