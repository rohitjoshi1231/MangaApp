package com.alpha.features.signin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.core.data.database.UserEntity
import com.alpha.core.data.repository.UserRepository
import com.alpha.features.signin.presentation.state.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _signupState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val signupState: StateFlow<SignupUiState> = _signupState



    // Register function for signing up users
    fun register(user: UserEntity) {
        viewModelScope.launch {
            _signupState.value = SignupUiState.Loading
            try {
                userRepository.register(user)
                _signupState.value = SignupUiState.Success
            } catch (e: Exception) {
                _signupState.value = SignupUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}
