package com.alpha.features.signin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.core.data.database.Manga
import com.alpha.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    // Manga list that is exposed as StateFlow
    private val _mangaList = MutableStateFlow<List<Manga>>(emptyList())
    val mangaList: StateFlow<List<Manga>> = _mangaList

    // Load cached manga list from the repository
    fun loadCachedManga() {
        viewModelScope.launch {
            val cached = userRepository.getCachedManga()
            _mangaList.value = cached
        }
    }

    fun cacheMangaList(mangaList: List<Manga>) {
        viewModelScope.launch {
            userRepository.cacheMangaList(mangaList)
        }
    }

    fun appendMangaList(newData: List<Manga>) {
        _mangaList.value += newData
    }

    fun clearMangaList() {
        _mangaList.value = emptyList()
    }
}
