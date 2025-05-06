package com.alpha.mangaapp.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.alpha.core.data.session.UserSessionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSessionActive: () -> Unit, onLoginRequired: () -> Unit
) {
    val context = LocalContext.current
    val userSessionManager = remember { UserSessionManager(context) }
    val isLoggedIn by userSessionManager.userLoggedIn.collectAsState(initial = false)

    LaunchedEffect(Unit) {

        delay(1200)
        delay(1000) // Optional splash delay
        if (isLoggedIn) onSessionActive() else onLoginRequired()
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text("MangaApp", style = MaterialTheme.typography.headlineLarge)
    }
}
