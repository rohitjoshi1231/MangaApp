package com.alpha.features.signin.presentation.ui


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alpha.core.data.session.UserSessionManager
import com.alpha.features.signin.presentation.state.SignupUiState
import com.alpha.features.signin.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String, String, String) -> Unit,
    onSignUpClick: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val userSessionManager = remember { UserSessionManager(context) }


    val username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val isFormValid = email.contains("@") && password.length >= 6


    val loginState by loginViewModel.loginState.collectAsState()


    val users by loginViewModel.allUsers.collectAsState()

    LaunchedEffect(Unit) {
        loginViewModel.fetchAllUsers()
    }

    LaunchedEffect(users) {
        users.forEach { user ->
            println("Username: ${user.name}, Email: ${user.email}, Password: ${user.password}")
        }
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is SignupUiState.Success -> {
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                // Save session in coroutine
                CoroutineScope(Dispatchers.Main).launch {
                    userSessionManager.saveSession(true, email)
                    onLogin(username, email, password)
                }
            }

            is SignupUiState.Error -> {
                println(error)
                Toast.makeText(
                    context, (loginState as SignupUiState.Error).message, Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }




    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(value = email.trim(),
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = password.trim(),
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (showPassword) "Hide Password" else "Show Password"
                        )
                    }
                })

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (isFormValid) {
                        error = ""
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                loginViewModel.login(email, password)
                            } catch (e: Exception) {
//                                error = "Invalid email or password"
                                error = e.toString()
                            }
                        }
                    } else {
                        error = "Please fill all fields correctly"
                    }
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }

            if (error.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onSignUpClick) {
                Text("Don't have an account? Sign up")
            }
        }
    }
}

