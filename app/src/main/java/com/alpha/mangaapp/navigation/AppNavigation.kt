package com.alpha.mangaapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alpha.features.manga.presentation.ui.MangaDescriptionScreen
import com.alpha.features.manga.presentation.ui.MangaScreen
import com.alpha.features.signin.presentation.ui.HomeScreen
import com.alpha.features.signin.presentation.ui.LoginScreen
import com.alpha.features.signin.presentation.ui.SignUpScreen
import com.alpha.mangaapp.presentation.ui.SplashScreen

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val SPLASH = "splash"
    const val HOME = "home"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(onLoginRequired = {
                navController.navigate(Routes.SIGNUP) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }, onSessionActive = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }


        composable(Routes.LOGIN) {

            LoginScreen(onLogin = { _, email, _ ->

                navController.navigate(Routes.HOME) {
                    Log.d("LoginScreen", "Login button clicked: $email")
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }, onSignUpClick = {
                navController.navigate(Routes.SIGNUP)
            })
        }


        composable("manga_screen") {
            MangaScreen(navController = navController)
        }
        composable("manga_description_screen/{mangaId}") { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getString("mangaId") ?: ""
            MangaDescriptionScreen(mangaId = mangaId)
        }

        composable(Routes.SIGNUP) {
            SignUpScreen(onSignUp = { _, _, _ ->
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SIGNUP) { inclusive = true }
                }
            }, onLoginClick = {
                navController.navigate(Routes.LOGIN)
            })
        }


        composable(Routes.HOME) {
            HomeScreen(navController)
        }
    }
}
