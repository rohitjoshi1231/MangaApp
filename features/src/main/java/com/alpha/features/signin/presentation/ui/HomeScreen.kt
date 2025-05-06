package com.alpha.features.signin.presentation.ui

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alpha.features.face.domain.FaceDetection
import com.alpha.features.manga.presentation.ui.MangaScreen
import com.alpha.features.profile.presentation.ui.ProfileScreen
import com.alpha.features.profile.presentation.viewmodels.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val bottomNavController = rememberNavController()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val email by profileViewModel.email.collectAsState()

    println("email: $email")
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(bottomBar = {
        BottomNavigationBar(navController = bottomNavController, email)
    },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                bottomNavController.navigate("face")
            }) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Center FAB")
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.Center,
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                NavHost(
                    navController = bottomNavController, startDestination = "manga"
                ) {
                    composable("manga") {
                        MangaScreen(navController)
                    }

                    composable("profile/{email}") { backStackEntry ->
                        val emailArg = backStackEntry.arguments?.getString("email") ?: ""
                        ProfileScreen(email = emailArg) {
                            navController.navigate("login")
                        }
                    }

                    composable("face") {
                        FaceDetection()
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, email: String) {
    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Default.MenuBook, contentDescription = "Manga") },
            label = { Text("Manga") },
            selected = currentRoute(navController) == "manga",
            onClick = { navController.navigate("manga") })

        NavigationBarItem(icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute(navController)?.startsWith("profile") == true,
            onClick = {
                if (email.isNotBlank()) {
                    navController.navigate("profile/$email")
                }
            })
    }
}


@Composable
fun currentRoute(navController: NavHostController): String? {
    return navController.currentBackStackEntryAsState().value?.destination?.route
}


