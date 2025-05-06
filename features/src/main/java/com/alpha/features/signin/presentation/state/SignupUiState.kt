package com.alpha.features.signin.presentation.state

sealed class SignupUiState {
    object Idle : SignupUiState() // Initial state
    object Loading : SignupUiState() // When a request is in progress
    object Success : SignupUiState() // When the registration is successful
    data class Error(val message: String) : SignupUiState() // When an error occurs
}
