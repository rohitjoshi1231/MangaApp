package com.alpha.mangaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alpha.mangaapp.navigation.AppNavigation
import com.alpha.mangaapp.presentation.ui.theme.MangaAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MangaAppTheme {
                AppNavigation()
            }
        }
    }
}
