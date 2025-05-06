package com.alpha.features.manga.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.alpha.core.data.database.Manga
import com.alpha.features.manga.data.FetchType
import com.alpha.features.manga.data.fetchMangaData
import com.alpha.features.signin.presentation.viewmodel.MangaViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun MangaScreen(navController: NavController, mangaViewModel: MangaViewModel = hiltViewModel()) {
    var mangaList by remember { mutableStateOf<List<Manga>>(emptyList()) }
    var page by remember { mutableIntStateOf(1) }
    var hasMoreData by remember { mutableStateOf(true) }

    val context = LocalContext.current

    val cachedManga by mangaViewModel.mangaList.collectAsState()

    LaunchedEffect(Unit) {
        mangaViewModel.loadCachedManga()
    }


    LaunchedEffect(cachedManga) {
        if (cachedManga.isNotEmpty() && mangaList.isEmpty()) {
            mangaList = cachedManga
        }
    }


    LaunchedEffect(page) {
        if (page == 1 && mangaList.isNotEmpty()) return@LaunchedEffect

        fetchMangaData(context, FetchType.ALL) { data ->
            mangaViewModel.cacheMangaList(data)  // Cache the new data
            if (data.isEmpty()) {
                hasMoreData = false
            } else {
                mangaViewModel.appendMangaList(data) // Append to the existing list
            }
        }
    }



    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), modifier = Modifier.padding(12.dp)
            ) {
                items(mangaList) { manga ->
                    MangaItem(manga = manga, onClick = { mangaID ->
                        navController.navigate("manga_description_screen/$mangaID")
                    })
                }
            }

            if (hasMoreData) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Load More",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Blue,
                        modifier = Modifier.clickable { page += 1 })
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No more manga to load", style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Composable
fun MangaDescriptionScreen(mangaId: String) {
    var manga by remember { mutableStateOf<Manga?>(null) }
    val context = LocalContext.current

    LaunchedEffect(mangaId) {
        fetchMangaData(context, FetchType.ID, mangaId) { fetchedMangaList ->
            if (fetchedMangaList.isNotEmpty()) {
                manga = fetchedMangaList.first()
            }
        }
    }

    manga?.let {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Title
                Text(text = it.title, style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(8.dp))

                // Thumbnail
                AsyncImage(
                    model = it.thumb,
                    contentDescription = it.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(text = it.summary, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                // Genres
                if (it.genres.isNotEmpty()) {
                    Text(text = "Genres:", style = MaterialTheme.typography.bodyMedium)
                    it.genres.forEach { genre ->
                        Text(text = genre, style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle
                Text(
                    text = "Subtitle: ${it.subTitle}", style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } ?: run {
        Text(text = "Manga not found", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun MangaItem(manga: Manga, onClick: (String) -> Unit) {
    var isImageLoading by remember { mutableStateOf(true) }
    var isTitleLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(manga.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image with shimmer
        AsyncImage(model = manga.thumb,
            contentDescription = manga.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .placeholder(
                    visible = isImageLoading,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.shimmer()
                ),
            onSuccess = { isImageLoading = false },
            onError = { isImageLoading = false })

        Spacer(modifier = Modifier.height(8.dp))

        LaunchedEffect(manga.title) {
            if (manga.title.isNotEmpty()) {
                isTitleLoading = false
            }
        }
        Text(
            text = manga.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.placeholder(
                visible = isTitleLoading, highlight = PlaceholderHighlight.shimmer()
            )
        )
    }
}

