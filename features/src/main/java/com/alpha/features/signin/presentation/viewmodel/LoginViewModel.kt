package com.alpha.features.signin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.core.data.database.UserEntity
import com.alpha.core.data.repository.UserRepository
import com.alpha.core.data.session.UserSessionManager
import com.alpha.features.signin.presentation.state.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository, private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val loginState: StateFlow<SignupUiState> = _loginState


    private val _allUsers = MutableStateFlow<List<UserEntity>>(emptyList())
    val allUsers = _allUsers.asStateFlow()

    fun fetchAllUsers() {
        viewModelScope.launch {
            val users = userRepository.getAllUsers()
            println("Fetched users: $users")
            _allUsers.value = users
        }
    }


    fun saveUserSession(isLoggedIn: Boolean, email: String) {

        viewModelScope.launch {
            userSessionManager.saveSession(isLoggedIn, email)
        }


        if (isLoggedIn) {
            // Proceed with the UI for logged-in users
        } else {
            // Show login screen or prompt for login
        }
    }


    suspend fun login(email: String, password: String) {
        _loginState.value = SignupUiState.Loading
        try {
            val user = userRepository.login(email, password)
            if (user != null) {
                userSessionManager.saveSession(true, email)
                _loginState.value = SignupUiState.Success
            } else {
                _loginState.value = SignupUiState.Error("Invalid email or password")
            }
        } catch (e: Exception) {
            _loginState.value = SignupUiState.Error(e.localizedMessage ?: "Login failed")
        }

    }
}

