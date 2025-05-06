package com.alpha.features.profile.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.core.data.database.UserEntity
import com.alpha.core.data.session.UserSessionManager
import com.alpha.features.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: MutableStateFlow<UserEntity?> = _user


    init {
        viewModelScope.launch {
            userSessionManager.userEmail.collect { email ->
                if (!email.isNullOrEmpty()) {
                    getUser(email)
                }
            }
        }
    }

    // Get user by email and update the state
    fun getUser(email: String) {
        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "Fetching user with email: $email")
                val userEntity = profileRepository.getUser(email)
                if (userEntity != null) {
                    _user.value = userEntity
                    _email.value = email
                    Log.d("ProfileViewModel", "User found: ${userEntity.name}")
                } else {
                    _user.value = null
                    Log.d("ProfileViewModel", "No user found for email: $email")
                }
            } catch (e: Exception) {
                _user.value = null
                Log.e("ProfileViewModel", "Error fetching user: ${e.message}")
            }
        }
    }
}

